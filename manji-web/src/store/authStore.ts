import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: (): Auth => ({
    isAuth: false,
    userInfo: null,
  }),
  getters: {},
  actions: {
    // 退出系统
    handleLogout() {},
  },
});
