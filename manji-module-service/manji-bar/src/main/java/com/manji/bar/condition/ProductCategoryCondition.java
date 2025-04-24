package com.manji.bar.condition;

import com.manji.base.condition.BaseCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCategoryCondition extends BaseCondition {

    private String categoryName;

}
