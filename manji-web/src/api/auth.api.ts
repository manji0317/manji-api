import httpApi from '@/plugins/axios';
import { getRefreshToken } from '@/utils/TokenUtil';

const API = {
  captcha: '/auth/captcha',
  login: '/auth/login',
  logout: '/auth/logout',
  refreshToken: '/auth/refreshToken',
};

/**
 * 获取验证码
 */
export const getCaptcha = () => {
  return httpApi.get(`${API.captcha}`);
};

/**
 * 登录请求
 */
export const login = (data: Login) => {
  return httpApi.post<Token>(API.login, { ...data });
};

/**
 * 推出登录
 */
export const logout = () => {
  return httpApi.get(`${API.logout}`);
};

/**
 * 刷新Token令牌
 */
export const refreshToken = () => {
  return httpApi.post<Token>(`${API.refreshToken}`, {
    refreshToken: getRefreshToken(),
  });
};
