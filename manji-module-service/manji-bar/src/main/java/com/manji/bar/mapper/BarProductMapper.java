package com.manji.bar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manji.bar.condition.ProductCondition;
import com.manji.bar.entity.BarProduct;
import com.manji.bar.entity.BarSkuCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 酒吧商品详情 Mapper 接口
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-07
 */
public interface BarProductMapper extends BaseMapper<BarProduct> {

    /**
     * 查询商品列表，包含规格分类和规格信息
     * @param condition 查询条件
     * @return 商品列表（包含规格分类和规格信息）
     */
    List<BarProduct> selectProductListWithSku(@Param("condition") ProductCondition condition);

    List<BarSkuCategory> selectProductSku(Integer productId);
}
