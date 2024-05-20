<script setup lang="ts">
  import { ref } from 'vue';
  import { useForm } from 'vee-validate';
  import { string, object } from 'yup';
  import { useI18n } from 'vue-i18n';
  import { useAuthStore } from '@/store/authStore';
  import { Message } from '@/plugins/vuetify-global';
  import { login } from '@/api/auth.api';
  import { getUserBasicUser } from '@/api/user.api';
  import { setAccessToken, setRefreshToken } from '@/utils/TokenUtil';

  const { t } = useI18n();
  let authStore = useAuthStore();

  // 控制密码显示隐藏
  const passwordVisible = ref(false);

  // 请求中标识
  const loading = ref(false);
  const { handleSubmit } = useForm<Login>({
    validationSchema: object({
      username: string().required(t('fieldError.required', [t('login.username')])),
      password: string().required(t('fieldError.required', [t('login.password')])),
    }),
    initialValues: {
      username: '',
      password: '',
    },
  });
  /**
   * 登录
   */
  const handleLogin = handleSubmit((values: Login) => {
    loading.value = true;
    login(values)
      .then((res) => {
        if (res.status === 200) {
          const { accessToken, refreshToken } = res.data;
          authStore.isAuth = true;
          setAccessToken(accessToken);
          setRefreshToken(refreshToken);
          Message.success('登录成功');
        } else {
          authStore.isAuth = false;
        }
      })
      .finally(() => (loading.value = false));
  });

  const handle = () => {
    getUserBasicUser(new Date().getMilliseconds());
  };
</script>

<template>
  <v-row no-gutters class="fill-height">
    <!--    <v-snackbar-queue v-model="messages" color="success" :timeout="5000"></v-snackbar-queue>-->
    <!-- 登录页左侧图片 -->
    <v-col cols="12" lg="8" md="7" class="d-lg-flex d-md-flex justify-center align-center">
      <img src="@/assets/login/login-bg.svg" alt="Login background load fail" class="hidden-sm hidden-xs" />
    </v-col>
    <!-- 登录页右侧表单 -->
    <v-col cols="12" lg="4" md="5">
      <v-card class="h-screen pa-5 d-flex flex-column" elevation="8">
          <img src="@/assets/logo.svg" alt="Logo load fail" class="flex-1-0" />
          <div class="flex-1-0">
            <m-text-field
              name="username"
              :label="$t('login.username')"
              :placeholder="$t('common.pleaseInput', [$t('login.username')])"
              prepend-inner-icon="mdi-account-circle"
            />

            <m-text-field
              name="password"
              :label="$t('login.password')"
              :placeholder="$t('common.pleaseInput', [$t('login.password')])"
              prepend-inner-icon="mdi-lock"
              :type="passwordVisible ? 'text' : 'password'"
              :append-inner-icon="passwordVisible ? 'mdi-eye' : 'mdi-eye-off'"
              @click:append-inner="passwordVisible = !passwordVisible"
            />

            <div class="d-flex justify-end mb-5">
              <v-btn variant="text" color="blue">{{ $t('login.forgotPassword') }}</v-btn>
            </div>
            <v-btn block color="primary" @click="handleLogin" :loading="loading">{{ $t('login.login') }}</v-btn>
            <v-card-text class="text-center">
              <v-btn variant="text" color="blue" append-icon="mdi-chevron-right" @click="handle">
                {{ $t('login.register') }}
              </v-btn>
            </v-card-text>
          </div>
      </v-card>
    </v-col>
  </v-row>
</template>

<style scoped></style>
