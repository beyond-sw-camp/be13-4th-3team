<template>
      <div>
        <div class="header border-bottom mb-4 d-flex justify-content-between pb-3 mt-2">
            <span class="fs-1 fw-bold pb-1">약 질문</span>
        </div>
         <PostQuestionForm  @form-submit="formSubmit" submit-button-text="등록"></PostQuestionForm>
    </div>
</template>

<script setup>
    import PostQuestionForm from '@/components/forms/PostQuestionForm.vue';
    import apiClient from '@/api';
    import { useRouter } from 'vue-router';
        
    const router = useRouter();

    const formSubmit= async(formData)=>{
        console.log("🔥 전송 전 formData:", JSON.stringify(formData)); // 추가
        try {
            const response = await apiClient.post(`/qboard`,formData);

            console.log("formData==" + formData.qboardTitle);
            console.log("formData==" + formData.qboardContent);
            

            if(response.status === 201){
                alert(`정상적으로 등록`);

                router.push({name:'questionboard'});   
            }
        } catch (error) {
            
            if(error.response.status===400){
                alert('500자이내작성')
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