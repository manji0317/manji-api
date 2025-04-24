package com.manji.bar.condition;

import com.manji.base.condition.BaseCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PackageCondition extends BaseCondition {
    private Integer packageId;
    private String packageName;
    private Integer status;
}
