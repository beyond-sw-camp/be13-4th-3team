<template>
    <div>
        <RegisterReviewForm submit-button-text="수정" :init-form-data="initFormData" @form-submit="formSubmit"/>
    </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import RegisterReviewForm from '@/components/forms/RegisterReviewForm.vue';
import Login from '../auth/Login.vue';
import apiClient from '@/api';

    const router = useRouter();
    const currentRoute = useRoute();
    const initFormData = reactive({
        
    });

    const fetchReview = async (nSupplementId) => {
        try {
            const response = await apiClient.get(
                `/nsupplement/${nSupplementId}/review/my`
            );

            Object.assign(initFormData, { content: response.data });
        } catch (error) {
            if(error.status === 404) {
                alert('작성한 리뷰가 없습니다.');
            }
            else {
                alert('에러가 발생했습니다.');
            }
            router.push({name: 'nsupplement/nSupplementId/review', params: {nSupplementId: currentRoute.params.nSupplementId}});
        }
    };

    const formSubmit = async (formData) => {
        try {
            console.log(formData);
            
            const response = await apiClient.put(
                `/nsupplement/${currentRoute.params.nSupplementId}/review`,
                formData
            );
            
            if(response.status === 201) {
                alert('정상적으로 수정되었습니다.');
                router.push({name: 'nsupplement/nSupplementId/review', params: {nSupplementId: currentRoute.params.nSupplementId}});
            }
        } catch (error) {
            console.log(error);
            
            alert('에러가 발생했습니다.');
        }
    };

    onMounted(() => {
        fetchReview(currentRoute.params.nSupplementId);
    });


</script>

<style lang="scss" scoped>

</style>