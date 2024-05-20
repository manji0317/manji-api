<script lang="ts">
import {defineComponent, provide, ref} from 'vue';

  export default defineComponent({
    name: 'Message',
    setup(props, ctx) {
      const visible = ref(true);
      return {
        visible,
      };
    },
  });
</script>

<template>
  <v-snackbar v-model="visible" text="123">
    <template #actions>
      <v-btn @click="visible = false"> Close</v-btn>
    </template>
  </v-snackbar>
</template>

<style scoped></style>
