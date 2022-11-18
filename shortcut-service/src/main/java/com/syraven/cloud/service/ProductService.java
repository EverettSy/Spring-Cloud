package com.syraven.cloud.service;

import com.syraven.cloud.domain.Product;

/**
 * @author syrobin
 * @version v1.0
 * @description: 产品服务
 * @date 2022-11-10 16:33
 */
public interface ProductService {

    Product create(Product product);

    Product update(Product product);

    Product get(String productId);
}
