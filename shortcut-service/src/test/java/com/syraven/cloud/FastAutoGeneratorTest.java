package com.syraven.cloud;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName: FastAutoGeneratorTest
 * @Description: 快速生成
 * @Author syrobin
 * @Date 2022-01-12 6:52 PM
 * @Version V1.0
 */
public class FastAutoGeneratorTest {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/spring_cloud?useUnicode=true&useSSL=false&characterEncoding=utf8" +
            "&serverTimezone=UTC",
            "root", "sy199520");


    public static void before(){
        Connection conn = DATA_SOURCE_CONFIG.build().getConn();
    }

    public static void main(String[] args) {
        before();
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                //全局配置
                .globalConfig((scanner,builder) -> builder.author(scanner.apply("请输入作者名称？"))
                        .fileOverride().enableSwagger().commentDate("yyyy-MM-dd").outputDir("/Users/syrobin" +
                                "/IdeaProjects/CodeRepository/Spring-Cloud/shortcut-service/src/main/java"))
                //包配置
                .packageConfig((scanner,builder) -> builder
                        .parent(scanner.apply("请输入包名？"))
                        .entity("domin")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .xml("mapper.xml")
                        .pathInfo(Collections.singletonMap(OutputFile.mapperXml,"/Users/syrobin/IdeaProjects" +
                                "/CodeRepository/Spring-Cloud/shortcut-service/src/main/resources/mapper"))
                        .controller("controller"))
                //策略配置
                .strategyConfig((scanner,builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .entityBuilder()
                        .enableLombok()
                        .addTableFills(new Column("create_time", FieldFill.INSERT))
                        .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
                        .idType(IdType.AUTO)
                        .controllerBuilder()
                        .enableRestStyle()
                        .enableHyphenStyle()
                        .mapperBuilder()
                        .enableMapperAnnotation()

                )
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
