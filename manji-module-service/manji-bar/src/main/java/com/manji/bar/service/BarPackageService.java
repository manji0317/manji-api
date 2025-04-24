package com.manji.bar.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.bar.condition.PackageCondition;
import com.manji.bar.entity.*;
import com.manji.bar.mapper.BarPackageMapper;
import com.manji.base.error.BizException;
import com.manji.file.utils.FileUtil;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * 商品套餐 服务实现类
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-10
 */
@Service
@Slf4j
public class BarPackageService extends ServiceImpl<BarPackageMapper, BarPackage> {

    @Resource
    private BarPackageProductService barPackageProductService;

    @Resource
    private BarPackageSkuBindService barPackageSkuBindService;

    public List<BarPackage> queryPackageList(PackageCondition condition) {
        return this.baseMapper.queryPackageList(condition);
    }

    /**
     * 获取套餐详细信息，包括基本信息、关联商品和规格绑定
     *
     * @param packageId 套餐ID
     * @return 套餐详细信息对象
     */
    public BarPackage getPackageDetails(Integer packageId) {
        // 参数校验
        if (packageId == null) {
            log.error("获取套餐详情失败：套餐ID不能为空");
            throw new BizException("套餐ID不能为空");
        }
        try {
            // 获取套餐基本信息
            List<BarPackage> barPackages = this.baseMapper.queryPackageList(PackageCondition.builder().packageId(packageId).build());
            if (CollectionUtils.isEmpty(barPackages)) {
                return null;
            }
            return barPackages.get(0);
        } catch (Exception e) {
            log.error("获取套餐详情异常: id={}", packageId, e);
            throw new BizException("获取套餐详情失败: " + e.getMessage());
        }
    }

    /**
     * 添加或编辑套餐
     *
     * @param barPackage 套餐信息，包含基本信息、商品列表和规格绑定信息
     * @return 操作是否成功
     * @throws BizException 业务异常
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrEditPackage(@NonNull BarPackage barPackage) {
        String packageName = barPackage.getPackageName();
        log.info("开始处理套餐: {}", packageName);

        // 校验套餐商品
        List<BarPackageProduct> packageProducts = barPackage.getPackageProducts();
        if (CollectionUtils.isEmpty(packageProducts)) {
            log.error("套餐[{}]缺少商品信息", packageName);
            throw new BizException("套餐必须包含至少一个商品");
        }

        // 获取套餐ID，用于判断是新增还是修改
        Integer packageId = barPackage.getId();
        boolean isNewPackage = packageId == null;

        try {
            // 处理套餐图片上传
            processPackageImage(barPackage);

            // 处理套餐基本信息
            boolean saveResult;
            if (isNewPackage) {
                // 新增套餐
                log.info("新增套餐: {}", packageName);
                saveResult = this.save(barPackage);
                if (!saveResult || barPackage.getId() == null) {
                    log.error("保存套餐基本信息失败: {}", packageName);
                    throw new BizException("保存套餐基本信息失败");
                }
                packageId = barPackage.getId();
            } else {
                // 修改套餐
                log.info("修改套餐: id={}, name={}", packageId, packageName);
                // 查询现有套餐，验证存在性
                BarPackage existingPackage = this.getById(packageId);
                if (existingPackage == null) {
                    log.error("套餐不存在，无法修改: id={}", packageId);
                    throw new BizException("套餐不存在，无法修改");
                }

                // 保留原图片（如果没有上传新图片）
                if (barPackage.getPackageImageUrl() == null) {
                    barPackage.setPackageImageUrl(existingPackage.getPackageImageUrl());
                }

                saveResult = this.updateById(barPackage);
                if (!saveResult) {
                    log.warn("套餐已被他人修改，更新失败: id={}", packageId);
                    throw new BizException("套餐已被他人修改，请刷新后重试");
                }

                // 删除旧的商品关联和规格绑定
                boolean removedProducts = barPackageProductService.lambdaUpdate()
                        .eq(BarPackageProduct::getPackageId, packageId)
                        .remove();
                if (!removedProducts) {
                    log.warn("删除套餐旧的商品关联可能不完全: packageId={}", packageId);
                }

                log.info("删除套餐旧的规格绑定关系: packageId={}", packageId);
                barPackageSkuBindService.deleteByPackageId(packageId);
            }

            // 保存套餐商品关联关系
            savePackageProducts(packageId, packageProducts);

            // 保存套餐规格绑定关系
            savePackageSkuBinds(barPackage);

            log.info("套餐处理完成: {}, 结果: 成功", packageName);
            return true;
        } catch (BizException e) {
            throw e; // 业务异常直接抛出
        } catch (Exception e) {
            log.error("处理套餐时发生错误: {}", packageName, e);
            throw new BizException("套餐操作失败: " + e.getMessage());
        }
    }

    /**
     * 处理套餐图片上传
     *
     * @param barPackage 套餐信息
     * @throws BizException 图片上传失败时抛出
     */
    private void processPackageImage(BarPackage barPackage) {
        MultipartFile packageImage = barPackage.getPackageImage();
        if (packageImage != null && !packageImage.isEmpty()) {
            try {
                log.info("开始上传套餐图片: {}", packageImage.getOriginalFilename());
                String imageUrl = FileUtil.uploadFile(packageImage, "bar", "package");
                barPackage.setPackageImageUrl(imageUrl);
                log.info("套餐图片上传成功: {}", imageUrl);
            } catch (Exception e) {
                log.error("上传套餐图片失败", e);
                throw new BizException("图片上传失败: " + e.getMessage());
            }
        }
    }

