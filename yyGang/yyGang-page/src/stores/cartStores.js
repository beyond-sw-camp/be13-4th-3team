import { defineStore } from "pinia";
import { ref } from "vue";


export const useCartStore = defineStore('cart', () => {
  // 상태 정의
  const cartItems = ref([]); // 배열을 ref로 감싸야 반응형 상태로 만들 수 있음

  // 액션 정의
  const setCartItems = (items) => {
    cartItems.value = items; // cartItems를 업데이트
  };

  return {
    cartItems,
    setCartItems,
  };
});