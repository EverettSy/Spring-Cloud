package com.syraven.cloud.service.impl;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: huacailiang
 * @date: 2020/5/17
 * @description:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value="SmsVo",description="调用短信服务请求参数类" )
public class SmsSendDTO implements Serializable {

  @ApiModelProperty(value = "发送电话")
  private String phone;

  @ApiModelProperty(value = "模板编号")
  private String tempCode;

  @ApiModelProperty(value = "参数")
  private Map<String, String> param;

  @ApiModelProperty(value = "签名",example="【工保科技】")
  private String sign;
}
