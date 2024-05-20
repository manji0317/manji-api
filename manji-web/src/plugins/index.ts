/**
 * plugins/index.ts
 *
 * Automatically included in `./src/main.ts`
 */

// Plugins
import pinia from './pinia'
import vuetify from './vuetify'
import i18n from './i18n'
import router from '../router'
import createVuetifyGlobal from "@/plugins/vuetify-global";
import NProgressConfig from './nprogress-config';

// Types
import type { App } from 'vue'

export function registerPlugins (app: App) {
  app
    .use(vuetify)
    .use(pinia)
    .use(i18n)
    .use(router)
    .use(createVuetifyGlobal)
    .use(NProgressConfig)
}
