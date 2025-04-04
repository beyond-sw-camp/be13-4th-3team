<template>
    <div class="flex-shrink-0 p-3" style="width: 280px;">

        <div class="row">
          <div class="d-flex align-items-center pb-3 mb-3 link-body-emphasis border-bottom">
              <img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/mainLogo.png" alt="mainLogo" style="width: 80px;">
              <span class="fs-5 fw-semibold flex-grow-1">Y<span style="color: green;">yang</span>G</span>
              <button @click="$emit('toggle-sidebar')" class="btn"><i class="bi bi-x"></i></button>
          </div>
        </div>
        <ul class="list-unstyled ps-0">
            <li class="mb-1">
                <a href="/" class="align-items-center d-inline-flex align-items-center rounded border-0 text-decoration-none ms-3 text-dark">
                    홈
                </a>
        </li>
        <li v-for="(item, index) in menuItems" :key="index" class="mb-1">
          <!-- 버튼 클릭 시 toggleCollapse 함수 실행 -->
          <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0"
            @click="toggleCollapse(index)">
            {{ item.title }}
          </button>
          <!-- collapse 대상 div -->
          <div class="collapse" :id="`collapse-${index}`" ref="collapseRefs">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li v-for="(subItem, subIndex) in item.subMenu" :key="subIndex">
                <RouterLink :to="subItem.path" class="link-body-emphasis d-inline-flex text-decoration-none rounded ms-3">
                  {{ subItem.name }}
                </RouterLink>
              </li>
            </ul>
          </div>
        </li>
        <li v-if="!isLoggedIn" class="mb-1 border-top">
          <RouterLink to="/login" class="btn btn-toggle d-inline-flex align-items-center rounded border-0 ms-0 small-text" style="color: gray;">
            로그인
          </RouterLink>
        </li>
        <li v-if="isLoggedIn && userInfo.role === 'SELLER'" class="mb-1 border-top">
          <RouterLink :to="{name:'registProduct'}" class="btn btn-toggle d-inline-flex align-items-center rounded border-0 ms-0 small-text" style="color: gray;">
            상품 등록
          </RouterLink>
        </li>
        <li v-if="isLoggedIn && userInfo.role === 'SELLER'" class="mb-1">
          <RouterLink :to="{name:'registProductList'}" class="btn btn-toggle d-inline-flex align-items-center rounded border-0 ms-0 small-text" style="color: gray;">
            상품 조회
          </RouterLink>
        </li>
        <li v-if="isLoggedIn" class="mb-1 border-top">
          <RouterLink to="/user/my-page" class="btn btn-toggle d-inline-flex align-items-center rounded border-0 ms-0 small-text" style="color: gray;">
            마이페이지
          </RouterLink>
        </li>
        <li v-if="isLoggedIn" class="mb-1">
          <RouterLink to="/user/my-page/pass-word" class="btn btn-toggle d-inline-flex align-items-center rounded border-0 ms-0 small-text" style="color: gray;">
            비밀번호 변경
          </RouterLink>
        </li>
        <li v-if="isLoggedIn" class="mb-1">
          <RouterLink to="/user/my-page/withdraw" class="btn btn-toggle d-inline-flex align-items-center rounded border-0 ms-0 small-text" style="color: gray;">
            회원탈퇴
          </RouterLink>
        </li>
        <li v-if="isLoggedIn" class="mb-1">
          <RouterLink :to="{name:'cart'}" class="btn btn-toggle d-inline-flex align-items-center rounded border-0 ms-0 small-text" style="color: gray;">
            장바구니
          </RouterLink>
        </li>
        <li v-if="isLoggedIn" class="mb-1">
          <RouterLink :to="{name:'orderList'}" class="btn btn-toggle d-inline-flex align-items-center rounded border-0 ms-0 small-text" style="color: gray;">
            주문내역조회
          </RouterLink>
        </li>
      </ul>
    </div>
  </template>
  
<script setup>
  import { ref, onMounted } from 'vue';
  import Collapse from 'bootstrap/js/dist/collapse';
  import { useAuthStore } from '@/stores/auth';
  const authStore = useAuthStore();
  const isLoggedIn = authStore.isLoggedIn;
  const userInfo = authStore.userInfo;
  
  // 메뉴 데이터
  const menuItems = ref([
    {
      title: "제품 정보",
      subMenu: [
        { name: "제품 정보", path: "/supplement" },
        { name: "건강 기능 별 제품", path: "/supplement" },
        { name: "성분 별 제품 확인", path: "/supplement" },
      ]
    },
    {
      title: "추천 영양제 찾기",
      subMenu: [
        { name: "나이별 추천", path: "/" },
        { name: "성별 추천", path: "/" },
        { name: "개인 맞춤 추천", path: "/" },
      ]
    },
    {
      title: "약 질문하기",
      subMenu: [
        { name: "약 질문하기", path: "/questionboard" },
        { name: "자주 묻는 질문", path: "/" },
      ]
    },  {
      title: "자유게시판",
      subMenu: [
        { name: "자유 게시판", path: "/board" },
        { name: "인기 게시글", path: "/" },
      ]
    }
  ]);
  
  // collapse 요소들의 ref 배열
  const collapseRefs = ref([]);
  const collapseInstances = ref([]);
  
  // Bootstrap Collapse 인스턴스 초기화
  onMounted(() => {
    collapseInstances.value = collapseRefs.value.map(
      (el) => new Collapse(el, { toggle: false })
    );
  });
  
  // 특정 index의 collapse를 토글하는 함수
  const toggleCollapse = (index) => {
    if (collapseInstances.value[index]) {
      collapseInstances.value[index].toggle();
    }
  };
</script>


<style>
  .small-text {
    font-size: 0.8rem;
  }
</style>
