<template>
    <main>
        <RegistProductTable :nsupplements="nsupplements" @delete-item="deleteItems"/>
    </main>
</template>

<script setup>
    import apiClient from '@/api';
    import { onMounted, ref } from 'vue';
    import RegistProductTable from '@/components/tables/RegistProductTable.vue';

    const nsupplements = ref([]);  // 상품 목록 데이터를 저장할 변수

    // 상품 데이터를 API에서 불러오는 함수
    const fetchDepartments = async () => {
        try {
            const response = await apiClient.get('/nsupplement');  // API 호출
            nsupplements.value = response.data;  // 상품 목록을 nsupplements에 저장
        } catch (error) {
            if (error.response && error.response.data.code === 404) {
                alert(error.response.data.message);
            } else {
                alert('에러가 발생했습니다.');
            }
        }
    };

    // 컴포넌트가 마운트되면 상품 목록을 불러옴
    onMounted(() => {
        fetchDepartments();
    });

    // 여러 상품 삭제 처리
    const deleteItems = async (productIds) => {
        try {
            const deletePromises = productIds.map(id => apiClient.delete(`/nsupplement/${id}`));  // 각 상품을 삭제하는 API 호출
            await Promise.all(deletePromises);  // 모든 삭제 요청을 병렬로 처리
            nsupplements.value = nsupplements.value.filter(item => !productIds.includes(item.productId));  // 삭제된 상품 제외
            alert('선택된 상품이 삭제되었습니다.');
        } catch (error) {
            alert('삭제 실패');
            console.error(error);
        }
    };
</script>

<style scoped>

</style>
