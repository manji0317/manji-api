package com.manji.bar.controller;

import com.manji.bar.condition.ProductCondition;
import com.manji.bar.entity.BarProduct;
import com.manji.bar.entity.BarSkuCategory;
import com.manji.bar.service.BarProductService;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 酒吧商品详情 前端控制器
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-07
 */
@RestController
@RequestMapping("/api/v1/product")
public class BarProductController {

    @Resource
    private BarProductService service;

    /**
     * 用于接收商品状态更新请求的包装类
     */
    @Data
    public static class ProductStatusRequest {
        private List<Integer> productIds;
        private Integer status;
    }

    /**
     * 查询商品列表信息
     *
     * @param condition 查询条件
     * @return 商品列表信息
     */
    @GetMapping("/list")
    public ResponseEntity<?> queryProductList(ProductCondition condition) {
        List<BarProduct> barProductPage = service.queryProductList(condition);
        return ResponseEntity.ok(barProductPage);
    }

    /**
     * 添加/修改 商品信息
     *
     * @param product 商品信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public ResponseEntity<?> addOrUpdateProductInfo(@ModelAttribute BarProduct product) {
        try {
            boolean bool = service.addOrUpdateProductInfo(product);
            return ResponseEntity.ok(bool);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("处理失败: " + e.getMessage());
        }
    }

    /**
     * 上下架商品信息
     *
     * @param request 包含商品ID集合和状态的请求对象
     * @return 更新结果
     */
    @PatchMapping("/status")
    public ResponseEntity<?> updateProductStatus(@RequestBody ProductStatusRequest request) {
        return ResponseEntity.ok(service.updateProductStatus(request.getProductIds(), request.getStatus()));
    }

    /**
     * 获取商品详细信息
     *
     * @param productId 商品ID
     * @return 商品详细信息
     */
    @GetMapping("/{productId}/details")
    public ResponseEntity<?> getProductDetails(@PathVariable Integer productId) {
        BarProduct barProduct = service.getProductDetails(productId);
        return ResponseEntity.ok(barProduct);
    }

    /**
     * 根据商品ID查询商品规格
     *
     * @param productId 商品ID
     * @return 商品规格
     */
    @GetMapping("/{productId}/sku")
    public ResponseEntity<?> getProductSku(@PathVariable Integer productId) {
        List<BarSkuCategory> skuList = service.getProductSku(productId);
        return ResponseEntity.ok(skuList);
    }

}
