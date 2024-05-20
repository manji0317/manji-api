/**
 * Vuetify js 的全局配置：
 * 当前开发版本3.6.6是最新版本，目前最新版本不能全局在任何地方调用弹窗等；
 * 此插件主要这个问题，在创建Vuetify组件时，需要传入appContext，否则无法使用
 * 所以在初始化App是将appContext传入到全局，方便随时使用。
 */
import { type App, h, render, VNode } from 'vue';
import MessageQueue from '@/components/common/MessageQueue.vue';
import { useSystemStore } from '@/store/systemStore';

let messageQueueVNode: VNode;
let messageQueueContainer: HTMLElement;

const createVuetifyGlobal = {
  install(app: App) {
    // 组件注册时，创建一个全局的消息队列，用来展示系统中的消息弹窗。
    // MessageQueue组件为自己定义，封装的是v-snackbar-queue绑定的useSystemStore仓库的消息队列
    // 当useSystemStore中messageQueue有变化时，会重新渲染MessageQueue组件
    messageQueueContainer = document.createElement('div');
    messageQueueVNode = h(MessageQueue);
    messageQueueVNode.appContext = app._context;
    render(messageQueueVNode, messageQueueContainer);
  },
};

interface MessageProps {
  text: string;
  timeout?: number;
}

export class Message {
  private static updateAndRenderMessageQueue(props: Record<string, any>) {
    // 向仓库中添加消息、v-snackbar-queue会自动渲染
    const systemStore = useSystemStore();
    systemStore.messageQueue.push(props);
  }

  // 更宽泛的定义，自由度更高。
  static show(customerProp: MessageProps) {
    this.updateAndRenderMessageQueue(customerProp);
  }

  // 成功
  static success(text: string) {
    this.updateAndRenderMessageQueue({ text, color: 'success', location: 'top' });
  }

  // 失败
  static error(text: string) {
    this.updateAndRenderMessageQueue({ text, color: 'error', location: 'top' });
  }

  // 提醒
  static info(text: string) {
    this.updateAndRenderMessageQueue({ text, color: 'info', location: 'top' });
  }
}

export default createVuetifyGlobal;
