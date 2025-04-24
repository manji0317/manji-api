package com.manji.bar.condition;

import com.manji.base.condition.BaseCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCondition extends BaseCondition {
    private String search;
    private Integer productId;
    private String productName;
    private Integer categoryId;
    private Integer status;
}
