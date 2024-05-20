<script setup lang="ts">
  import { useField } from 'vee-validate';

  const props = defineProps({
    name: { required: true, type: String },
  });

  const { value, errorMessage } = useField(() => props.name);
</script>

<template>
<!--
    此处没有对v-text-field组件做过多的封装
    只是结合了vee-validate的useField方法
    防止使用大量的defineField对字段进行解析
    如果不使用表单校验，完全可以直接使用v-text-field组件

    vee-validate官方参考文档：
    https://vee-validate.logaretm.com/v4/guide/composition-api/custom-inputs/
 -->
  <v-text-field v-bind="$attrs" v-model="value" :error-messages="errorMessage" />
</template>

<style scoped></style>
