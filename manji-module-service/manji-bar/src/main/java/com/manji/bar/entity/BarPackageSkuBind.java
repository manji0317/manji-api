package com.manji.bar.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 套餐规格绑定表
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bar_package_sku_bind")
public class BarPackageSkuBind extends Model<BarPackageSkuBind> {

    /**
     * 套餐ID
     */
    private Integer packageId;

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 规格属性ID
     */
    private Integer skuCategoryId;

    /**
     * 规格ID
     */
    private Integer skuId;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;
}
