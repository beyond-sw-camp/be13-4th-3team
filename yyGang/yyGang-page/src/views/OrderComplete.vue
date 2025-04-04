<template>
  <div class="order-complete">
    <div class="icon-wrapper">
      <div class="check-icon">✔</div>
    </div>
    <h2 class="thank-you-text">구매해주셔서 감사합니다!</h2>

    <div class="order-info">
      <table>
        <tr>
          <td>주문 번호</td>
          <td>{{ order.orderId }}</td>
        </tr>
        <tr>
          <td>결제 금액</td>
          <td>{{ order.totalPrice }}원</td>
        </tr>
        <tr>
          <td>주문 일시</td>
          <td>{{ order.orderDate }}</td>
        </tr>
      </table>

      <hr />

      <table>
        <tr>
          <td>배송지</td>
          <td>
            {{userInfo.name}}<br />
            {{userInfo.phone}}<br />
            {{ userInfo.address }} 
          </td>
        </tr>
        <tr>
          <td>배송 방법</td>
          <td>택배</td>
        </tr>
        <tr>
          <td>배송 메모</td>
          <td>경비실에 맡겨주세요.</td>
        </tr>
      </table>
    </div>

    <button class="confirm-button" @click="onConfirm">확인</button>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';
import apiClient from '@/api';

const route = useRoute();
const router = useRouter();
const orderId = route.query.orderId;
const order = ref([]);

const userInfo = ref({
  name: '',
  phone: '',
  address: ''
});

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



const fetchOrderDetails = async () => {
  try {

    const response = await apiClient.get(`/order/${orderId}`); // API 호출
    order.value = response.data; // 응답 데이터로 주문 정보 설정
  } catch (error) {
    console.error('주문 정보 조회 오류:', error);
  }
};

onMounted(() => {
  fetchOrderDetails(); // 페이지가 마운트되면 주문 정보 조회
  getUserInfo();
});

const emit = defineEmits(['confirm']);

const onConfirm = () => {
  emit('confirm');
  router.push({name:'orderList'})
};


</script>

<style scoped>
.order-complete {
  text-align: center;
  max-width: 400px;
  margin: 0 auto;
  font-family: 'Noto Sans KR', sans-serif;
}

.icon-wrapper {
  margin: 30px 0 10px;
}

.check-icon {
  font-size: 60px;
  color: #4caf50;
  background-color: #e6f4ea;
  border-radius: 50%;
  width: 80px;
  height: 80px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
}

.thank-you-text {
  font-size: 18px;
  margin: 10px 0 30px;
}

.order-info table {
  width: 100%;
  margin-bottom: 10px;
  border-spacing: 0;
}

.order-info td {
  text-align: left;
  padding: 5px 0;
  font-size: 14px;
}

.order-info td:first-child {
  width: 80px;
  color: #888;
}

hr {
  margin: 15px 0;
  border: 0;
  border-top: 1px solid #ccc;
}

.confirm-button {
  margin-top: 20px;
  padding: 10px 30px;
  background-color: #b4ec51;
  color: black;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.confirm-button:hover {
  background-color: #a2e041;
}
</style>

