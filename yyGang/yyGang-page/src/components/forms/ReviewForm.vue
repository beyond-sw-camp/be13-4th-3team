<template>
    <div class="review-section container py-5">
      <!-- 헤더 -->
      <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-4">
        <div>
          <h5 class="fw-bold mb-0">상품 리뷰</h5>
          <small class="text-muted">상품 구매하신 분들의 소중한 리뷰입니다.</small>
        </div>
        <div>
          <button class="btn btn-outline-success me-2" @click="emit('register-review-click')">리뷰 작성하기</button>
          <button class="btn btn-outline-success" @click="emit('modify-review-click')">내 리뷰 확인</button>
        </div>
      </div>
  
  
      <!-- 리뷰 리스트 -->
      <div v-for="review in reviewData.list" :key="review.reviewId" class="border-top pt-3 mb-3">
        <div class="d-flex align-items-center mb-1">
          <span class="text-muted me-3">{{ review.userName }}</span>
          <small class="text-muted">{{ review.createdAt }}</small>
        </div>
        <div class="text-dark">{{ review.content }}</div>
      </div>
  
      
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'

  const props = defineProps({
        reviewData: Object
    });

    const emit = defineEmits(['register-review-click', 'modify-review-click']);
    

  </script>
  
  <style scoped>
  .review-section {
    max-width: 800px;
  }
  
  .pagination .page-item.active .page-link {
    background-color: #198754;
    border-color: #198754;
    color: white;
  }
  </style>
  