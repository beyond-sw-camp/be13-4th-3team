<template>
<div>
        <form @submit.prevent="submitClick">
            <div class="input-form border-bottom pb-5" >
                <div class="input-group row">
                    <span class="input-content input-group-text col-2 ">답변</span>
                    <textarea class="form-control col-10" aria-label="With textarea" placeholder="답변을 입력하세요" v-model.trim="formData.answerContent"></textarea>
                </div>
            </div>

            <div class="btn-section d-flex mt-5 mb-5">
                <button class="submit-btn btn btn-outline-secondary fs-5" type="submit" v-text="submitButtonText"></button>
                <button class="cancel-btn btn btn-outline-secondary fs-5" type="button" @click=" this.$router.go(-1)">취소하기</button>
            </div>
        </form>
    </div>
</template>

<script setup>
 import { reactive, watch} from 'vue';
    import { useRouter } from 'vue-router';

    const router = useRouter();

    const props = defineProps({
        submitButtonText: String,
        answer: Object
    });
    
    const formData = reactive({
        answerContent: ''
    });
    
    const emit = defineEmits(['form-submit']);


    const submitClick = ()=>{
        emit('form-submit', formData)
    };

    const buttonClick = ()=>{
        router.back();    
    };


    watch(
        () => props.answer,
        (newFormData)=>{
            // formData의 속성만 newFormData의 속성의 값으로 변경
            Object.assign(formData, newFormData);
        },
        {immediate: true} // watch가 등록될 때 즉시 한 번 실행
    );

</script>

<style scoped>

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