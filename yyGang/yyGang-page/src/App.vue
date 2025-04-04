<template>
  <RouterView></RouterView>
</template>

<script setup>

  import { useAuthStore } from './stores/auth';

  const authStore = useAuthStore();
  
  authStore.checkLogin();
</script>