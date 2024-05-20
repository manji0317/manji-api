import httpApi from '@/plugins/axios';

const API = {
  GET_USER_BASIC_INFO: '/user/get/',
};

/**
 * 获取用户基本信息
 */
export const getUserBasicUser = (id: number) => {
  return httpApi.get(`${API.GET_USER_BASIC_INFO}${id}`);
};
