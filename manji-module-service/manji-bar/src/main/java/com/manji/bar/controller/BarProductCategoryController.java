package com.manji.bar.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manji.bar.condition.ProductCategoryCondition;
import com.manji.bar.entity.BarProductCategory;
import com.manji.bar.service.BarProductCategoryService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/api/v1/category")
public class BarProductCategoryController {

    @Resource
    private BarProductCategoryService service;

    /**
     * 查询商品分类信息 （分页）
     * @return 商品分类列表
     */
    @GetMapping("/page-list")
    public ResponseEntity<?> queryProductCategoryPageList(ProductCategoryCondition condition) {
        Page<BarProductCategory> barProductCategories = service.queryProductCategoryPageList(condition);
        return ResponseEntity.ok(barProductCategories);
    }

    /**
     * 查询商品分类信息 (不分页)
     * @return 商品分类列表
     */
    @GetMapping("/all-list")
    public ResponseEntity<?> queryProductCategoryAllList() {
        List<BarProductCategory> barProductCategories = service.queryProductCategoryAllList();
        return ResponseEntity.ok(barProductCategories);
    }

    /**
     * 根据ID查询商品分类信息
     */
    @GetMapping("/getById")
    public ResponseEntity<?> queryProductCategoryById(@RequestParam Integer id) {
        return ResponseEntity.ok(service.queryProductCategoryById(id));
    }

    /**
     * 保存商品分类（新增或更新）
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveProductCategory(@RequestBody BarProductCategory barProductCategory) {
        service.saveProductCategory(barProductCategory);
        return ResponseEntity.ok(barProductCategory);
    }

    /**
     * 获取最大排序值
     */
    @GetMapping("/getMaxSort")
    public ResponseEntity<?> getMaxSort() {
        return ResponseEntity.ok(service.getMaxSort());
    }

    /**
     * 删除商品分类 
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable Integer id) {
        service.deleteProductCategory(id);
        return ResponseEntity.ok(id);
    }
}
