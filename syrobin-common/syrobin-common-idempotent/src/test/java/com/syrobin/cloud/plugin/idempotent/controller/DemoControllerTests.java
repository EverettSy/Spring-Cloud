package com.syrobin.cloud.plugin.idempotent.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @ClassName: DemoControllerTests
 * @Description: 单元测试
 * @Author syrobin
 * @Date 2021-12-01 6:45 下午
 * @Version V1.0
 */

@SpringBootApplication(scanBasePackages = "com.syrobin.cloud.plugin.idempotent")
@SpringBootTest
class DemoControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 单线程测试
     * @throws Exception
     */
    @Test
    void getOneThreadResutTest() throws Exception{
        mockMvc.perform(get("/get?key=1")).andExpect(status().isOk()).andReturn();
    }


    /**
     * 多线程测试
     */
    @RepeatedTest(10)
    @Execution(CONCURRENT)
    void getMultiThreadResutTest() {
        try {
            mockMvc.perform(get("/get?key=10")).andExpect(status().isOk());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RepeatedTest(10)
    @Execution(CONCURRENT)
    void getMultiThreadNoKeyResutTest() {
        try {
            mockMvc.perform(get("/noKey")).andExpect(status().isOk());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
