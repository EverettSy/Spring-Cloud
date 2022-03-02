package com.syraven.cloud.service;

import feign.Param;
import feign.RequestLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: GitHub
 * @Description:
 * @Author syrobin
 * @Date 2022-01-15 1:16 PM
 * @Version V1.0
 */
public interface GitHub {

    /**
     * 定义get方法，包括路径参数，响应返回序列化类
     * @param owner
     * @param repository
     * @return
     */
    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(@Param("owner") String owner,
                                   @Param("repo") String repository);


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Contributor {

        String login;
        int contributions;

    }
}
