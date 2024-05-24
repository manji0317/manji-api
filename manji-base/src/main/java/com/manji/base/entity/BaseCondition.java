package com.manji.base.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 基础查询类，用来接收查询API的公用查询参数
 *
 * @author Bqd
 * @since 2024/5/23 21:06
 */
@Getter
@Setter
public class BaseCondition {
    /**
     * 第几页
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer itemPrePage;
    /**
     * 排序字段集合
     */
    private List<SortItem> sortBy;
}
