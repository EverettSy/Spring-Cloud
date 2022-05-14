package com.syraven.cloud;

import com.syraven.cloud.domain.Car;
import com.syraven.cloud.dto.CarDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-05-09 22:10
 */
class MapstructSpringExtensionsApplicationTests extends SpringTest{

    @Autowired
    private ConversionService conversionService;

    @Test
    void contextLoads() {

        Car source = new Car();
        source.setName("VW");
        source.setType("SUV");
        source.setSeatConfiguration(7);
        source.setSeatCount(5);
        // CarMapper carMapper = CarMapper.CAR_MAPPER;
        CarDto carDto = conversionService.convert(source,CarDto.class);
        Assertions.assertNotNull(carDto);
        Assertions.assertEquals(7,carDto.getSeats());
        Assertions.assertEquals("VW",carDto.getName());
        Assertions.assertEquals("SUV",carDto.getType());
        Assertions.assertEquals(5,carDto.getSeatCounts());

    }
}
