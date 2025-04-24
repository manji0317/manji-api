package com.manji.bar.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manji.bar.entity.BarPackageSkuBind;
import com.manji.bar.mapper.BarPackageSkuBindMapper;
import com.manji.base.error.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 套餐规格绑定表 服务实现类
 * </p>
 *
 * @author BaiQingDong
 * @since 2025-04-18
 */
@Service
@Slf4j
public class BarPackageSkuBindService extends ServiceImpl<BarPackageSkuBindMapper, BarPackageSkuBind> {

    /**
     * 根据套餐ID删除所有规格绑定关系
     *
     * @param packageId 套餐ID
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteByPackageId(Integer packageId) {
        if (packageId == null) {
            throw new BizException("套餐ID不能为空");
        }
        
        try {
            log.info("开始删除套餐规格绑定关系: packageId={}", packageId);
            // 使用MyBatis-Plus提供的删除方法，按套餐ID删除所有绑定关系
            boolean removed = this.lambdaUpdate()
                    .eq(BarPackageSkuBind::getPackageId, packageId)
                    .remove();
            
            if (!removed) {
                log.warn("未找到套餐规格绑定关系或删除失败: packageId={}", packageId);
            } else {
                log.info("套餐规格绑定关系删除成功: packageId={}", packageId);
            }
        } catch (Exception e) {
            log.error("删除套餐规格绑定关系失败, packageId: {}", packageId, e);
            throw new BizException("删除套餐规格绑定关系失败: " + e.getMessage());
        }
    }

}
