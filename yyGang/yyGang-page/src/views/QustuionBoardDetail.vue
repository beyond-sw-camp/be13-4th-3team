<template>
   <div>
        <QuestionDetail :question-data="questionData" @delete-question="deleteQuestion"></QuestionDetail>
        <AnswerDetail :answer-data="answerData"></AnswerDetail>
   </div>
    
</template>

<script setup>
    import AnswerDetail from '@/components/questionboard/AnswerDetail.vue';
    import QuestionDetail from '@/components/questionboard/QuestionDetail.vue';

    import { useRoute, useRouter } from 'vue-router';
    import apiClient from '@/api';
    import { onMounted, reactive, ref } from 'vue';


    const router = useRouter();
    const currentRoute = useRoute(); 
    const questionData = reactive({ qboardTitle: '', qboardContent: '' ,qboardDate:'', viewCount: ''});
    const answerData = ref({});


    const fetchQuestion = async (id) => {
        try {
            const response = await apiClient.get(`/qboard/${id}`);

            console.log("API Response:", response);

            questionData.qboardId = response.data.qboardId;
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

    const fetchAnswer = async (id) => {
        try {
            const response = await apiClient.get(`/qboard/${id}/answers`);

            console.log("fetchAnswerasdas API Response:", response);    
            console.log("answerdata",response.data)

            answerData.value = response.data; 

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


    const deleteQuestion = async (id) => {
        try {
            const response = await apiClient.delete(`/qboard/${id}`);
            
            if(response.data.code === 200) {
                alert('정상적으로 삭제되었습니다.');

                // fetchDepartments(pageInfo.currentPage);
                router.push({name: 'questionboard'});
            }
        } catch (error) {
            if (error.response.data.code === 403) {
                alert('권한이 없는 사용자입니다.');
            } else if (error.response.data.code === 404) {
                alert(error.response.data.message);


            } else {
                alert('에러가 발생했습니다.');
            }
        }
    };

    onMounted(() => {
        fetchQuestion(currentRoute.params.id);
        fetchAnswer(currentRoute.params.id);
        // deleteQuestion(currentRoute.params.id)
    });

</script>

<style scoped>

</style>