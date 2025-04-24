package com.manji.bar.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manji.base.basic.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bar_product_category")
public class BarProductCategory extends BaseEntity<BarProductCategory> {

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 后端Icon图标
     */
    private String webIcon;

    /**
     * 小程序Icon图标
     */
    private String miniappIcon;

}
