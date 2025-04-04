<template>
    <div class="container py-5">
      <!-- 배송지 정보 -->
      <section class="mb-4 p-4 bg-light rounded border">
        <h5 class="fw-bold">배송지</h5>
        <p><strong>{{ userInfo.name }}</strong> {{ userInfo.phone }}</p>
        <p>{{ userInfo.address }}</p>
        <input type="text" class="form-control mt-3" placeholder="배송 메모를 입력해주세요" />
      </section>
  
      <!-- 주문 상품 -->
      <section class="mb-4">
        <h5 class="fw-bold">주문상품</h5>
        <div v-for="item in cartItems" :key="item.cartOptionId" class="d-flex align-items-center mb-3">
          <img :src="item.productImage || defaultImage" alt="상품 이미지" class="img-thumbnail me-3" style="width: 60px; height: 60px; object-fit: cover;" />
          <div>
            <div><strong>{{ item.nsupplementName }}</strong> <small class="text-muted">{{ item.brand }}</small></div>
            <div class="small text-muted">옵션: {{ item.productOption }} | 수량: {{ item.quantity }}</div>
          </div>
        </div>
        <div class="mt-2">
          <strong>{{ formatPrice(totalPrice) }}</strong>
        </div>
  
        <div class="text-end bg-success text-white p-2 mt-3 rounded">
          총 주문 금액: <strong>{{ formatPrice(totalPrice) }}</strong>
        </div>
      </section>
  
      <!-- 결제 수단 -->
      <section class="mb-4 p-4 bg-light rounded border">
        <h5 class="fw-bold">결제 수단</h5>
        <div class="btn-group mb-3">
          <button class="btn btn-outline-secondary" v-for="method in methods" :key="method">{{ method }}</button>
        </div>
        <div class="d-flex flex-wrap gap-2 mb-3">
          <span class="badge bg-success"><img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/badge_npay.png" style="width: 40px; height: auto;"></span>
          <span class="badge bg-warning text-dark"><img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/payment_icon_yellow_medium.png" style="width: 40px; height: auto;"></span>
          <span class="badge bg-primary"><img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/TossPay_Logo_Primary.png" style="width: 30px; height: auto;"></span>
        </div>
  
        <div class="row">
          <div class="col-md-6">
            <select class="form-select mb-3">
              <option>카카오 뱅크</option>
              <option>국민은행</option>
            </select>
          </div>
          <div class="col-md-6">
            <select class="form-select mb-3">
              <option>일시불</option>
              <option>3개월 할부</option>
            </select>
          </div>
        </div>
      </section>
  
      <!-- 결제 상세 -->
      <section class="mb-4">
        <h5 class="fw-bold">결제 상세</h5>
        <div class="mb-2">
          <input type="text" class="form-control" value="신용카드" disabled />
        </div>
        <div class="mb-2 text-end">
          <strong class="fs-5 text-success">{{ formatPrice(totalPrice) }}</strong>
        </div>
        <div class="form-check mb-3">
          <input class="form-check-input" type="checkbox" id="agree" v-model="agree" />
          <label class="form-check-label" for="agree">
            [필수] 서비스 이용 약관, 개인정보 처리 동의
          </label>
        </div>
        <div class="d-flex gap-3">
          <button class="btn btn-outline-secondary w-50" @click="cancelPayment">결제취소</button>
          <button class="btn btn-success w-50" :disabled="!agree" @click="orderProduct">결제하기</button>
        </div>
      </section>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted, defineProps } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import apiClient from '@/api';  // apiClient는 axios 인스턴스를 가정
  
  const route = useRoute();
  const router = useRouter();
  
  // 장바구니에서 넘어온 항목들
  const quantity = ref(1);
  const agree = ref(false);
  
  const methods = ['카드', '계좌 이체', '무통장 입금'];

  const props = defineProps({
    cartItems: {
        type: Array,
        required: true,
    },
  });
  
  // 사용자 정보
  const userInfo = ref({
    name: '',
    phone: '',
    address: ''
  });
  
  // 기본 이미지
  const defaultImage = 'https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/SupplementCharactor.png';
  
  // 사용자 정보 가져오기
  const getUserInfo = async () => {
    const accessToken = localStorage.getItem('accessToken');
    try {
      const response = await apiClient.get('/user/my-page', {
        headers: { Authorization: `Bearer ${accessToken}` }
      });
      userInfo.value = response.data;
    } catch (error) {
      console.error('사용자 정보 불러오기 실패:', error);
      alert('사용자 정보를 불러오는 데 실패했습니다.');
    }
  };
  
  onMounted(() => {
    getUserInfo();
  });
  
  // 가격 포맷
  const formatPrice = (price) => {
    return price.toLocaleString() + '원';
  };
  
  // 총 가격 계산
  const totalPrice = computed(() => {
    return props.cartItems.reduce((sum, item) => sum + item.itemPrice * item.quantity, 0);
  });
  
  // 결제 취소
  const cancelPayment = () => {
    router.go(-1);
  };
  
  // 주문 처리
  const orderProduct = async () => {
    try {
      const orderDtos = props.cartItems.map(item => ({
        supplement_id: item.nsupplementId,
        count: item.quantity
      }));
  
      console.log(orderDtos);
      const response = await apiClient.post(
        '/order', 
        orderDtos
      );

      const orderId = response.data.orderId;
  
      if (response.status === 200) {
        router.push({ name: 'orderComplete', query: { orderId } });
      } else {
        alert('주문 실패');
        router.go(-1);
      }
    } catch (error) {
      console.log(error);
      alert(error.response?.data?.message || '주문 중 오류가 발생했습니다.');
      router.go(-1);
    }
  };
  </script>
  
  <style scoped>
  .container {
    max-width: 900px;
    margin: auto;
  }
  
  h5 {
    font-weight: bold;
  }
  
  .btn-group button {
    margin-right: 10px;
  }
  
  .form-select {
    background-color: #f9f9f9;
  }
  
  .text-end {
    font-size: 1.1rem;
  }
  
  .text-success {
    color: #28a745 !important;
  }
  
  .bg-light {
    background-color: #f8f9fa !important;
  }
  
  .bg-success {
    background-color: #28a745 !important;
  }
  
  .text-white {
    color: #fff !important;
  }
  
  .form-check-label {
    font-size: 0.875rem;
  }
  
  input[type="text"].form-control {
    background-color: #f9f9f9;
  }
  
  .img-thumbnail {
    border-radius: 8px;
  }
  </style>
  