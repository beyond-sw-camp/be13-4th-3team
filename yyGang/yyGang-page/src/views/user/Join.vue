<template>
    <div>
        <JoinForm submit-button-text="회원 가입" :init-form-data="initFormData"
        @form-submit="formSubmit"/>
    </div>
</template>

<script setup>
import JoinForm from '@/components/forms/JoinForm.vue';
import { reactive } from 'vue';
import apiClient from '@/api';
import { useRouter } from 'vue-router';

const router = useRouter();

const initFormData = reactive({
        email: '',
        userPwd: '',
        userPwd2: '',
        name: '',
        birthday: '', // 생년월일
        gender: '',
        phone: '',
        address: ''
    });

// 비동기 통신
const formSubmit = async (formData) => {
        
        try{
            const response = await apiClient.post(
                '/user/join',
                formData,
            );

            if(response.status === 201){
                alert('정상적으로 등록되었습니다.');

                // 정상적으로 등록이 완료된 경우, 회원 상세 정보 조회로 이동시키도록
                router.replace({name: 'login'});   // 일단 홈으로 이동
            }
        } catch(error){
            alert('에러가 발생했습니다.')
        }
    };
</script>

<style lang="scss" scoped>

</style>