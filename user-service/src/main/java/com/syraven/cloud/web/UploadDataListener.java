package com.syraven.cloud.web;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.syraven.cloud.domain.UploadData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <<模板的读取类>>
 *
 * @author Raven
 * @date 2020/6/30 17:23
 */
public class UploadDataListener extends AnalysisEventListener<UploadData> {


    public static final Logger LOGGER = LoggerFactory.getLogger(UploadDataListener.class);

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    public static final int BATCH_COUNT = 5;

    List<UploadData> dataList = new ArrayList<>();

    @Autowired
    private UploadDataMapper uploadDataMapper;


    public UploadDataListener(UploadDataMapper uploadDataMapper) {
        this.uploadDataMapper = uploadDataMapper;
    }

    /**
     * 这个每一条数据解析都会来调用
     * @param uploadData
     * @param analysisContext
     */
    @Override
    public void invoke(UploadData uploadData, AnalysisContext analysisContext) {
        LOGGER.info("解析到一条数据：{}", JSON.toJSONString(dataList));
        dataList.add(uploadData);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (dataList.size() >= BATCH_COUNT){
            saveData();
            //存储完清理掉
            dataList.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", dataList.size());
        dataList.forEach(n -> uploadDataMapper.insert(n));
        LOGGER.info("存储数据库成功！");
    }
}