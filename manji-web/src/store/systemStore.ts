import { defineStore } from 'pinia';
import { type VSnackbar } from 'vuetify/components';

type SnackbarMessage = Omit<
  VSnackbar['$props'],
  | '$children'
  | 'modelValue'
  | 'onUpdate:modelValue'
  | 'activator'
  | 'activatorProps'
  | 'closeDelay'
  | 'openDelay'
  | 'openOnClick'
  | 'openOnFocus'
  | 'openOnHover'
>;

interface SystemStore {
  messageQueue: SnackbarMessage[];
}

export const useSystemStore = defineStore('system', {
  state: (): SystemStore => ({
    messageQueue: [],
  }),
});
