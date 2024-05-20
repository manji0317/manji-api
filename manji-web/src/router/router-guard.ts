/**
 * 路由守卫配置文件。
 *
 * @author 白青东
 * @since 2024年4月26日 14点26分
 */

import { type Router } from 'vue-router';

/**
 * 前置路由守卫
 *
 */
export const setBeforeEachGuard = (router: Router) => {
  router.beforeEach((to, from, next) => {
    next();
  });
};

/**
 * 后置路由守卫
 */
export const setAfterEachGuard = (router: Router) => {
  router.afterEach((to, from, failure) => {
    console.log('路由守卫', to, from, failure);
  });
};
