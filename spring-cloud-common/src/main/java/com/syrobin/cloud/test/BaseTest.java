package com.syrobin.cloud.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author syrobin
 * @version v1.0
 * @description:基础测试类 - 以mock方式启动
 * @date 2022-04-21 16:03
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class BaseTest {

    protected final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    protected final String UTF_8 = "UTF-8";
    private final String URL_PATTERN_ALL = "/*";
    protected final String JSON_PATH_RESP_CODE = "$.respCode";
    protected final String JSON_PATH_DATA = "$.data";
    protected final String JSON_PATH_ROWS = "$.rows";
    protected final String JSON_PATH_TOTAL = "$.total";
    protected final String PARAM_PAGE_NO = "pageNo";
    protected final String PARAM_PAGE_SIZE = "pageSize";
    protected final String PARAM_ORDER_BY = "orderBy";
    protected final String PARAM_ORDER_SORT = "orderSort";

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                //添加拦截器 - 设置mockMvc默认响应编码UTF-8
                //覆盖MockHttpServletResponse.characterEncoding默认为为WebUtils.DEFAULT_CHARACTER_ENCODING = "ISO-8859-1"
                .addFilter((request, response, chain) -> {
                    request.setCharacterEncoding(UTF_8);
                    response.setCharacterEncoding(UTF_8);
                    chain.doFilter(request, response);
                }, URL_PATTERN_ALL)
                .build();
    }
}
