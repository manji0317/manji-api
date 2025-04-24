package com.manji.bar.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.manji.base.basic.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 商品规格表
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bar_sku")
public class BarSku extends BaseEntity<BarSku> {

    /**
     * 规格分类ID
     */
    private Integer categoryId;

    /**
     * 规格名字
     */
    private String skuName;

    /**
     * 规格描述
     */
    private String skuDescription;

    /**
     * 规格库存
     */
    private Integer skuStore;

    /**
     * 规格金额
     */
    private BigDecimal skuPrice;
    
}
