package com.syraven.cloud.vo;

import com.syraven.cloud.domain.Phone;
import lombok.Data;

import java.util.List;

/**
 * @author syrobin
 * @version v1.0
 * @description: 排行榜VO
 * @date 2022-06-25 19:53
 */
@Data
public class LeaderBoardVO {

    List<Phone> phoneList;
    List<PhoneVO> phoneVOList;
    List<DynamicVO> dynamicList;

}
