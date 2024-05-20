<script setup lang="ts">
  import { ref, useAttrs } from 'vue';

  const { id } = useAttrs();
  const dialogVisible = ref(true);

  const handleClose = () => {
    alert(id);
  };
</script>

<template>
  <v-dialog v-model="dialogVisible">
    {{$attrs}}
    <v-card>
      <v-card-title>
        <span class="ml-2">提示</span>
      </v-card-title>
    </v-card>
  </v-dialog>
</template>

<style scoped></style>
