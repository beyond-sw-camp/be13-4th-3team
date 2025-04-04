<template>
    <div>
        <OrderForm :nsupplement="nsupplement" :quantity="quantity"></OrderForm>
    </div>
</template>

<script setup>

import OrderForm from '@/components/forms/OrderForm.vue';
import apiClient from '@/api';
import { onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const currentRoute = useRoute();
const nsupplement = reactive([]);


const fetchNSupplement = async (productId) => {
        try {
            const response = await apiClient.get(
                `/nsupplement/${productId}`
            );
            console.log(response);
            const data = typeof response.data === 'string'
        
        ? JSON.parse(response.data)
        : response.data;

        console.log(response.data);

        Object.assign(nsupplement, data);
        console.log('최종 nsupplement:', nsupplement);

        } catch (error) {
            console.log(error);
            
            // if (error.response.data.code === 404) {
            //     alert(error.response.data.message);

            //     router.push({name: 'nsupplement'});
            // } else {
            //     alert('에러가 발생했습니다.');
            // }
        }
    };

    onMounted(() => {
        fetchNSupplement(currentRoute.params.productId);
    });

</script>

<style lang="scss" scoped>

</style>