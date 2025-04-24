package com.manji.bar.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.bar.condition.ProductCondition;
import com.manji.bar.entity.BarProduct;
import com.manji.bar.entity.BarSku;
import com.manji.bar.entity.BarSkuCategory;
import com.manji.bar.mapper.BarProductMapper;
import com.manji.base.error.BizException;
import com.manji.file.utils.FileUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 酒吧商品详情 服务实现类
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-07
 */
@Service
@Slf4j
public class BarProductService extends ServiceImpl<BarProductMapper, BarProduct> {

    @Resource
    private BarSkuCategoryService skuCategoryService;

    @Resource
    private BarSkuService skuService;

    /**
     * 查询商品列表信息
     *
     * @param condition 查询条件
     * @return 商品列表信息
     */
    public List<BarProduct> queryProductList(ProductCondition condition) {
        return this.baseMapper.selectProductListWithSku(condition);
    }

    /**
     * 上下架商品信息
     *
     * @param productIds 商品ID集合
     * @param status     状态（例如：0-下架，1-上架）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductStatus(List<Integer> productIds, int status) {
        return this.lambdaUpdate()
                .set(BarProduct::getStatus, status)
                .in(BarProduct::getId, productIds).update();
    }

    /**
     * 获取商品详细信息
     *
     * @param productId 商品ID
     * @return 商品详细信息
     */
    public BarProduct getProductDetails(Integer productId) {
        if (productId == null) {
            throw new BizException("商品ID不能为空");
        }
        List<BarProduct> barProducts = this.baseMapper.selectProductListWithSku(ProductCondition.builder().productId(productId).build());
        if (barProducts.isEmpty()) {
            throw new BizException("商品ID不存在");
        }
        return barProducts.get(0);
    }

    /**
     * 添加/修改 商品信息
     *
     * @param product 商品信息
     * @return 添加结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrUpdateProductInfo(BarProduct product) {
        // 参数校验
        if (product == null) {
            throw new BizException("商品信息不能为空");
        }

        // 初始化变量
        MultipartFile imageFile = product.getImageFile();
        List<BarSkuCategory> skuCategories = product.getSkuCategories();
        boolean hasSkuCategories = skuCategories != null && !skuCategories.isEmpty();

        try {
            if (product.getId() == null) {
                // 新增商品
                // 处理商品图片
                if (imageFile != null && !imageFile.isEmpty()) {
                    // 使用优化后的FileUtil方法，直接传入MultipartFile
                    String filePath = FileUtil.uploadFile(imageFile, "product", String.valueOf(System.currentTimeMillis()));
                    product.setImageUrl(filePath);
                } else {
                    throw new BizException("商品图片不能为空");
                }

                // 保存商品基本信息
                if (!this.save(product)) {
                    throw new BizException("保存商品信息失败");
                }
            } else {
                // 修改商品
                // 查询数据库中的商品，确认存在
                BarProduct dbProduct = this.getById(product.getId());
                if (dbProduct == null) {
                    throw new BizException("商品不存在，无法修改");
                }

                // 处理商品图片
                if (imageFile != null && !imageFile.isEmpty()) {
                    // 使用优化后的FileUtil方法，直接传入MultipartFile，并使用商品ID作为路径的一部分，避免冲突
                    String filePath = FileUtil.uploadFile(imageFile, "product", product.getId().toString());
                    product.setImageUrl(filePath);
                } else {
                    // 保留原有图片
                    product.setImageUrl(dbProduct.getImageUrl());
                }

                // 更新商品基本信息
                if (!this.updateById(product)) {
                    throw new BizException("更新商品信息失败");
                }

                // 先删除原有规格信息
                deleteExistingSkuData(product.getId());
            }
            // 更新规格分类和规格信息
            if (hasSkuCategories) {
                saveSkuCategoriesAndSkus(product.getId(), skuCategories);
            }
            return true;
        } catch (BizException e) {
            log.error("商品保存/更新业务异常: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("商品保存/更新系统异常: {}", e.getMessage(), e);
            throw new BizException("商品保存/更新失败: " + e.getMessage());
        }
    }

    /**
     * 保存规格分类和规格信息
     *
     * @param productId     商品ID
     * @param skuCategories 规格分类列表
     */
    private void saveSkuCategoriesAndSkus(Integer productId, List<BarSkuCategory> skuCategories) {
        if (skuCategories == null || skuCategories.isEmpty() || productId == null) {
            return;
        }

        // 计算总库存
        int totalStock = 0;

        for (BarSkuCategory category : skuCategories) {
            // 确保ID为null，以便自动生成
            category.setId(null);
            // 设置商品ID
            category.setProductId(productId);

            // 保存规格分类
            if (!skuCategoryService.save(category)) {
                throw new BizException("保存规格分类失败");
            }

            // 获取规格列表
            List<BarSku> skuList = category.getSkuList();
            if (skuList != null && !skuList.isEmpty()) {
                for (BarSku sku : skuList) {
                    // 确保ID为null，以便自动生成
                    sku.setId(null);
                    // 设置分类ID
                    sku.setCategoryId(category.getId());

                    // 保存规格
                    if (!skuService.save(sku)) {
                        throw new BizException("保存规格信息失败");
                    }

                    // 累加库存
                    if (sku.getSkuStore() != null) {
                        totalStock += sku.getSkuStore();
                    }
                }
            }
        }

        // 更新商品总库存
        this.lambdaUpdate()
                .set(BarProduct::getBaseStock, totalStock)
                .eq(BarProduct::getId, productId)
                .update();
    }

    /**
     * 删除商品已有的规格分类和规格信息
     *
     * @param productId 商品ID
     */
    private void deleteExistingSkuData(Integer productId) {
        if (productId == null) {
            return;
        }

        // 查询所有规格分类
        List<BarSkuCategory> categories = skuCategoryService.lambdaQuery()
                .eq(BarSkuCategory::getProductId, productId)
                .list();

        // 删除所有规格
        for (BarSkuCategory category : categories) {
            skuService.lambdaUpdate()
                    .eq(BarSku::getCategoryId, category.getId())
                    .remove();
        }

        // 删除所有规格分类
        skuCategoryService.lambdaUpdate()
                .eq(BarSkuCategory::getProductId, productId)
                .remove();
    }

    /**
     * 根据商品ID查询商品规格
     *
     * @param productId 商品ID
     * @return 商品规格
     */
    public List<BarSkuCategory> getProductSku(Integer productId) {
        return this.baseMapper.selectProductSku(productId);
    }

}
