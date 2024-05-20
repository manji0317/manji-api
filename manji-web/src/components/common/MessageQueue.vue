<script setup lang="ts">
  import { useSystemStore } from '@/store/systemStore';
  const systemStore = useSystemStore();
</script>

<template>
  <v-snackbar-queue v-model="systemStore.messageQueue" />
</template>

<style scoped></style>
