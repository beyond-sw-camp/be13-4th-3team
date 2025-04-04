<template>
    <div class="cart-wrapper py-4">
    <!-- 선택 & 삭제 -->
    <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
        <div>
        <input type="checkbox" v-model="allSelected" @change="toggleAll" /> 전체 선택
        </div>
        <button class="btn btn-sm btn-outline-danger" @click="deleteSelected">선택 삭제</button>
    </div>

    <!-- 상품 목록 -->
    <div v-for="item in cartItems" :key="item.cartOptionId" class="mb-4 p-3 border rounded">
        <div class = "col-md-1">
            <input type ="checkbox" v-model="item.checked"/>
        </div>
        <div class="row mb-3 border-bottom pb-3 align-items-center">
        <div class="col-md-2">
            <img :src="item.productImage || defaultImage" class="img-fluid rounded" />
        </div>
        <div class="col-md-5">
            <p
            class="fw-bold mb-1 text-primary"
            style="cursor: pointer;"
            @click="$emit('item-click', item.nsupplementId)">
            {{ item.nsupplementName }}
            </p>
            <p class="mb-0 text-muted">{{ item.brand }}</p>
            <span class="text-danger">{{ formatPrice(item.itemPrice) }}</span>
        </div>
        <div class="col-md-2 text-end">
            <label for="qty">수량</label>
            <input
            type="number"
            v-model.number="item.quantity"
            @change="changeQuantity(item.cartOptionId, item.quantity)"
            class="form-control form-control-sm"
            style="width: 70px; display: inline-block;"
            min="1"
            />
        </div>
        <div class="col-md-2 text-end">
            <p class="fw-bold">총 금액<br />{{ formatPrice(item.itemPrice * item.quantity) }}</p>
            <p class="small">배송비 {{ item.shippingFee ? formatPrice(item.shippingFee) : '무료' }}</p>
            <button class="btn btn-outline-danger btn-sm mt-2" @click="$emit('delete-item', item.cartOptionId)">삭제</button>
        </div>
        </div>
    </div>

    <!-- 하단 요약 -->
    <div class="bg-light p-3 rounded d-flex justify-content-between align-items-center">
        <div>
        <span class="me-3">선택상품금액 <strong>{{ formatPrice(selectedTotal) }}</strong></span>
        <span class="me-3">총 배송비 <strong>0원</strong></span>
        <span class="me-3">즉시할인예상금액 <strong class="text-danger"> 0원 </strong></span>
        <span>주문금액 <strong class="text-success">{{ formatPrice(selectedTotal) }}</strong></span>
        </div>
        <button class="btn btn-success" @click="proceedToOrderForm">선택 상품 주문하기</button>
    </div>
    </div>
</template>

<script setup>
import { ref, computed, watch, defineProps } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '@/stores/cartStores';
import apiClient from '@/api';

const cartStore = useCartStore();
const router = useRouter();

const defaultImage = 'https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/SupplementCharactor.png';
const props = defineProps({
    cartOptions: Array
});

const emit = defineEmits(['update-quantity', 'delete-item']);
const cartItems = ref([]);

watch(
    () => props.cartOptions,
    async (newVal) => {
    if (Array.isArray(newVal)) {
        console.log(newVal);
        cartItems.value = newVal.map(item => ({ ...item, checked: false }));
        
        for (let item of newVal) {
                const responseImg = await apiClient.get(`/nsupplement/productImage/${item.nsupplementId}`);
                // 각 이미지 데이터를 cartItems에 추가
                item.productImage = responseImg.data;
            }
        console.log(newVal);
    } else {
        cartItems.value = [];
    }
    },
    { immediate: true, deep: true }
);

const allSelected = computed({
    get: () => cartItems.value.every(item => item.checked),
    set: (val) => {
    cartItems.value.forEach(item => (item.checked = val));
    }
});

const selectedTotal = computed(() =>
    cartItems.value
    .filter(item => item.checked)
    .reduce((sum, item) => sum + (item.itemPrice || 0) * (item.quantity || 0), 0)
);


const formatPrice = price => price.toLocaleString() + '원';

const toggleAll = () => {
    const val = allSelected.value;
    cartItems.value.forEach(item => (item.checked = val));
};

const changeQuantity = (cartOptionId, newQty) => {
    const quantity = parseInt(newQty);
    if (quantity < 1) {
    alert('최소 수량은 1개입니다.');
    return;
    }

    const item = cartItems.value.find(item => item.cartOptionId === cartOptionId);
    if (!item) return;

    if (quantity > item.stockQuantity) {
        alert(`재고는 ${item.stockQuantity}개까지 가능합니다.`);
        item.quantity = item.stockQuantity; // 혹은 이전 값으로 되돌리기
        return;
    }
    emit('update-quantity',cartOptionId, quantity);
};

const deleteSelected = () => {
    const selectedIds = cartItems.value
        .filter(item => item.checked)
        .map(item => item.cartOptionId);

    if (selectedIds.length === 0) {
        alert('선택된 항목이 없습니다.');
        return;
    }
    emit('delete-selected', selectedIds); 
};

const proceedToOrderForm = () => {
    const selectedItems = cartItems.value.filter(item => item.checked);
    
    if (selectedItems.length === 0) {
        alert('선택된 항목이 없습니다.');
        return;
    }

    console.log(selectedItems);
    cartStore.setCartItems(selectedItems);
    // OrderForm 페이지로 이동하며 장바구니 정보를 state로 전달
        router.push({ 
            name: 'cartOrder'
        });
};

</script>

<style scoped>
.cart-wrapper {
    max-width: 1000px;
    margin: 0 auto;
    padding: 1rem;
}
img {
    max-width: 100%;
}
</style>
