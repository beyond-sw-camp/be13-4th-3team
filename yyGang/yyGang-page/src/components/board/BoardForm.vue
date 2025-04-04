<template>
    <form @submit.prevent="submitClick">
            <!-- <div class="mb-3 row">
                <label for="title" class="col-sm-2 col-form-label">제목</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="title" v-model.trim="formData.title">
                </div>
            </div>
            <div class="mb-3 row">
                <label for="content" class="col-sm-2 col-form-label">내용</label>
                <div class="col-sm-10">
                    <textarea type="text" class="form-control" id="content" v-model.trim="formData.content"></textarea>
                </div>
            </div>
        <div id="divBtn">
            <button  type="submit" class="btn btn-success" v-text="submitButtonText"></button>
            <button class="btn btn-light" type="button" @click="buttonClick">뒤로가기</button>
        </div> -->
        <div class="input-form   border-bottom pb-5">
            <div class="input-group mb-3 row">
                <span class="input-title input-group-text col-2" id="basic-addon1"> 제목</span>
                <input type="text" class="form-control col-10" placeholder="제목을 입력하세요" aria-label="Username" aria-describedby="basic-addon1" v-model.trim="formData.title">
            </div>
            <div class="input-group row">
                <span class="input-content input-group-text col-2 ">내용</span>
                <textarea class="form-control col-10" aria-label="With textarea" placeholder="내용을 입력하세요" v-model.trim="formData.content"></textarea>
            </div>
        </div>

        <div class="btn-section d-flex mt-5 mb-5">
            <button class="submit-btn btn btn-outline-secondary fs-5" type="submit"  v-text="submitButtonText"></button>
            <button class="cancel-btn btn btn-outline-secondary fs-5" type="button" @click=" this.$router.go(-1)">취소하기</button>
        </div>
    </form>
</template>

<script setup>

    import { reactive, watch} from 'vue';
    import { useRouter } from 'vue-router';

    const router = useRouter();
    const formData = reactive({});
    const emit = defineEmits(['form-submit']);

    const props = defineProps({
        submitButtonText: String,
        board: Object
    });

    const submitClick = ()=>{
        emit('form-submit', formData)
    };

    const buttonClick = ()=>{
        router.back();    
    };


    watch(
        props.board,
        (newFormData)=>{
            // formData의 속성만 newFormData의 속성의 값으로 변경
            Object.assign(formData, newFormData);
        },
        {immediate: true} // watch가 등록될 때 즉시 한 번 실행
    );


</script>

<style>
    /* #divBtn{
        border-top: 2px solid rgb(51, 116, 51);
        padding-top: 3%;
    } */
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