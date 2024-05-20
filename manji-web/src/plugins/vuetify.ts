/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css';
import 'vuetify/styles';

// Composables
import { createVuetify } from 'vuetify';
import { VSnackbarQueue } from 'vuetify/labs/VSnackbarQueue';

export default createVuetify({
  theme: {
    defaultTheme: 'light',
  },
  components: {
    VSnackbarQueue,
  },
});
