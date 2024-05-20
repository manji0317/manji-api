<script setup lang="ts">
  import { ref } from 'vue';

  const visible = ref(true);
  defineProps({
    message: { required: true, type: String },
    variant: { type: String, default: 'text' },
  });
</script>

<template>
  <v-snackbar v-model="visible" :text="message">
    <template #actions>
      <v-btn @click="visible = false"> Close</v-btn>
    </template>
  </v-snackbar>
</template>

<style scoped></style>
