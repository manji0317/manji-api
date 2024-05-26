package com.manji.base.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseEntity<T extends Model<?>> extends Model<T> {
    /**
     * 数据创建时间
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 数据创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 数据创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 数据修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 数据修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 删除标识：1-已删除 0-未删除
     */
    @TableLogic
    private Boolean delFlag;

}
