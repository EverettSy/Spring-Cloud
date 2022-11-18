package com.syraven.cloud.controller;

import com.syraven.cloud.domain.Product;
import com.syraven.cloud.record.R;
import com.syraven.cloud.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-11-14 17:14
 */
@Api(tags = "Product")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "创建产品")
    @PostMapping("/create")
    public R<Product> create(@RequestBody Product product){
        return R.ok(productService.create(product));
    }


    @ApiOperation(value = "更新产品")
    @PostMapping("/update")
    public R<Product> update(@RequestBody Product product){
        return R.ok(productService.update(product));
    }

    @ApiOperation(value = "获取产品")
    @GetMapping("/get")
    public R<Product> get(@RequestParam String productId){
        return R.ok(productService.get(productId));
    }
}
