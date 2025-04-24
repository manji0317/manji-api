package com.manji.bar.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manji.base.basic.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品和套餐关联表
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bar_package_product")
public class BarPackageProduct extends BaseEntity<BarPackageProduct> {

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 套餐ID
     */
    private Integer packageId;

    /**
     * 所需商品数量
     */
    private Integer quantity;

    /**
     * 关联产品信息
     */
    @TableField(exist = false)
    private BarProduct product;

}
