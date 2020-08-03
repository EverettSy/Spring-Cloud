package com.syraven.cloud.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syraven.cloud.domain.CommonResult;
import com.syraven.cloud.domain.DownloadData;
import com.syraven.cloud.domain.UploadData;
import com.syraven.cloud.util.MongoPageHelper;
import com.syraven.cloud.web.UploadDataListener;
import com.syraven.cloud.web.UploadDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/6/30 17:46
 */
@Controller
@RestController
@RequestMapping("/web")
public class WebUploadController {

    @Autowired
    private UploadDataMapper uploadDataMapper;


    @Autowired
    private MongoPageHelper mongoPageHelper;

    /**
     * Excel文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public CommonResult<String> upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener(uploadDataMapper)).sheet().doRead();
        return new CommonResult<>("上传成功", 200);
    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     *
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/download")
    public CommonResult<String> download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("csv", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        Wrapper<UploadData> uploadDataWrapper = new QueryWrapper<UploadData>().lambda()
                .eq(UploadData::getDoubleData, "1.00");
        List<UploadData> downloadDataList = uploadDataMapper.selectList(uploadDataWrapper);
        EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(downloadDataList);
        return new CommonResult<>("下载成功", 200);
    }


    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/downloadFailedUsingJson")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("csv", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            Wrapper<UploadData> uploadDataWrapper = new QueryWrapper<UploadData>().lambda()
                    .eq(UploadData::getDoubleData, "1.00");
            List<UploadData> downloadDataList = uploadDataMapper.selectList(uploadDataWrapper);
            //这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), DownloadData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(downloadDataList);
        } catch (Exception e) {
            //重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>() {{
                put("status","failure");
                put("message","下载文件失败"+e.getMessage());
            }};
            response.getWriter().println(JSON.toJSONString(map));
        }

    }
}