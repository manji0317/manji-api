/**
 * 路由配置
 */
import { createRouter, createWebHistory, type Router } from 'vue-router';
import { RouterWhite } from '@/router/router-white';
import { setBeforeEachGuard, setAfterEachGuard } from '@/router/router-guard';


const router: Router = createRouter({
  routes: [...RouterWhite],
  history: createWebHistory(),
});

setBeforeEachGuard(router);

setAfterEachGuard(router);

export default router;
