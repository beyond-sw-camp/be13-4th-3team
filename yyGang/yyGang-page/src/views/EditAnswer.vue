<template>
    <div>
        <EditAnswerForm :answer-data="answerData" @form-submit="editSubmit" submit-button-text="수정"></EditAnswerForm>
    </div>
</template>
    
    <script setup>
    import EditAnswerForm from '@/components/forms/EditAnswerForm.vue';
    import { reactive, watch, onMounted} from 'vue';
    import { useRoute, useRouter } from 'vue-router';
    import apiClient from '@/api';
    
        const router = useRouter();
        const currentRoute = useRoute(); 
    
        const props = defineProps({
            submitButtonText: String,
            answer: Object
        });
        
        const answerData = reactive({
            answerContent: '',
            answerId: null,
        });
        

        const fetchAnswer = async (id) => {
        try {
            const answerId = currentRoute;

            const response = await apiClient.get(`/qboard/${id}/answers`);

            answerData.answerId = response.data[0].answerId;
            answerData.answerContent = response.data.answerContent; // ✅ 답변 내용 저장
            

            console.log("API Response:", response);
            console.log("API response.data Response:", response.data);
            console.log("API response.data.answerId Response:", response.data.answerId);
            console.log("API response.data[0].answerId Response:", response.data[0].answerId);

            console.log("answerId",answerId);


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


    const editSubmit= async(formData)=>{
        // console.log("🔥 전송 전 formData:", JSON.stringify(formData)); // 추가
        try {   

            const response = await apiClient.put(`/qboard/${currentRoute.params.id}/answers/${answerData.answerId}`,formData);
            
            console.log("이거 지금 안먹히는 것같은데");
            console.log("API Response:", response);
            console.log("formData:",formData);  
            console.log("id",currentRoute.params.id);
            console.log("currentRoute.params",currentRoute.params);
            console.log("currentRoute",currentRoute);
            console.log("API Status:", response.status);
            console.log("Response Data:", response.data);
            console.log("answerData", answerData);
            

            if(response.status === 200){
                alert(`정상적으로 등록`);
                console.log("이거 지금 안먹히는 것같은데");
                router.push({name:'questionboard'});
            }
        } catch (error) {
            // if(error.response.data.code===400){
            //     alert('500자이내작성')
            //     console.log("이거 지금 안먹히는 것같은데");
            // }
            console.log(error)
        }
    }



        // const emit = defineEmits(['form-submit']);
    
    
        // const submitClick = ()=>{
        //     emit('form-submit', formData)
        // };
    
        // const buttonClick = ()=>{
        //     router.back();    
        // };
    
    
        // watch(
        //     () => props.answer,
        //     (newFormData)=>{
        //         // formData의 속성만 newFormData의 속성의 값으로 변경
        //         Object.assign(formData, newFormData);
        //     },
        //     {immediate: true} // watch가 등록될 때 즉시 한 번 실행
        // );

        onMounted(() => {
            fetchAnswer(currentRoute.params.id);
        });
    
    </script>
    
    <style scoped>
    .header{
        width: 90%;
        margin: 0 auto;
    }
    
    .input-form{
        width: 90%;
        margin: 0 auto;
    }
    
    .input-title{
        min-height: 55px;
        justify-content: center;
    }
    .input-content{
        min-height: 300px;
        align-items: flex-start;
        justify-content: center;
    }
    
    .btn-section{
        width: 90%;
        margin: 0 auto;
        justify-content: space-evenly;
        
    }
    
    .submit-btn, .cancel-btn{
        width: 25%;
        border-radius: 20px 0px 20px 0px;
        border-color: #BDBDBD;
        box-shadow: 5px 5px 10px 0 lightgray;   
    }
    
    .submit-btn{
        background-color: #A0D683;
    }
    
    </style>