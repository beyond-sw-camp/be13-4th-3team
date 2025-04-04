<template>
    <div class="order-history">
      <h2>주문 내역</h2>
      <hr />
  
      <!-- 주문 내역 반복 -->
      <div v-for="(order, index) in orders" :key="index" class="order-item">
        <img :src="order.productImage" alt="상품 이미지" class="product-image" />
  
        <div class="product-info">
          <div class="name">{{ order.nsupplementName }}</div>
          <div class="price">{{order.totalPrice}}원 · {{ order.quantity }}개</div>
        </div>
  
        <div class="status">
          <div class="text">{{ getIngredientName(order.status) }}</div>
          <div class="date">{{ order.orderDate }}</div>
        </div>
  
        <button class="cancel-button" @click="cancelOrder(order)">주문 취소</button>
      </div>
  

    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import apiClient from '@/api';  // apiClient를 실제 파일에 맞게 수정하세요.
  
  const orders = ref([]);


  const getIngredientName = (status) => {
    switch(status) {
      case 'PENDING': return '결제 대기';
      case 'ORDERED': return '주문 완료';
      case 'PAID': return '결제 완료';
      case 'CANCELLED': return '주문 취소';
      case 'IN_TRANSIT': return '배송 출발';
      case 'DELIVERED': return '배송 완료';
      default: return status;
    }
  };
  
  const fetchOrders = async () => {
    try {
      const response = await apiClient.get('/order');
      console.log(response.data);

      orders.value = response.data;  // 서버에서 반환된 주문 내역

      for (let order of orders.value) {
        const responseImg = await apiClient.get(`/nsupplement/productImage/${order.nsupplementId}`);
        const responseDate = await apiClient.get(`/order/${order.orderId}`);
        order.productImage = responseImg.data;  // 각 주문에 이미지를 추가
        order.orderDate = responseDate.data.orderDate;
        order.status = responseDate.data.status;
        }

      console.log(orders.value);
    } catch (error) {
      console.error('주문 정보 조회 오류:', error);
    }
  };
  
  const cancelOrder = async (order) => {
  // 사용자에게 확인 메시지를 표시
  const isConfirmed = window.confirm("정말 주문을 취소하시겠습니까?");
  
  // 사용자가 '확인'을 클릭한 경우
  if (isConfirmed) {
    console.log('주문 취소:', order);
    console.log(order.orderId);

    try {
      // 주문 취소 API 호출
      const response = await apiClient.post(`/order/${order.orderId}`);
      
      if (response.status === 200) {
        // 주문 취소 성공 후 페이지 새로고침
        window.location.reload(); // 페이지 새로고침
      }
    } catch (error) {
      alert('주문 취소 실패');
      console.error('주문 취소 실패:', error);
    }
  } else {
    // 취소 버튼을 클릭했을 때, 아무 것도 하지 않음
    console.log("주문 취소가 취소되었습니다.");
    alert('주문 취소가 취소되었습니다.');
  }
};

  
 
  // 컴포넌트 마운트 시 주문 내역 데이터 조회
  onMounted(() => {
    fetchOrders();
  });


  </script>
  
  <style scoped>
  .order-history {
    max-width: 800px;
    margin: 0 auto;
    font-family: 'Noto Sans KR', sans-serif;
  }
  
  h2 {
    font-size: 18px;
    margin-bottom: 10px;
  }
  
  hr {
    margin-bottom: 20px;
    border: none;
    border-top: 1px solid #ccc;
  }
  
  .order-item {
    display: flex;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #eee;
  }
  
  .product-image {
    width: 60px;
    height: 60px;
    object-fit: contain;
    margin-right: 15px;
  }
  
  .product-info {
    flex-grow: 1;
  }
  
  .name {
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .price {
    font-size: 14px;
    color: #666;
  }
  
  .status {
    text-align: right;
    margin-right: 20px;
    min-width: 130px;
  }
  
  .status .text {
    font-weight: bold;
  }
  
  .status .date {
    font-size: 12px;
    color: #888;
  }
  
  .cancel-button {
    background-color: #b4ec51;
    border: none;
    padding: 6px 12px;
    border-radius: 5px;
    font-weight: bold;
    cursor: pointer;
  }
  
  .cancel-button:hover {
    background-color: #a2e041;
  }
  
  .pagination {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
  
  .pagination button {
    padding: 8px 15px;
    border: none;
    background-color: #f0f0f0;
    cursor: pointer;
    margin: 0 10px;
  }
  
  .pagination button:disabled {
    cursor: not-allowed;
    background-color: #ccc;
  }
  </style>
  