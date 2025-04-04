<template>
    <div>
        <main>
            <div class="main-card row mt-3">
                
                <div class="main-card-content col-6 row ms-4" v-if="!isLoggedIn">
                    <i class="bi bi-person ms-2 fs-1"></i>
                    <p class="fs-5"><span class="fs-3 fw-bold">로그인</span> 하시고 <br>
                        <span class="text-success">맞춤 서비스</span>누리세요
                    </p>
                    <RouterLink class="nav-link text-decoration-underline" :to="{name:'login'}">로그인 하기 ></RouterLink>
                </div>

                <div class="main-card-content col-6 row ms-4" v-if="isLoggedIn">
                    <div class="pill-container d-flex align-items-center">
                        <div class="pill me-2"> <!-- me-2: margin-end 2 to add space between the icon and the text -->
                            <i class="bi bi-capsule-pill fs-1"></i>
                        </div>
                        <p class="fs-5 m-0"> <!-- m-0: removes any margin from the paragraph -->
                            <span class="fs-3 fw-bold">{{userInfo.name}}</span>님
                            <span class="text-success">환영합니다!</span>
                        </p>
                    </div>
                </div>

                <div class="col-6 row justify-content-around align-content-center" id="card-icon">
                    <div class=" col d-flex flex-column align-items-center text-center">
                        <div class="check align-items-center text-center">
                            <i class="bi bi-file-earmark-check fs-1 "></i>
                        </div>
                        <p class="">상태 확인</p>
                    </div>
                    
                    <div class="col d-flex flex-column align-items-center text-center">
                        <div class="product  align-items-center text-center">
                            <i class="bi bi-capsule fs-1"></i>
                        </div>
                        <p class="">맞춤 상품</p>
                    </div>
                    
                    <div class="col d-flex flex-column align-items-center text-center">
                        <div class="wish align-items-center text-center">
                            <i class="bi bi-heart fs-1"></i>
                        </div>
                        <p class="">찜 목록</p>
                    </div>
                    

                </div>
            </div>
        </main>
    </div>
</template>

<script setup>

import { useAuthStore } from '@/stores/auth';
const authStore = useAuthStore();
const userInfo = authStore.userInfo;
const isLoggedIn = authStore.isLoggedIn;

</script>

<style scoped>

.main-img-1{
    padding: 0px;
    top: 0px;
}
.main-card{
    width: 90%;
    margin: auto;
    display: flex;
    height: 276;
    border: 1px solid;
    border-color: #BDBDBD;
    border-radius: 20px 0px 20px 0px;
    box-shadow: 5px 5px 10px 0 lightgray;
}


.check, .product, .wish {
    height: 80px;
    width: 80px;
    border-radius: 20px;
    margin-bottom: 6px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    box-shadow: 0px 5px 5px 0 ;
}

.check{
    background-color: #F2F08F;
    color: #FFAA00;
}
.product{
    background-color: #99F3BD;
    color: #00C87B;
}
.wish{
    background-color: #A1E3F9;
    color: #578FCA;
}
.icon-text > p{
    width: 80px;
}

.pill-container {
    display: flex;
    align-items: center;
}

.pill{
    color: #99F3BD;
}


</style>