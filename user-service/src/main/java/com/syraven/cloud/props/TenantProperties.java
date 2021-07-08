package com.syraven.cloud.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 租户属性
 *
 * @author SyRAVEN
 * @since 2021-03-31 08:57
 */
@Getter
@Setter
@RefreshScope
@Component
@ConfigurationProperties(prefix = "raven.tenant")
public class TenantProperties {

    /**
     * 是否开启租户模式
     */
    private Boolean enable = true;

    /**
     * 需要排除的多租户的表
     */
    private List<String> ignoreTables = Arrays.asList("mate_sys_user", "mate_sys_depart", "mate_sys_role", "mate_sys_tenant", "mate_sys_role_permission");

    /**
     * 多租户字段名称
      */
    private String column = "tenant_id";

    /**
     * 排除不进行租户隔离的sql
     * 样例全路径：vip.mate.system.mapper.UserMapper.findList
     */
    private List<String> ignoreSqls = new ArrayList<>();




}