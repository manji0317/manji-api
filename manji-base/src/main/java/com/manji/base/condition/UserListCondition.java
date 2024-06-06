package com.manji.base.condition;

import lombok.Getter;
import lombok.Setter;

/**
 * 接收 用户列表查询条件
 *
 * @author Bqd
 * @since 2024/5/23 21:59
 */
@Getter
@Setter
public class UserListCondition extends BaseCondition {
    private String search;
}
