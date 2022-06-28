package com.syraven.cloud.controller;

import com.google.common.collect.Lists;
import com.syraven.cloud.domain.Phone;
import com.syraven.cloud.record.CommonResult;
import com.syraven.cloud.service.PhoneService;
import com.syraven.cloud.vo.DynamicVO;
import com.syraven.cloud.vo.LeaderBoardVO;
import com.syraven.cloud.vo.PhoneVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-25 19:08
 */
@RestController
@RequestMapping("/short/phone")
@Api(tags = "Phone")
public class PhoneController {

    List<Phone> phones = Lists.newArrayList(new Phone(1, "苹果"),
            new Phone(2, "小米"),
            new Phone(3, "华为"),
            new Phone(4, "一加"),
            new Phone(5, "vivo"));

    @Autowired
    private PhoneService phoneService;


    @ApiOperation(value = "获取手机排行榜", notes = "获取手机排行榜", httpMethod = "GET")
    @GetMapping("/home")
    public CommonResult<LeaderBoardVO> home() {
        for (Phone phone : phones) {
            int ranking = phoneService.phoneRank(phone.getId()) + 1;
            phone.setRanking(ranking == 0 ? "榜上无名" : "销量排名：" + ranking);
        }

        List<PhoneVO> phbList = phoneService.getPhbList();
        List<DynamicVO> dynamicList = phoneService.getBuyDynamic();
        LeaderBoardVO leaderBoardVO = new LeaderBoardVO();
        leaderBoardVO.setPhoneList(phones);
        leaderBoardVO.setDynamicList(dynamicList);
        leaderBoardVO.setPhoneVOList(phbList);
        return CommonResult.success(leaderBoardVO);
    }

    /**
     * 模拟购买手机
     *
     * @param phoneId
     * @return
     */
    @ApiOperation(value = "购买手机", httpMethod = "GET")
    @GetMapping("/buy/{phoneId}")
    public CommonResult<String> buyPhone(@PathVariable int phoneId) {
        phoneService.buyPhone(phoneId);
        return CommonResult.success("购买成功");
    }

    /**
     * 获得指定手机的排名
     *
     * @param phoneId
     * @return
     */
    @ApiOperation(value = "获取指定手机的排名", httpMethod = "GET")
    @GetMapping("/ranking/{phoneId}")
    public CommonResult phoneRanking(@PathVariable int phoneId) {
        return CommonResult.success(phoneService.phoneRank(phoneId));
    }

    /**
     * 清空缓存
     *
     * @return
     */
    @ApiOperation(value = "清空缓存", httpMethod = "GET")
    @GetMapping("/clear")
    public CommonResult<String> clear() {
        phoneService.clear();
        return CommonResult.success("清空缓存成功");
    }


}
