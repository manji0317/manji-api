import { createI18n } from 'vue-i18n';

import zh_CN from '@/locales/zh_CN';
import en_US from '@/locales/en_US';

export default createI18n({
  legacy: false,
  locale: 'zh_CN',
  fallbackLocale: 'zh_CN',
  messages: {
    zh_CN,
    en_US,
  },
});
