package com.manji.bar.controller;

import com.manji.bar.condition.PackageCondition;
import com.manji.bar.entity.BarPackage;
import com.manji.bar.service.BarPackageService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品套餐 前端控制器
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-10
 */
@RestController
@RequestMapping("/api/v1/package")
public class BarPackageController {
    @Resource
    private BarPackageService service;

    /**
     * 查询套餐列表信息
     * @param condition 查询条件
     * @return 商品列表信息
     */
    @GetMapping("/list")
    public ResponseEntity<?> queryPackageList(PackageCondition condition) {
        List<BarPackage> barPackages = service.queryPackageList(condition);
        return ResponseEntity.ok(barPackages);
    }

    /**
     * 新增/修改套餐信息
     * @param barPackage 套餐信息
     */
    @PostMapping("/add")
    public ResponseEntity<?> addOrEditPackage(@ModelAttribute BarPackage barPackage) {
        boolean bool = service.addOrEditPackage(barPackage);
        return ResponseEntity.ok(bool);
    }

    /**
     * 上下架商品套餐信息
     * @param packageId 套餐ID
     * @param status 状态（例如：0-下架，1-上架）
     * @return 更新结果
     */
    @PatchMapping("/{packageId}/status")
    public ResponseEntity<?> updatePackageStatus(@PathVariable int packageId, @RequestParam int status) {
        boolean bool = service.updatePackageStatus(packageId, status);
        if (bool) {
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.status(500).body("更新失败");
        }
    }

    /**
     * 获取商品详细信息
     * @param packageId 商品ID
     * @return 商品详细信息
     */
    @GetMapping("/{packageId}/details")
    public ResponseEntity<?> getPackageDetails(@PathVariable Integer packageId) {
        BarPackage barPackage = service.getPackageDetails(packageId);
        return ResponseEntity.ok(barPackage);
    }
}
