<template>
    <div>
        <ReviewForm :reviewData="reviewData" @register-review-click="registerReviewClick" @modify-review-click="modifyReviewClick"/>
        
      <Pagination :pageInfo="pageInfo" 
      @change-page="changePage"/>
    </div>
</template>

<script setup>
    import ReviewForm from '@/components/forms/ReviewForm.vue';
    import Pagination from '@/components/common/Pagination.vue';
import { onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router';
import { onMounted, reactive } from 'vue';
import apiClient from '@/api';

    const currentRoute = useRoute();
    const router = useRouter();
    const pageInfo = reactive({
        currentPage: parseInt(currentRoute.query.page) || 1,
        totalElements: 0, // 전체 데이터 수
        pageLimit: 5, // 페이지네이션에 보이는 페이지의 수
        listLimit: 0 // 한 페이지에 표시될 리스트의 수
    });
    const reviewData = reactive({list: []});

    // const props = defineProps({
    //     nSupplementId: Number
    // });

    // console.log(currentRoute.query.page);
    // console.log(pageInfo.currentPage);
    
    
    const nsupplementid = currentRoute.params.nSupplementId;

    const fetchReview = async (nSupplementId, page) => {
        try {
            const response = await apiClient.get(`/nsupplement/${nSupplementId}/review?page=${page-1}&size=10`);

            reviewData.list = response.data.reviewResponseDtos;

            pageInfo.totalElements = response.data.totalElements;
            pageInfo.listLimit = 10;

        } catch (error) {
            console.log(error);

        }
    };

    const changePage = ({page, totalPages}) => {
        
        if (page >= 1 && page <= totalPages) {
            router.push({
      name: 'nsupplement/nSupplementId/review',
      params: { nSupplementId: nsupplementid },
      query: { page }
    });
        }
    };
    
    const registerReviewClick = () => {
        
        router.push({ name: 'registerReview', params: {nSupplementId: currentRoute.params.nSupplementId}});
    };

    const modifyReviewClick = () => {
        
        router.push({ name: 'modifyReview', params: {nSupplementId: currentRoute.params.nSupplementId}});
    };

    onBeforeRouteUpdate((to, form) => {
        pageInfo.currentPage = parseInt(to.query.page) || 1;

        fetchReview(nsupplementid, pageInfo.currentPage);
    });

    onMounted(() => {
        // fetchHealthTags();
        // fetchIngredientTags();

        fetchReview(nsupplementid, pageInfo.currentPage);
    });


</script>

<style lang="scss" scoped>

</style>