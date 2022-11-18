package com.syraven.cloud.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.syraven.cloud.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author syrobin
 * @version v1.0
 * @description: 手机实体
 * @date 2022-06-25 17:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Product", description = "产品实体")
public class Product extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7740284161641516541L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("产品id")
    private String id;
    @ApiModelProperty("产品名称")
    private String name;
    @ApiModelProperty("产品价格")
    private BigDecimal price;
    @ApiModelProperty("产品图片")
    private String img;
    @ApiModelProperty("产品链接")
    private String link;
    @ApiModelProperty("产品描述")
    private String description;
    @ApiModelProperty("产品类型")
    private String type;
    @ApiModelProperty("产品品牌")
    private String brand;
    @ApiModelProperty("产品型号")
    private String model;
    @ApiModelProperty("产品排行")
    private String ranking;

}
