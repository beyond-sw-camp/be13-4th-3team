<template>
    <!-- 게시글 전체 목록 페이지 -->
    <div>
        <div class="border-bottom mb-4 d-flex justify-content-between" id="header">
            <span class="fs-1 fw-bold pb-1">약 질문</span>
            <button class="postbtn btn btn-success rounded-pill px-3" type="button">
                <RouterLink class="text-decoration-none text-white" :to="{name:'addqdboard'}">등록하기</RouterLink>
            </button>
        </div>

       <QuestionBoardCard :questionboard="questionboard" :answers="answers" @item-click="itemClick"></QuestionBoardCard>

       <Pagination :pageInfo="pageInfo" @change-page="changePage"></Pagination>
    </div>
</template>

<script setup>
    import apiClient from '@/api';
    import { onMounted, reactive, ref, watch } from 'vue';
    import { onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router';
    import QuestionBoardCard from '@/components/questionboard/QuestionBoardCard.vue'
    import Pagination from '@/components/common/Pagination.vue';

    const questionboard = ref([]);
    const answers = ref({}); // 각 질문의 답변을 객체 형태로 저장
    const currentRoute = useRoute();
    const router = useRouter();

    const pageInfo = reactive({
        currentPage: parseInt(currentRoute.query.page) || 1,
        totalCount: 0,
        pageLimit: 5,
        listLimit: 10
    });

    const fetchQuestionBoard = async (page) => {
        try {
            const response = await apiClient.get(`/qboard?page=${page-1}&size=10`);
            questionboard.value = response.data.qboardResponseDto;
            pageInfo.totalCount = response.data.totalElements;
            pageInfo.listLimit = 10;
        } catch (error) {
            console.error("API 요청 중 오류 발생:", error);
        }
    };

    const fetchAnswers = async (qboardId) => {
        try {
            const response = await apiClient.get(`/qboard/${qboardId}/answers`);
            answers.value[qboardId] = response.data || [];
        } catch (error) {
            console.error(`답변 불러오기 실패 (qboardId: ${qboardId})`, error);
        }
    };

    const changePage = ({ page, totalPages }) => {
        if (page >= 1 && page <= totalPages) {
            router.push({name: 'questionboard', query: {page}});
        }
    };

    onBeforeRouteUpdate((to, from) => {
        pageInfo.currentPage = parseInt(to.query.page) || 1;
        fetchQuestionBoard(pageInfo.currentPage);
    });

    onMounted(async () => {
        await fetchQuestionBoard(pageInfo.currentPage);

        if (questionboard.value.length > 0) {
            await Promise.all(questionboard.value.map(q => fetchAnswers(q.qboardId)));
        }
    });

    watch(currentRoute, () => {
        pageInfo.currentPage = parseInt(currentRoute.query.page) || 1;
        fetchQuestionBoard(pageInfo.currentPage);
    });

    const itemClick = (id) => {
        console.log("클릭한 질문 번호:", id);
        if (!id) {
            console.error("잘못된 ID 값:", id);
            return;
        }
        router.push({ name: 'qboardDetail', params: { id } });
    };
</script>

<style scoped>
#header {
    width: 90%;
    margin: 0 auto;
}

.postbtn {
    margin: auto 0;
}
</style>
    