package com.syraven.cloud.domain;

import com.syraven.cloud.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: Test
 * @Description:
 * @Author syrobin
 * @Date 2021-12-03 4:46 PM
 * @Version V1.0
 */
@Component
public class Test {

    @Autowired
    private List<ServiceInterface> serviceInterfaces;
}
