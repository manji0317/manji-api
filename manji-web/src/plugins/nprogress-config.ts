import nProgress from 'nprogress';

/**
 * 全局滚动条配置
 */
const NProgressConfig = {
  install() {
    nProgress.configure({
      easing: 'ease-in-out',
      speed: 300,
      trickleSpeed: 300,
    });
  },
};

export default NProgressConfig;
