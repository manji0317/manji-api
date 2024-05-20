import axios from 'axios';
import { Message } from '@/plugins/vuetify-global';
import i18n from './i18n';
import { getAccessToken, setAccessToken } from '@/utils/TokenUtil';
import { refreshToken } from '@/api/auth.api';
import nProgress from 'nprogress';

const { t } = i18n.global;
// 是否正在刷新Token标识
let isRefreshToken: boolean = false;
// 重试队列，每一项是一个带请求的API
let retryQueue: Array<(token: string) => void> = [];

// axios基础配置
const httpApi = axios.create({
  baseURL: import.meta.env.VITE_SERVICE_API,
  timeout: 10000,
});

// 添加请求拦截器
httpApi.interceptors.request.use(
  (config) => {
    nProgress.start();
    // 请求添加token
    config.headers.Authorization = `Bearer ${getAccessToken()}`;
    return config;
  },
  (error) => {
    // 请求错误处理
    return Promise.reject(error);
  }
);

// 添加相应拦截器
httpApi.interceptors.response.use(
  (response) => {
    nProgress.done();
    return response;
  },
  async (error) => {
    nProgress.done();
    const { response, config } = error;
    // 如果不是401或403，则直接返回错误给业务代码处理
    if (!response && ![401, 403].includes(response.status)) {
      return Promise.reject(error);
    }

    // 判断请求是否为登录API
    if (config.url === '/auth/login') {
      Message.error(t('login.usernameOrPasswordError'));
      return Promise.reject(error);
    }

    // 如果刷新Token的接口返回的401，需要重新登录
    if (config.url === '/auth/refreshToken') {
      isRefreshToken = false;
      Message.error(t('login.tokenExpired'));
      return Promise.reject(error);
    }

    if (!isRefreshToken) {
      isRefreshToken = true;
      refreshToken()
        .then((res) => {
          setAccessToken(res.data.accessToken);
          config.headers.Authorization = `Bearer ${res.data.accessToken}`;
          retryQueue.forEach((fn) => fn(res.data.accessToken));
          retryQueue = [];
          return httpApi(config);
        })
        .finally(() => (isRefreshToken = false));
    } else {
      // 如果正在刷新Token，将请求重新加入队列
      return new Promise((resolve) => {
        retryQueue.push((token: string) => {
          config.headers.Authorization = `Bearer ${token}`;
          resolve(httpApi(config));
        });
      });
    }
  }
);

export default httpApi;
