<template>
    <div>
           <div v-for="(question, index) in questionboardWithAnswers" :key="question.qboardId" class="border mb-3" id="qcard">
               <div id="qcard-content" class="mt-3 mb-4" @click.stop="emit('item-click', question.qboardId);">
                   <div id="qcard-question" class="mb-3">
                           <div class="question-item">
                               <div id="qcard-question-title" class="fs-2 d-flex">
                                   <span id="q" class="fw-bold">Q. &nbsp</span>
                                   <p class="">{{ question.qboardTitle }}</p>
                               </div>
                               <div id="qcard-question-content">
                                   <p>{{ question.qboardContent }}</p>
                               </div>
                           </div>
   
                       <div id="qcard-question-answer" class="d-flex">
                           <img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/doctor.png" alt="의사아이콘" id="answer-icon" class="me-2">
                           <p id="qcard-question-answer-content">
                               {{ question.answers.length > 0 ? question.answers[0].answerContent : "아직 답변이 없습니다." }}
                           </p>
                       </div>
                   </div>
               </div>
           </div>
       </div>
   </template>
   
   <script setup>
       import { computed, watch } from 'vue';
   
       const props = defineProps({
           questionboard: {
               type: Array,
               required: true,
           },
           answers: {
               type: Object,  // 객체 형태로 여러 질문의 답변을 저장
               required: true,
           }
       });
   
       const emit = defineEmits(['item-click']);
   
       // questionboard와 answers를 합쳐 새로운 데이터 생성
       const questionboardWithAnswers = computed(() => {
        
           return props.questionboard.map(question => ({
            
               ...question,
               answers: props.answers[question.qboardId] || []
           }));
       });
   
   </script>
   
   <style scoped>
   #qcard {
       width: 80%;
       margin: 0 auto;
       border-radius: 20px 0px 20px 0px;
       border-color: #BDBDBD;
       box-shadow: 5px 5px 10px 0 lightgray;   
   }
   
   #qcard-content {
       width: 90%;
       margin: 0 auto;
   }
   
   #q {
       color: #6AB456;
   }
   
   #qcard-question-answer {
       height: 30px;
   }
   
   #answer-icon {
       width: 30px;
       border: 1px solid;
       border-radius: 50%;
   }
   
   #qcard-question-answer-content {
       margin: auto 0;
       font-size: 12px;
        color: #6c757d;
   }
   </style>