<template>
    <div class="container py-5">
      <!-- 배송지 정보 -->
      <section class="mb-4 p-3 bg-light rounded border">
        <h5>배송지</h5>
        <p><strong>이덕찬</strong> 010-1111-2222</p>
        <p>서울특별시 동작구 보라매로</p>
        <input type="text" class="form-control mt-2" placeholder="배송 메모를 입력해주세요" />
      </section>
  
      <!-- 주문 상품 -->
      <section class="mb-4">
        <h5>주문상품</h5>
        <div
          class="d-flex align-items-center justify-content-between border-bottom py-2"
          v-for="(item, index) in cartItems"
          :key="index"
        >
          <div class="d-flex align-items-center">
            <img :src="item.image" alt="상품 이미지" class="img-thumbnail me-3" style="width: 60px" />
            <div>
              <div><strong>{{ item.name }}</strong> <small class="text-muted">{{ item.brand }}</small></div>
              <div class="small text-muted">옵션: {{ item.option }} | 수량: {{ item.quantity }}</div>
            </div>
          </div>
          <div><strong>{{ formatPrice(item.price * item.quantity) }}</strong></div>
        </div>
  
        <div class="text-end bg-success text-white p-2 mt-3 rounded">
          총 주문 금액: <strong>{{ formatPrice(totalAmount) }}</strong>
        </div>
      </section>
  
      <!-- 결제 수단 -->
      <section class="mb-4 p-3 bg-light rounded border">
        <h5>결제 수단</h5>
        <div class="btn-group mb-2">
          <button class="btn btn-outline-secondary" v-for="method in methods" :key="method">{{ method }}</button>
        </div>
        <div class="d-flex flex-wrap gap-2 mb-3">
          <span class="badge bg-success">N Pay</span>
          <span class="badge bg-warning text-dark">Kakao Pay</span>
          <span class="badge bg-primary">Toss Pay</span>
        </div>
  
        <div class="row">
          <div class="col-md-6">
            <select class="form-select mb-2">
              <option>카카오 뱅크</option>
              <option>국민은행</option>
            </select>
          </div>
          <div class="col-md-6">
            <select class="form-select mb-2">
              <option>일시불</option>
              <option>3개월 할부</option>
            </select>
          </div>
        </div>
      </section>
  
      <!-- 결제 상세 -->
      <section class="mb-4">
        <h5>결제 상세</h5>
        <div class="mb-2">
          <input type="text" class="form-control" value="신용카드" disabled />
        </div>
        <div class="mb-2 text-end">
          <strong class="fs-5 text-success">{{ formatPrice(totalAmount) }}</strong>
        </div>
        <div class="form-check mb-3">
          <input class="form-check-input" type="checkbox" id="agree" v-model="agree" />
          <label class="form-check-label" for="agree">
            [필수] 서비스 이용 약관, 개인정보 처리 동의
          </label>
        </div>
        <div class="d-flex gap-2">
          <button class="btn btn-outline-secondary w-50">결제취소</button>
          <button class="btn btn-success w-50" :disabled="!agree">결제하기</button>
        </div>
      </section>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue';
  
  const agree = ref(false);
  const cartItems = ref([
    {
      name: 'Vitamin K-2',
      brand: 'Solaray',
      option: 'Vitamin K-2',
      quantity: 1,
      price: 13000,
      image: 'https://via.placeholder.com/60x60?text=제품'
    },
    {
      name: 'Vitamin K-2',
      brand: 'Solaray',
      option: 'Vitamin K-2',
      quantity: 2,
      price: 13000,
      image: 'https://via.placeholder.com/60x60?text=제품'
    }
  ]);
  
  const methods = ['카드', '계좌 이체', '무통장 입금'];
  
  const totalAmount = computed(() =>
    cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  );
  
  const formatPrice = (price) => {
    return price.toLocaleString() + '원';
  };
  </script>
  
  <style scoped>
  .container {
    max-width: 900px;
  }
  </style>
  