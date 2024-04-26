<template>
  <v-btn @click="handleChangeTheme">Click change</v-btn>
  1
  <v-icon icon="mdi-account" />
</template>

<script setup lang="ts">
import {useTheme} from 'vuetify'

const theme = useTheme()

const handleChangeTheme = () => {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
</script>
