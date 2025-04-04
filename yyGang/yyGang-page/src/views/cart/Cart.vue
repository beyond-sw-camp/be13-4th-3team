<template>
    <main class="cart-container">
    <CartForm
        :cart-options="cartOptions"
        @update-quantity="updateQuantity"
        @delete-item="deleteCart"
        @delete-selected="deleteSelectedItems"
        @item-click="itemClick"
    />


    <Pagination
        :pageInfo="pageInfo"
        @change-page="changePage"
    />
    </main>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import apiClient from '@/api';
import { onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router';
import CartForm from '@/components/forms/CartForm.vue';
import Pagination from '@/components/common/Pagination.vue';
import { useCartStore } from '@/stores/cartStores';

const cartStores = useCartStore();
const cartOptions = ref([]);
const currentRoute = useRoute();
const router = useRouter();

const pageInfo = reactive({
    currentPage: parseInt(currentRoute.query.page) || 1,
    totalCount: 0,
    pageLimit: 5,
    listLimit: 10
});

const fetchCart = async (page) => {

    try {
    const response = await apiClient.get(`/cart?page=${page}&size=${pageInfo.listLimit}`);
    cartOptions.value = response.data.cartOptions;
    pageInfo.currentPage = page;
    pageInfo.totalCount = response.data.totalCount;
    

    } catch (error) {
    if (error.response.data.code === 404) {
        alert(error.response.data.message);
        router.push({ name: 'cart' });
    } else {
        alert('장바구니 조회 중 에러가 발생했습니다.');
    }}
    
};

const changePage = ({ page, totalPages }) => {
    if (page >= 1 && page <= totalPages) {
    router.push({ name: 'cart', query: { page } });
    }
};

// 상세 페이지 등으로 이동
const itemClick = (no) => {
    if (!no) return alert("상품 ID가 없습니다.");
    router.push({name: 'nsupplement/no', params: {no}});
};

const deleteCart = async (cartOptionId) => {
    if (!confirm('정말로 삭제하시겠습니까?')) return;

    try {
    await apiClient.delete(`/cart/${cartOptionId}`);
    alert('장바구니에서 삭제되었습니다.');
    fetchCart(pageInfo.currentPage);
    } catch (error) {
    if (error.response.data.code === 404) {
        alert(error.response.data.message);
        router.push({ name: 'cart' });
    } else {
        alert('삭제 중 에러가 발생했습니다.');
    }
    }
};

// 체크박스된 상품 선택 삭제
const deleteSelectedItems = async (selectedIds) => {
    if (!confirm('선택된 상품을 삭제하시겠습니까?')) return;

    try {
        await Promise.all(
        selectedIds.map(id => apiClient.delete(`/cart/${id}`))
        );
        alert('선택한 상품이 모두 삭제되었습니다.');
        fetchCart(pageInfo.currentPage);
    } catch (error) {
        console.error('선택 삭제 중 오류:', error);
        alert('선택 삭제 중 오류가 발생했습니다.');
    }
};

const updateQuantity = async (cartOptionId, quantity) => {
    try {
    const response = await apiClient.put(`/cart/${cartOptionId}?quantity=${quantity}`);
    const updated = response.data;
    const index = cartOptions.value.findIndex(opt => opt.cartOptionId === updated.cartOptionId);
    if (index !== -1) {
        cartOptions.value[index] = updated;
    }
    } catch (error) {
    const code = error.response.data.code;
    const message = error.response.data.message;
    if (code === 404) {
        switch(message) {
            case '장바구니가 비었습니다.':
            case '등록된 상품이 없습니다.':
                alert(message);
                break;
        }
    } else if (code === 400) {
        alert(message);
    }
    else {
        alert('에러가 발생했습니다.');
    }
    }
};

onBeforeRouteUpdate((to, form) => {
    pageInfo.currentPage = parseInt(to.query.page) || 1;
    fetchCart(pageInfo.currentPage);
});

onMounted(() => {
    fetchCart(pageInfo.currentPage);
});
</script>

<style scoped>
.cart-container {
    padding: 2rem 1rem;
}
</style>
