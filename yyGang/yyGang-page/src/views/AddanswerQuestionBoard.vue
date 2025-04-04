<template>

      <div>
        <div class="header border-bottom mb-4 d-flex justify-content-between pb-3 mt-2">
            <span class="fs-1 fw-bold pb-1">답변 하기</span>
        </div>
        <PostAnswerForm @form-submit="answerSubmit" submit-button-text="등록"></PostAnswerForm>
    </div>
</template>

<script setup>
    import PostAnswerForm from '@/components/forms/PostAnswerForm.vue';
    import apiClient from '@/api';
    import { useRouter,useRoute } from 'vue-router';
    // import { onMounted } from 'vue';

    const router = useRouter();
    const currentRoute = useRoute(); 

    const answerSubmit= async(formData)=>{
        try {
            const response = await apiClient.post(`/qboard/${currentRoute.params.id}/answers`, formData);

            if(response.status === 201){
                alert(`정상적으로 등록`);

                router.push({name:'qboardDetail'});
            }
        } catch (error) {
            
            if(error.response.status===400){
                console.log(error.response.data.message);
                alert(error.response.data.message)
            }
        }
    }




</script>

<style scoped>
.header{
    width: 90%;
    margin: 0 auto;
}


</style>