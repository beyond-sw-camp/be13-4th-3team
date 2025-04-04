<template>
    <div>
        <RegisterReviewForm submit-button-text="등록" :init-form-data="initFormData" @form-submit="formSubmit"/>
    </div>
</template>

<script setup>
    import apiClient from '@/api';
import RegisterReviewForm from '@/components/forms/RegisterReviewForm.vue';
    import { reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';

    const currentRoute = useRoute();
    const router = useRouter();
    const initFormData = reactive({
        content: ''
    });
    

    const formSubmit = async (formData) => {
        try {
            console.log(formData);
            
            const response = await apiClient.post(
                `/nsupplement/${currentRoute.params.nSupplementId}/review`,
                formData
            );

            console.log(response);

            if (response.status === 201) {
                alert('정상적으로 등록되었습니다.');

                router.push({name: 'nsupplement/nSupplementId/review', params: {nSupplementId: currentRoute.params.nSupplementId}});
            }
        } catch(error) {
            console.log(error);
            
            if (error.status === 409) {
                alert('이미 리뷰를 작성하셨습니다.');
                router.push({name: 'nsupplement/nSupplementId/review', params: {nSupplementId: currentRoute.params.nSupplementId}});
            } else {
                alert('오류가 발생했습니다.');
                router.push({name: 'nsupplement/nSupplementId/review', params: {nSupplementId: currentRoute.params.nSupplementId}});
            }
        }
    };


    
</script>

<style lang="scss" scoped>

</style>