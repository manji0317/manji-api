package com.manji.base.condition;

import lombok.Getter;
import lombok.Setter;

/**
 * 针对Vuetify分页Options的排序字段
 *
 * @author Bqd
 * @since 2024/5/23 21:58
 */
@Getter
@Setter
public class SortItem {
    private String key;
    private String order;
}
