package com.syraven.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syraven.cloud.domain.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-11-10 16:30
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