    /**
     * 保存套餐商品关联关系
     *
     * @param packageId       套餐ID
     * @param packageProducts 套餐商品列表
     * @throws BizException 保存失败时抛出
     */
    private void savePackageProducts(Integer packageId, List<BarPackageProduct> packageProducts) {
        if (CollectionUtils.isEmpty(packageProducts)) {
            log.warn("没有套餐商品关联需要保存: packageId={}", packageId);
            return;
        }

        // 批量设置套餐ID
        packageProducts.forEach(product -> {
            product.setId(null);
            product.setPackageId(packageId);
        });

        log.info("批量保存套餐商品关联，数量: {}", packageProducts.size());
        boolean saved = barPackageProductService.saveBatch(packageProducts);
        if (!saved) {
            log.error("保存套餐商品关联失败: packageId={}", packageId);
            throw new BizException("保存套餐商品关联失败");
        }
    }

    /**
     * 保存套餐规格绑定关系
     *
     * @param barPackage 套餐信息
     */
    private void savePackageSkuBinds(BarPackage barPackage) {
        Integer packageId = barPackage.getId();
        if (packageId == null) {
            log.error("套餐ID不能为空");
            throw new BizException("套餐ID不能为空");
        }

        List<BarPackageProduct> packageProducts = barPackage.getPackageProducts();
        if (packageProducts.isEmpty()) {
            log.info("没有套餐商品需要处理，跳过规格绑定: packageId={}", packageId);
            return;
        }

        // 收集所有需要创建的绑定关系，用于批量保存
        List<BarPackageSkuBind> skuBinds = new ArrayList<>();

        for (BarPackageProduct barPackageProduct : packageProducts) {
            Integer productId = barPackageProduct.getProductId();
            BarProduct barProduct = barPackageProduct.getProduct();

            if(barProduct == null) {
                continue;
            }

            List<BarSkuCategory> skuCategories = barProduct.getSkuCategories();

            // 处理规格分类和规格
            if (skuCategories != null && !skuCategories.isEmpty()) {
                for (BarSkuCategory category : skuCategories) {
                    // 获取选中的规格
                    if (category.getPackageBindSku() != null && category.getPackageBindSku().getId() != null) {
                        Integer categoryId = category.getId();
                        Integer skuId = category.getPackageBindSku().getId();

                        // 创建绑定对象并添加到集合
                        BarPackageSkuBind bind = new BarPackageSkuBind()
                                .setPackageId(packageId)
                                .setProductId(productId)
                                .setSkuCategoryId(categoryId)
                                .setSkuId(skuId);

                        skuBinds.add(bind);
                    }
                }
            }
        }

        // 使用批量保存提高性能
        if (!skuBinds.isEmpty()) {
            log.info("批量保存套餐规格绑定关系，数量: {}", skuBinds.size());
            boolean saved = barPackageSkuBindService.saveBatch(skuBinds);
            if (!saved) {
                log.error("保存套餐规格绑定失败: packageId={}, bindCount={}", packageId, skuBinds.size());
                throw new BizException("保存套餐规格绑定失败");
            }
        } else {
            log.info("没有套餐规格绑定关系需要保存: packageId={}", packageId);
        }
    }

    /**
     * 更新套餐状态
     *
     * @param packageId 套餐ID
     * @param status    状态值（0-下架；1-上架）
     * @return 更新是否成功
     */
    public boolean updatePackageStatus(int packageId, int status) {
        return this.lambdaUpdate()
                .set(BarPackage::getStatus, status)
                .eq(BarPackage::getId, packageId).update();
    }

}
