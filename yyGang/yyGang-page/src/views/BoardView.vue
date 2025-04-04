<template>
    <main>
        <div id="mainArea">
            <div id="div1">
                <h2 id="boardTitle">자유 게시판</h2>
                <button type="button" class="btn btn-success" @click="btnClick" >등록하기</button>
            </div>
            <BoardTable :boards="boards" @item-click="itemClick"/>
            
            <!-- <RouterLink class="nav-link" :to="{name:`board/write`}"><button type="button" class="btn btn-success">등록하기</button></RouterLink> -->

            <Pagination :pageInfo="pageInfo"
            @change-page="changePage"/>
        </div>             
    </main>
</template>

<script setup>
    import apiClient from '@/api';
    import BoardTable from '@/components/board/BoardTable.vue';
    import { onMounted, ref, reactive, watch } from 'vue'; 
    import { useRoute, useRouter } from 'vue-router';
    import Pagination from '@/components/common/Pagination.vue';
    
    const boards = ref([]);
    const router = useRouter();
    const currentRoute = useRoute();
    const pageInfo = reactive({
        // 값을 정수로 변환하고 실패하면 1을 기본값으로 사용
        currentPage: parseInt(currentRoute.query.page) || 1,
        totalCount: 0, // 전체 데이터 수
        pageLimit: 5, // 페이지네이션에 보이는 페이지의 수
        listLimit: 0 // 한 페이지의 표시될 리스트의 수 
    });

    
    
    const fetchBoards = async(page)=>{
        
        try {
            const response = await apiClient.get(`/board?page=${page-1}&size=10`);
            
            boards.value = response.data.content; // 'content'가 있으면 사용
            pageInfo.totalCount = response.data.totalElements; // 전체 데이터 개수 설정
            pageInfo.listLimit = 10; // 한 페이지에 표시된 게시물 수 (response.data.content.length)


        } catch (error) {
            console.error("Error fetching boards:", error);
        }
    };

    const itemClick = (id)=>{
        router.push({name:'board/id', params:{id}});
    };
    
    const changePage = ({page,totalPages})=>{
       if(page >=1 && page <= totalPages){
            router.push({name:'board', query:{page}});
       }
    };

    const btnClick = ()=>{
        router.push({name:'board/write'})
    }

    watch(currentRoute, ()=>{
        pageInfo.currentPage = parseInt(currentRoute.query.page) || 1;

        fetchBoards(pageInfo.currentPage); // URL에서 페이지 번호를 가져와서 boards 데이터를 다시 불러옴
        
    });

    
    onMounted(function(){
        // fetchBoards();
        fetchBoards(pageInfo.currentPage); // 초기 페이지 로드 시 데이터를 불러옴
    });


</script>

<style scoped>

    #mainArea{
        margin-top: 5%;
        height: 70%;
    }
    #boardTitle{
        font-weight: bold;
        color: #3D8D7A;
        padding-bottom: 10px;
    }
    #div1{
        padding-bottom: 10px;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: end;
    }   

    #div1 button{
        padding: 5px 10px;
    }
</style>