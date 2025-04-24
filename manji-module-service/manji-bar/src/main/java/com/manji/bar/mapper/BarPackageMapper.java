package com.manji.bar.mapper;

import com.manji.bar.condition.PackageCondition;
import com.manji.bar.entity.BarPackage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品套餐 Mapper 接口
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-10
 */
public interface BarPackageMapper extends BaseMapper<BarPackage> {

    List<BarPackage> queryPackageList(@Param("condition") PackageCondition condition);
}
