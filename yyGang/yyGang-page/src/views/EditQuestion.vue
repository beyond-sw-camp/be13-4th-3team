<template>
    <div>
        <EditQuestionForm :questionboard="questionData" @form-submit="editSubmit" submit-button-text="등록"></EditQuestionForm>
    </div>
</template>

<script setup>
    import EditQuestionForm from '@/components/forms/EditQuestionForm.vue';
    import { useRoute, useRouter } from 'vue-router';
    import apiClient from '@/api';
    import { onMounted, reactive, ref } from 'vue';


    const router = useRouter();
    const currentRoute = useRoute(); 
    const questionData = reactive({ 
        title: '', 
        content: ''
    });


    const fetchQuestion = async (id) => {
        try {
            const response = await apiClient.get(`/qboard/${id}`);

            console.log("API Response:", response);

            questionData.qboardTitle = response.data.qboardTitle;
            questionData.qboardContent = response.data.qboardContent;
            questionData.qboardDate = response.data.qboardDate;
            questionData.viewCount = response.data.viewCount;

        } catch (error) {

            console.error("Error fetching question:", error);

            if (error.response.data.code === 404) {
                alert(error.response.data.message);

                router.push({name: 'questionboard'});
            } else {
                alert('에러가 발생했습니다.');
            }
        }
    };

    const editSubmit= async(formData)=>{
        try {   
            const response = await apiClient.put(`/qboard/${currentRoute.params.id}`,formData);
            
            console.log("이거 지금 안먹히는 것같은데");
            console.log("API Response:", response);
            console.log("formData:",formData);
            console.log("id",currentRoute.params.id);
            console.log("API Status:", response.status);
            console.log("Response Data:", response.data);
            

            if(response.status === 200){
                alert(`정상적으로 등록`);
                console.log("이거 지금 안먹히는 것같은데");
                router.push({name:'questionboard'});
            }
        } catch (error) {
            console.log(error)
        }
    }

    onMounted(() => {
        fetchQuestion(currentRoute.params.id);
    });


</script>

<style lang="scss" scoped>

</style>