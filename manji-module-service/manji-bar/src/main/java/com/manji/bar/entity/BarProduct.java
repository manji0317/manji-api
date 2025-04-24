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
 * 酒吧商品详情
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bar_product")
public class BarProduct extends BaseEntity<BarProduct> {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 商品图片
     */
    private String imageUrl;

    /**
     * 是否有规格（0-无；1-有）
     */
    private Integer hasSku;

    /**
     * 商品价格（商品基本价格）
     */
    private BigDecimal basePrice;

    /**
     * 商品库存（当有规格时，为规格所有库存总和）
     */
    private Integer baseStock;

    /**
     * 商品状态（0-下架；1-上架）
     */
    private Integer status;

    /**
     * 商品规格分类列表
     */
    @TableField(exist = false)
    private List<BarSkuCategory> skuCategories;

    /**
     * 接收商品图片文件
     */
    @TableField(exist = false)
    private MultipartFile imageFile;
}
