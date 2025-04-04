<template>
 <svg xmlns="http://www.w3.org/2000/svg" class="d-none">
        <symbol id="bootstrap" viewBox="0 0 118 94">
            <title>Bootstrap</title>
            <path fill-rule="evenodd" clip-rule="evenodd" d="M24.509 0c-6.733 0-11.715 5.893-11.492 12.284.214 6.14-.064 14.092-2.066 20.577C8.943 39.365 5.547 43.485 0 44.014v5.972c5.547.529 8.943 4.649 10.951 11.153 2.002 6.485 2.28 14.437 2.066 20.577C12.794 88.106 17.776 94 24.51 94H93.5c6.733 0 11.714-5.893 11.491-12.284-.214-6.14.064-14.092 2.066-20.577 2.009-6.504 5.396-10.624 10.943-11.153v-5.972c-5.547-.529-8.934-4.649-10.943-11.153-2.002-6.484-2.28-14.437-2.066-20.577C105.214 5.894 100.233 0 93.5 0H24.508zM80 57.863C80 66.663 73.436 72 62.543 72H44a2 2 0 01-2-2V24a2 2 0 012-2h18.437c9.083 0 15.044 4.92 15.044 12.474 0 5.302-4.01 10.049-9.119 10.88v.277C75.317 46.394 80 51.21 80 57.863zM60.521 28.34H49.948v14.934h8.905c6.884 0 10.68-2.772 10.68-7.727 0-4.643-3.264-7.207-9.012-7.207zM49.948 49.2v16.458H60.91c7.167 0 10.964-2.876 10.964-8.281 0-5.406-3.903-8.178-11.425-8.178H49.948z"></path>
        </symbol>
    </svg>
    <header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">

        <button @click="$emit('toggle-sidebar')" id="list-btn">
            <i class="bi bi-list col-md-1 ms-3" ></i>
        </button>
        
        <RouterLink to="/" class="align-items-center mb-3 mb-md-0 me-auto link-body-emphasis text-decoration-none ms-5">
            <img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/mainLogo.png" alt="mainLogo" style="width: 80px;">
            <!-- <span class="fs-4">University</span> -->   
        </RouterLink>

        <ul class="nav nav-pills align-self-center justify-content-around">
            <li class="nav-item">
                <!-- <RouterLink class="nav-link" :to="{name:'home'}">Home</RouterLink> -->
                <RouterLink class="nav-link" :to="{name:'nsupplement'}">제품 정보</RouterLink>
                
            </li>
            <li class="nav-item">
                <RouterLink class="nav-link">추천 영양제 찾기</RouterLink>
                <!-- <RouterLink class="nav-link" :to="{name:'departments'}">학과 목록 조회</RouterLink> -->
            </li>
            <li class="nav-item">
                <RouterLink class="nav-link" :to="{name:'questionboard'}">약 질문하기</RouterLink>
                <!-- <RouterLink class="nav-link" :to="{name:'departments/no', params:{no:'033'}}">학과 상세 조회</RouterLink> -->
            </li>
            <li class="nav-item">
                <RouterLink class="nav-link" :to="{name:'board'}">자유 게시판</RouterLink>
                <!-- <RouterLink class="nav-link" :to="{name:'departments/add'}">학과 등록</RouterLink> -->
            </li>
        </ul>
        
        <div class="col-md-4 text-center align-self-center">
            <!-- <button type="button" class="btn btn-outline-secondary" @click="logout">Logout</button> -->
            <ul class="nav nav-pills align-self-center justify-content-end">
            <li class="nav-item" v-if="!isLoggedIn">
                <!-- <RouterLink class="nav-link" :to="{name:'home'}">Home</RouterLink> -->
                <RouterLink class="nav-link" to="/user/login">로그인</RouterLink>  
            </li>
            <li class="nav-item" v-if="!isLoggedIn">
                <RouterLink class="nav-link" :to="{name:'join'}">회원가입</RouterLink>
                <!-- <RouterLink class="nav-link" :to="{name:'departments'}">학과 목록 조회</RouterLink> -->
            </li>
            <li class="nav-item" v-if="isLoggedIn">
                <span class="nav-link">{{ userInfo.name }}님 안녕하세요!</span>
            </li>

            <li class="nav-item" v-if="isLoggedIn">
                <!-- <RouterLink class="nav-link" :to="{name:'home'}">Home</RouterLink> -->
                <button class="nav-link" @click="logout">로그아웃</button>  
            </li>
            <li class="nav-item">
                <RouterLink class="nav-link" :to="{ name: 'nsupplement' }"><i class="bi bi-search"></i></RouterLink>
                <!-- <RouterLink class="nav-link" :to="{name:'departments/no', params:{no:'033'}}">학과 상세 조회</RouterLink> -->
            </li>
            <li class="nav-item">
                <RouterLink class="nav-link" :to="{ name: 'cart' }"><i class="bi bi-cart"></i></RouterLink>
                <!-- <RouterLink class="nav-link" :to="{name:'departments/add'}">학과 등록</RouterLink> -->
            </li>
            <li class="nav-item">
                <RouterLink class="nav-link"><i class="bi bi-heart"></i></RouterLink>
                <!-- <RouterLink class="nav-link" :to="{name:'departments/add'}">학과 등록</RouterLink> -->
            </li>
        </ul>
        </div>
    </header>
</template>

<script setup>

import { useAuthStore } from '@/stores/auth';
import axios from 'axios';

const authStore = useAuthStore();
const isLoggedIn = authStore.isLoggedIn;
const userInfo = authStore.userInfo;

const logout = () => {
   if (confirm('정말로 로그아웃하시겠습니까?')) {
        authStore.logout();
    }
};
</script>

<style scoped>
.nav-link {
  color: black !important; /* 검은색으로 변경 */
}

#list-btn{
    border: 0px;
    padding: 0px;
    background-color:transparent;
    font-size: 2.5rem;
}
</style>