<template>
    <!-- 로그인 폼 제출 시 submitClick 함수 호출 -->
    <form @submit.prevent="submitClick">
        <div class="justify-content-center d-flex border-bottom mb-5" >   
            <RouterLink to="/">
                <img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/mainLogo.png"  alt="mainLogo" width="200px">
            </RouterLink>
        </div>
        <h1 class="h3 mb-3 fw-normal text-center">로그인</h1>
        <div class="row">
            <div class="form-floating mb-3 mt-3">
                <input type="text" class="form-control" id="floatingInput" placeholder="E-mail"
                v-model.trim="formData.email" required>
                <label for="floatingInput">E-mail</label>
            </div>
            <div class="form-floating mb-3">
                <input type="password" class="form-control" id="floatingPassword" placeholder="Password"
                v-model.trim="formData.password" required>
                <label for="floatingPassword">Password</label>
            </div>
            <div class="mb-5">
                <input type="checkbox" class="btn-check" id="save-login-btn" autocomplete="off">
                <label id="saveId" class="btn btn-sm btn-outline-dark me-1" for="save-login-btn"><i class="bi bi-check fs-7 "></i></label>
                자동 로그인
            
                <input type="checkbox" class="btn-check" id="save-id-btn" autocomplete="off">
                <label id="saveId" class="btn btn-sm btn-outline-dark me-1 ms-2" for="save-id-btn"><i class="bi bi-check fs-7 "></i></label>
                아이디 저장
            </div>
        </div>
        <button class="btn btn-primary w-100 py-2" type="submit">Sign in</button>
    </form>
</template>

<script setup>
    // reactive: 반응형 객체 생성 시 사용 
    // toRaw : 반응형 객체의 원본 반환
    import { reactive, toRaw } from 'vue';

    const formData = reactive({
        email: '',
        password: ''
    });

    // defineEmits : 컴포넌트에서 부모 컴포넌트로 이벤트 발생시킬 때 사용
    // 자식 컴포넌트에서 부모 컴포넌트로 보내는 이벤트 이름은 form-submit, 메서드 생성
    const emit = defineEmits(['form-submit']);

    // submitClick: 사용자가 폼 제출 시 호출
    const submitClick = () => {
        // 사용자가 폼을 제출할 경우, form-submit 이벤트를 발생시킨 뒤, formData의 원본 데이터를 전달함
        emit('form-submit', toRaw(formData));
    };


</script>

<style scoped>
label{
    left: auto;
}   

#saveId{
    border-radius: 20px;

}

</style>