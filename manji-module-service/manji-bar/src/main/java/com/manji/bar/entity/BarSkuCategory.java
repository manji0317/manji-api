package com.manji.bar.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manji.base.basic.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 商品规格分类
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bar_sku_category")
public class BarSkuCategory extends BaseEntity<BarSkuCategory> {

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 规格分类名称
     */
    private String categoryName;

    /**
     * 该分类下的规格列表 (全部)
     */
    @TableField(exist = false)
    private List<BarSku> skuList;

    /**
     * 与套餐绑定的规格信息
     */
    @TableField(exist = false)
    private BarSku packageBindSku;
}
