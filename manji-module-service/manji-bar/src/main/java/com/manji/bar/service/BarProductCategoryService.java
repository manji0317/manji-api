package com.manji.bar.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.bar.condition.ProductCategoryCondition;
import com.manji.bar.entity.BarProductCategory;
import com.manji.bar.mapper.BarProductCategoryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-09
 */
@Service
public class BarProductCategoryService extends ServiceImpl<BarProductCategoryMapper, BarProductCategory> {

    /**
     * 查询商品分类信息
     *
     * @return 商品分类列表
     */
    public Page<BarProductCategory> queryProductCategoryPageList(ProductCategoryCondition condition) {

        // 查询商品分类信息
        return this.lambdaQuery()
                .like(StringUtils.isNotBlank(condition.getCategoryName()), BarProductCategory::getCategoryName, condition.getCategoryName())
                .orderByAsc(BarProductCategory::getSort)
                .select(
                        BarProductCategory::getId,
                        BarProductCategory::getSort,
                        BarProductCategory::getCategoryName,
                        BarProductCategory::getWebIcon,
                        BarProductCategory::getMiniappIcon
                ).page(new Page<>(condition.getPage(), condition.getItemPrePage()));
    }

    /**
     * 查询所有商品分类信息（不分页）
     *
     * @return 商品分类列表
     */
    public List<BarProductCategory> queryProductCategoryAllList() {
        return this.lambdaQuery()
                .orderByAsc(BarProductCategory::getSort)
                .select(
                        BarProductCategory::getId,
                        BarProductCategory::getSort,
                        BarProductCategory::getCategoryName,
                        BarProductCategory::getWebIcon,
                        BarProductCategory::getMiniappIcon
                ).list();
    }

    /**
     * 根据ID查询分类信息
     */
    public BarProductCategory queryProductCategoryById(Integer id) {
        return this.getById(id);
    }

    /**
     * 保存商品分类（新增或更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveProductCategory(BarProductCategory barProductCategory) {
        this.saveOrUpdate(barProductCategory);
    }

    /**
     * 获取最大排序值 +10
     */
    public Integer getMaxSort() {
        return this.lambdaQuery()
                .orderByDesc(BarProductCategory::getSort)
                .last("limit 1")
                .oneOpt()
                .map(BarProductCategory::getSort)
                .orElse(0);
    }

    /**
     * 删除商品分类
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductCategory(Integer id) {
        this.removeById(id);
    }
}
