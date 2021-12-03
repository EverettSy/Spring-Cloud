package com.syraven.cloud.spring.context.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ChildBean
 * @Description: 公共Bean类
 * @Author syrobin
 * @Date 2021-12-02 9:26 PM
 * @Version V1.0
 */
@Data
@NoArgsConstructor
public class ChildBean {

    private RootBean fatherBean;
    private String name;
}
