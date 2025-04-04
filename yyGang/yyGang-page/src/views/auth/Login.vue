<template>
    <!-- 부트 스트랩에서 로그인폼 정의 CSS 스타일 정의 클래스 -->
    <main class="form-signin w-100 m-auto">
        <!-- LoginForm 컴포넌트에서 form-submit 이벤트 발생 시 
             LoginForm 내의 formSubmit 메소드 호출 -->
        <LoginForm @form-submit="formSubmit"></LoginForm>
    </main>
</template>

<script setup>
    // import '@/assets/css/auth/sign-in.css';
    import LoginForm from '@/components/forms/LoginForm.vue';
    import { useAuthStore } from '@/stores/auth';

    const formSubmit = (formData) => {
        const authStore = useAuthStore();

        authStore.login(formData);
    };
</script>