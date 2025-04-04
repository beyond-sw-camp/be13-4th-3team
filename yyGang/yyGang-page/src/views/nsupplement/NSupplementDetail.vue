<template>
    <div>
        <NSupplementForm :nsupplement="nsupplement" @review-click="reviewClick"/>
    </div>
</template>

<script setup>
    import apiClient from '@/api';
    import NSupplementForm from '@/components/forms/NSupplementForm.vue';
    import { onMounted, reactive, ref } from 'vue';
    import { useRoute, useRouter } from 'vue-router';

    const router = useRouter();
    const currentRoute = useRoute();
    const nsupplement = reactive([]);
    const productId = Number(currentRoute.params.productId);

    const fetchNSupplement = async (productId) => {
        try {
            const response = await apiClient.get(
                `/nsupplement/${productId}`
            );
            console.log(response);
            const data = typeof response.data === 'string'
        
        ? JSON.parse(response.data)
        : response.data;

        Object.assign(nsupplement, data);

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

    const reviewClick = (nsupplementId) => {
        
        router.push(`/nsupplement/${nsupplementId}/review`);
    };

    onMounted(() => {
        fetchNSupplement(currentRoute.params.productId);
    });

</script>

<style lang="scss" scoped>

</style>