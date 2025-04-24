package com.manji.bar.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manji.base.basic.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品套餐
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bar_package")
public class BarPackage extends BaseEntity<BarPackage> {

    /**
     * 套餐名
     */
    private String packageName;

    /**
     * 套餐描述
     */
    private String packageDescription;

    /**
     * 套餐图片
     */
    private String packageImageUrl;

    /**
     * 套餐金额
     */
    private BigDecimal packagePrice;

    /**
     * 套餐状态（0-下架；1-上架）
     */
    private Integer status;

    /**
     * 套餐-产品关联实体
     */
    @TableField(exist = false)
    private List<BarPackageProduct> packageProducts;

    /**
     * 套餐-图片
     */
    @TableField(exist = false)
    private MultipartFile packageImage;
}
