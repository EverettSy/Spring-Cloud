package com.syraven.cloud.domain;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <<基础数据类>>
 *
 * @author Raven
 * @date 2020/6/30 16:51
 */
@Data
@ToString
public class UploadData {

    private Integer id;
    private String name;
    private Date uploadDate;
    private BigDecimal doubleData;
}


    /**
     * 根据houseNoId更新开发房源信息
     *
     * @param entrustContractDto
     * @return
     *//*
    @Override
    public Result<Boolean> updateEntrustContractDto(UpdateEntrustContractDto entrustContractDto) {
        //判断房源状态
        Wrapper<HouseDev> houseDevWrapper = new QueryWrapper<HouseDev>().lambda()
                .eq(HouseDev::getComId, entrustContractDto.getComId())
                .eq(HouseDev::getHouseNoId, entrustContractDto.getHouseNoId())
                .eq(HouseDev::getDeleted, 0);
        List<HouseDev> houseDevList = houseDevMapper.selectList(houseDevWrapper);

        HouseDev houseDev = houseDevMapper.selectById(houseDevList.get(0).getId());
        if (houseDev == null) {
            return ResultUtils.failedResult("未找到对应房源信息");
        }
        //增加房源跟进
        HouseDevFollowUp houseDevFollowUp = new HouseDevFollowUp();
        //变更房源状态写入房源状态
        //合同审核状态
        if ("NEWSIGNATURE".equals(entrustContractDto.getContractStatus())) {
            if (DictConstants.HouseDevAuditType.SECONDAUDIT.getCode().equals(houseDev.getAuditType()) &&
                    DictConstants.HouseDevAuditStatus.PASS.getCode().equals(houseDev.getAuditStatus())) {

                //变更房源状态为已签约状态
                houseDev.setHouseStatus(DictConstants.HouseStatus.SIGNED.getCode());
                //增加房源跟进
                houseDevFollowUp.setFollowUpType(DictConstants.FollowupType.SYSTEMFOLLOW.getCode());
                houseDevFollowUp.setSubFollowUpType(DictConstants.SubFollowupType.STATUSCHANGE.getCode());
                houseDevFollowUp.setFollowUpContent("房源状态变更为" + DictConstants.HouseStatus.SIGNED.getName());


                //根据housedev_id查关联业主信息
                Wrapper<OwnerInfoHouseDev> queryOwnerInfoHouseDevWrapper = new QueryWrapper<OwnerInfoHouseDev>().lambda()
                        .eq(OwnerInfoHouseDev::getHouseDevId, houseDev.getId());
                List<OwnerInfoHouseDev> ownerInfoHouseDevList = ownerInfoHouseDevMapper.selectList(queryOwnerInfoHouseDevWrapper);
                if (ownerInfoHouseDevList.size() == 0) {
                    return ResultUtils.failedResult("没找到业主房源关联关系");
                }
//                for (OwnerInfoHouseDev ownerInfoHouseDev : ownerInfoHouseDevList) {
                //根据业主Code查找业主信息
                Wrapper<OwnerInfo> queryOwnerInfoWrapper = new QueryWrapper<OwnerInfo>().lambda()
                        .eq(OwnerInfo::getOwnerCode, entrustContractDto.getOwnerCode());
                OwnerInfo ownerInfo = ownerInfoMapper.selectOne(queryOwnerInfoWrapper);
                if (ownerInfo != null) {
                    if (!DictConstants.OwnerStatus.SIGNEDUP.getCode().equals(ownerInfo.getOwnerStatus())) {
                        ownerInfo.setOwnerStatus(DictConstants.OwnerStatus.SIGNEDUP.getCode());
                        ownerInfo.setUpdateUid(entrustContractDto.getUid());
                        ownerInfoMapper.updateById(ownerInfo);

                        //写入业主跟进
                        OwnerFollowUp ownerFollowUp = new OwnerFollowUp();
                        ownerFollowUp.setFollowUpType(DictConstants.FollowupType.SYSTEMFOLLOW.getCode());
                        ownerFollowUp.setSubFollowUpType(DictConstants.SubFollowupType.STATUSCHANGE.getCode());
                        ownerFollowUp.setFollowUpContent(entrustContractDto.getUpdateUName() + "变更业主状态为" + DictConstants.OwnerStatus.SIGNEDUP.getName());
                        ownerFollowUp.setOwnerInfoId(ownerInfo.getId());
                        ownerFollowUp.setCreateUid(entrustContractDto.getUid());
                        ownerFollowUp.setUpdateUid(entrustContractDto.getUid());
                        ownerFollowUp.setFollowUpName(entrustContractDto.getUpdateUName());
                        ownerFollowUp.setFollowUpDname(entrustContractDto.getUpdateDName());
                        ownerFollowUp.setCancelReason("");
                        ownerFollowUp.setRemark("");

                        followUpMapper.insert(ownerFollowUp);
                    }
                }
//                }
            }
        } else if ("TERMINATION".equals(entrustContractDto.getContractStatus())) {
            //检查是否在100天内通过的实勘
            Boolean examineTag = true;
            Wrapper<HouseExamine> queryHouseExamineWrapper = new QueryWrapper<HouseExamine>().lambda()
                    .eq(HouseExamine::getHouseDevId, houseDev.getId())
                    .eq(HouseExamine::getDeleted, 0)
                    .orderByDesc(HouseExamine::getUpdateTime);

            List<HouseExamine> houseExamineList = houseExamineMapper.selectList(queryHouseExamineWrapper);
            if (houseExamineList.size() == 0) {
                examineTag = false;
            }
            if (!DictConstants.HouseDevAuditStatus.PASS.getCode().equals(houseExamineList.get(0).getAuditStatus())) {
                examineTag = false;
            }
            if (DateUtil.daysBetween(houseExamineList.get(0).getUpdateTime(), new Date()) > 100) {
                examineTag = false;
            }
            //变更房源状态为待实勘状态
            if (examineTag) {
                houseDev.setHouseStatus(DictConstants.HouseStatus.TOBESIGN.getCode());
            } else {
                houseDev.setHouseStatus(DictConstants.HouseStatus.TOBE_EXAMINE.getCode());
            }

            houseDevFollowUp.setFollowUpType(DictConstants.FollowupType.SYSTEMFOLLOW.getCode());
            houseDevFollowUp.setSubFollowUpType(DictConstants.SubFollowupType.STATUSCHANGE.getCode());


            if (examineTag) {
                houseDevFollowUp.setFollowUpContent("房源状态变更为" + DictConstants.HouseStatus.TOBESIGN.getName());
            } else {
                houseDevFollowUp.setFollowUpContent("房源状态变更为" + DictConstants.HouseStatus.TOBE_EXAMINE.getName());
            }
            //根据housedev_id查业主信息
            Wrapper<OwnerInfoHouseDev> queryOwnerInfoHouseDevWrapper = new QueryWrapper<OwnerInfoHouseDev>().lambda()
                    .eq(OwnerInfoHouseDev::getHouseDevId, houseDev.getId());
            List<OwnerInfoHouseDev> ownerInfoHouseDevList = ownerInfoHouseDevMapper.selectList(queryOwnerInfoHouseDevWrapper);
            if (ownerInfoHouseDevList.size() == 0) {
                return ResultUtils.failedResult("没找到业主房源关联关系");
            }
            for (OwnerInfoHouseDev ownerInfoHouseDev : ownerInfoHouseDevList) {
                queryOwnerInfoHouseDevWrapper = new QueryWrapper<OwnerInfoHouseDev>().lambda()
                        .eq(OwnerInfoHouseDev::getId, ownerInfoHouseDev.getId());
                List<OwnerInfoHouseDev> ownerInfoHouseDevIdList = ownerInfoHouseDevMapper.selectList(queryOwnerInfoHouseDevWrapper);

                Boolean invidTag = false;
                if (ownerInfoHouseDevIdList.size() == 1) {
                    invidTag = true;
                } else {
                    List<Integer> houseIds = new ArrayList<>();
                    ownerInfoHouseDevIdList.forEach(n -> houseIds.add(n.getHouseDevId()));
                    Wrapper<HouseDev> queryHouseDevWrapper = new QueryWrapper<HouseDev>().lambda()
                            .in(HouseDev::getId, houseIds)
                            .eq(HouseDev::getHouseStatus, DictConstants.HouseStatus.SIGNED.getCode());
                    if (houseDevMapper.selectList(queryHouseDevWrapper).size() == 1) {
                        invidTag = true;
                    }
                }
                if (invidTag) {
                    Wrapper<OwnerInfo> queryOwnerInfoWrapper = new QueryWrapper<OwnerInfo>().lambda()
                            .eq(OwnerInfo::getOwnerCode, entrustContractDto.getOwnerCode());
                    OwnerInfo ownerInfo = ownerInfoMapper.selectOne(queryOwnerInfoWrapper);
                    *//* OwnerInfo ownerInfo = ownerInfoMapper.selectById(ownerInfoHouseDev.getOwnerInfoId());*//*
                    //变更业主状态为待委托状态
                    if (ownerInfo != null) {
                        ownerInfo.setOwnerStatus(DictConstants.OwnerStatus.TOBEENTRUSTED.getCode());
                        ownerInfo.setUpdateUid(entrustContractDto.getUid());
                        ownerInfoMapper.updateById(ownerInfo);

                        //写入业主跟进
                        OwnerFollowUp ownerFollowUp = new OwnerFollowUp();
                        ownerFollowUp.setFollowUpType(DictConstants.FollowupType.SYSTEMFOLLOW.getCode());
                        ownerFollowUp.setSubFollowUpType(DictConstants.SubFollowupType.STATUSCHANGE.getCode());
                        ownerFollowUp.setFollowUpContent(entrustContractDto.getUpdateUName() + "变更业主状态为" + DictConstants.OwnerStatus.TOBEENTRUSTED.getName());
                        ownerFollowUp.setOwnerInfoId(ownerInfo.getId());
                        ownerFollowUp.setCreateUid(entrustContractDto.getUid());
                        ownerFollowUp.setUpdateUid(entrustContractDto.getUid());
                        ownerFollowUp.setFollowUpName(entrustContractDto.getUpdateUName());
                        ownerFollowUp.setFollowUpDname(entrustContractDto.getUpdateDName());
                        ownerFollowUp.setCancelReason("");
                        ownerFollowUp.setRemark("");

                        followUpMapper.insert(ownerFollowUp);
                    }
                }
            }

        }
        //写入开发房源跟进
        houseDevFollowUp.setHouseDevId(houseDev.getId());
        houseDevFollowUp.setCreateUid(entrustContractDto.getUid());
        houseDevFollowUp.setUpdateUid(entrustContractDto.getDid());
        houseDevFollowUp.setCancelReason("");
        houseDevFollowUp.setRemark("");
        houseDevFollowUp.setFollowUpName(entrustContractDto.getUpdateUName());
        houseDevFollowUp.setFollowUpDname(entrustContractDto.getUpdateDName());
        houseDevFollowUpMapper.insert(houseDevFollowUp);

        //更新开发房源信息
        houseDev.setUpdateUid(entrustContractDto.getUid());
        houseDev.setSignDate(entrustContractDto.getSignDate());
        houseDev.setRealDueDate(entrustContractDto.getRealDueDate());
        houseDevMapper.updateById(houseDev);
        *//*  }*//*
        //业主状态
        //变更业主状态写入业主跟进
        return ResultUtils.successResult(true);
    }*/


