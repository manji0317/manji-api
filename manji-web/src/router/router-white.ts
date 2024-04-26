/**
 * 路由白名单配置文件，此处配置的路由，不需要通过鉴权。
 *
 * @author 白青东
 * @since 2024年4月26日 14点25分
 */

import { RouteRecordRaw } from 'vue-router';
import HelloWorld from '@/components/HelloWorld.vue';

export const RouterWhite: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: HelloWorld,
  },
];
