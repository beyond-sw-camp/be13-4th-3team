<template>
<div class="a-box border"  v-for="(answer, index) in answerData" :key="index">
            <div class="answer mt-4">
                <div class="a-title-box align-items-center d-flex">
                    <div class="a-title fw-bold fs-2 ">
                        <img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/doctor.png" class="doctor-icon me-3">
                        {{answer.userId}}  약사님의 답변
                    </div>
                 </div>
                <div class="a-content fs-5 mt-3"> 
                    {{answer.answerContent}}
                </div>                
                <div class="tag">
                    
                </div>
                <div class="a-info d-flex">
                    <div class="createAt me-4">{{ answer.answerDate }}</div>
                    <div class="count">조회수 : 3</div>
                </div>
                <div class="helpful d-flex">
                    <button class="btn btn-success rounded-pill px-3 me-1" type="button">도움이 되요</button>
                    <div class="helpful-text"><span class="fw-bold">??</span>명에게 도움이 되었습니다.</div>
                    <div class="btn-section text-end">

                        <button class="btn btn-success rounded-pill px-3 me-1 " type="button">
                            <RouterLink class="text-decoration-none text-white" :answer-data="answerData" :to="{name:'answerEdit'}">수정하기</RouterLink>
                        </button>
                        <button class="btn btn-danger rounded-pill px-3" type="button" @click.stop="confirmDelete(questionData.qboardId);">
                            삭제
                        </button>
                   
                    </div>
                </div>
            </div> 
        </div>
</template>

<script setup>

import { defineProps } from 'vue';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const userInfo = authStore.userInfo;


console.log("authStore",authStore)
console.log("userInfo",userInfo)



const props = defineProps({
    answerData: {
        type: Array,
        required: true,
    },
});

</script>

<style scoped>
.q-box{
    width: 90%;
    margin: 0 auto;
    height: 500px;
    border-radius: 20px 0px 20px 0px;
    border-color: #BDBDBD;
    box-shadow: 5px 5px 10px 0 lightgray;   
}
.question{
    width: 80%;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    height: 80%;
}

.answer{
    height: 70%;
}

.a-content{
    width: 80%;
    height: 80%;
    margin: 0 auto;
}

.q-info, .a-info, .a-content, .helpful, .a-title{
    width: 80%;
    margin: 0 auto;
}

.helpful {
    width: 80%;
    margin: 0 auto;
    display: flex;
    align-items: center;
}

.btn-section {
    margin-left: auto;
}

.a-box{
    width: 90%;
    margin: 0 auto;
    height: 500px;
    border-radius: 20px 0px 20px 0px;
    border-color: #BDBDBD;
    box-shadow: 5px 5px 10px 0 lightgray;   
}



.a-title-box{
    /* background-color: #B6F9D5; */
    /* background-color: #A8E063; */
    background-color: #E8F5E9;
    height: 100px;
}

.doctor-icon{
    width: 30px;
    height: 30px;
}

.helpful-text{
    margin: auto 0;
}


</style>