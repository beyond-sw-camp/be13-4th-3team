<template>
    <main>
        <div id="mainArea">
            <div id="boardArea">
                <div>
                    <h3 style="font-weight:bold">{{ board.title }}</h3>
                </div>
                <div id="boardInfo">
                    <div>작성자: {{ board.userName }}   |   작성일: {{ formatTime(board.createdAt) }}</div>
                    <div>
                        <!-- <font-awesome-icon :icon="['fas', 'heart']" :style="{color: activeColor}" @click="heartClick()"/> -->
                        <!-- <i class="bi bi-heart-fill color-red" ></i> {{ likeCount }}
                        <i class="bi bi-heart" id="heart"  :style="{backgroundColor : activeColor}" @click="heartClick()" ></i> -->

                        <i v-if="isLiked" class="bi bi-heart-fill" style="color: red;" @click="heartClick()"></i>
                        <i v-else class="bi bi-heart" @click="heartClick()"></i>
                        <i>{{ likeCount }}</i>
                        <!-- <i v-else class="bi bi-heartc" id="heart" @click="heartClick()"></i> view 0개 -->
                    </div>
                </div>
                <div id="boardContent">
                    {{board.content}}
                </div>
            </div>
            <div id="btnArea">
                <button class="btn btn-success" v-if="isLoggedIn && userInfo.username === board.userEmail" @click="editBtnClick(board.id)">수정하기</button>
                <button class="btn btn-success" v-if="isLoggedIn && userInfo.username === board.userEmail" @click="delBtnClick(board.id)">삭제</button>
                <button @click="btnClick" class="btn btn-success">뒤로가기</button>
            </div>
            <div class="mt-2 p-3 bg-body rounded shadow-sm">
                <div id="h2Div">
                    <h2 class="fs-6">댓글 작성</h2>
                    <h2 class="fs-6">댓글 수: {{comment.length }}</h2>
                </div>

                <input class="form-control" type="text" v-model.trim="newComment">
                <div class="form-text">존중과 배려하는 마음으로 건강한 댓글 문화를 함께 만들어가요.</div>

                <div class="d-flex justify-content-end">
                    <button class="btn btn-success" @click="addComment(comment.id)">등록</button>
                </div>
                
                <div id="commentArea">
                    <div v-for="cmt in comment" :key="cmt.id" class="comment-item">
                        <div class="comment-header">
                            <span @click="toggleReplyInput(cmt.id)" class="comment-info">
                                <span class="comment-user">{{ cmt.userId }}</span> | 
                                <span class="comment-date">{{ formatTime(cmt.createdAt) }}</span>
                            </span><br>
                            <strong class="comment-content">{{ cmt.content }}</strong>
                        </div>
                        
                        <!-- 대댓글 입력 폼 -->
                        <div v-if="replyTarget === cmt.id" class="reply-form">
                            <input class="form-control" type="text" v-model.trim="replyComment" @keyup.enter="addComment(cmt.id)" placeholder="답글을 입력하세요...">
                            <button class="btn btn-sm btn-outline-success mt-1" @click="addComment(cmt.id)">답글 등록</button>
                        </div>
                        
                        <!-- 대댓글 목록 -->
                        <div v-for="x in cmt.childComments" :key="x.id" class="child-comment">
                            <div class="child-comment-header">
                                <span class="comment-user">{{ x.userId }}</span> | 
                                <span class="comment-date">{{ formatTime(x.createdAt) }}</span>
                            </div>
                            <strong class="child-comment-content">{{ x.content }}</strong>
                        </div>
                    </div>
                </div>

            </div>

        </div>

    </main>
</template>

<script setup>
    import apiClient from '@/api';
    import { reactive, toRaw } from 'vue';
    import { onMounted, ref } from 'vue'; 
    import { useRoute, useRouter } from 'vue-router';
    import { useAuthStore } from '@/stores/auth';
    
    const authStore = useAuthStore();
    const isLoggedIn = authStore.isLoggedIn;
    const userInfo = authStore.userInfo;
    const router = useRouter();
    const currentRoute = useRoute();
    const board = reactive({});
    const comment = ref([]);
    const newComment = ref(""); // 새 댓글 입력 필드
    const replyComment = ref(""); // 대댓글 입력 필드
    const replyTarget = ref(null); // 대댓글 입력 대상
    const isLiked = ref(false);
    const activeColor = ref("white");
    const likeCount = ref(0);

    const userEmail = localStorage.getItem('userInfo');

    // 좋아요 개수 가져오기
    const fetchLikeInfo = async(id)=>{
        try {
            const response = await apiClient.get(`/board/${id}/likes`);
            
            likeCount.value = response.data;
            

        } catch (error) {
            console.error("좋아요 개수 불러오기 실패", error);
            
        }
    };

    //   // 좋아요 초기 세팅
    //   const fetchBoardLike = async(id)=>{
    //     console.log("좋아ㅛㅇ");
        
    //     try {
    //         const response = await apiClient.get(`/like/${id}`);

            
    //         // 행이 있는 경우(좋아요 눌러진 상태)
    //         if(response.data){
    //             activeColor.value = 'red';
    //         }
            
    //     } catch (error) {
    //         // alert(error.response.status);
    //     }
    // };
    //   // 좋아요 세팅
    //   const heartClick = async()=>{
    //     try {
    //         const response = await apiClient.post(`/like/${currentRoute.params.id}`);
    //         if (response && response.data) {
    //             activeColor.value = response.data === '좋아요 등록' ? 'red' : 'white';
    //             await fetchLikeInfo(currentRoute.params.id); // 좋아요 개수 다시 불러오기
    //         } else {
    //             throw new Error("서버 응답이 없습니다.");
    //         }
    //     } catch (error) {
    //         console.error("좋아요 처리 오류", error);
    //         alert("좋아요 처리 중 오류가 발생했습니다.");
    //     }
            
    // };

    const fetchBoardLike = async (id) => {
        try {
            const response = await apiClient.get(`/like/${id}`);
            isLiked.value = !!response.data; // 값이 있으면 true (좋아요 상태)
        } catch (error) {
            console.error("좋아요 정보 불러오기 실패", error);
        }
    };
    const heartClick = async () => {
        try {
            const response = await apiClient.post(`/like/${currentRoute.params.id}`);
            if (response && response.data) {
                isLiked.value = response.data === '좋아요 등록'; // 상태 변경
                await fetchLikeInfo(currentRoute.params.id); // 좋아요 개수 다시 불러오기
            } else {
                throw new Error("서버 응답이 없습니다.");
            }
        } catch (error) {
            console.error("좋아요 처리 오류", error);
            alert("좋아요 처리 중 오류가 발생했습니다.");
        }
    };


    // 게시글 정보 가져오기
    const fetchBoard = async(id)=>{
        try {
            const response = await apiClient.get(`/board/${id}`);
            
            Object.assign(board, response.data);
            
        } catch (error) {
            
        }
    };

    // 댓글 정보 가져오기
    const fetchComment = async(id)=>{
        try {
            const response1 = await apiClient.get(`/board/${id}/comment`);

            comment.value = await response1.data;

        } catch (error) {
            
        }

    };

    

    // 부모 댓글 클릭 시 대댓글 입력 필드 토글
    const toggleReplyInput = (parentId) => {
        replyTarget.value = replyTarget.value === parentId ? null : parentId;
        replyComment.value = ""; // 입력 필드 초기화
    };


    // 댓글 추가 기능
    const addComment = async (parentId) => {

        const content = parentId ? replyComment.value : newComment.value;

        if (!content.trim()) {
            return alert("댓글을 입력하세요!");
        }


        try {
            const response = await apiClient.post(`/board/${board.id}/comment`, 
                { content: content.trim() }, 
                { params: parentId ? { parentId } : {} } // parentId가 있을 경우만 추가
            );


            if (response.status === 201) {
                 // 댓글 목록 다시 불러오기
                await fetchComment(board.id);

                 // 입력 필드 초기화
            if (parentId) {
                replyComment.value = "";
                replyTarget.value = null;
            } else {
                newComment.value = "";
            }

            }
        } catch (error) {
            alert(`댓글을 추가하는 중 오류가 발생했습니다.`);
        }
    };


    // 게시글 삭제
    const delBtnClick = async(id)=>{
        try {

            if(confirm("삭제하시겠습니까?")){
                const response = await apiClient.delete(`/board/${id}`); 
                
                if(response.status === 200){
                    alert(`삭제되었습니다.`);
                    router.push({name:'board'});
                }
            }else{
                router.back();
            }

            
        } catch (error) {
            if (error.response.data.code === 403) {
                alert(`삭제 권한이 없습니다`);
            }else if(error.response.data.code === 404){
                alert("게시글 삭제 중 오류가 발생했습니다.");
                router.push({name:'departments'});
            }else{
                alert(`게시글 삭제 오류", ${error}`);
            }
        }
    };
    const btnClick = ()=>{
        router.push({name:'board'});
    };

    // 수정
    const editBtnClick = (id)=>{
        router.push({name:'board/edit/id', params:{id}});
    };

    // 날짜 
    const formatTime = (dateString) => {
        const date = new Date(dateString);
  
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더해야 합니다.
        const day = String(date.getDate()).padStart(2, '0');
        
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    };
    
  

    onMounted(function(){
        fetchBoard(currentRoute.params.id);
        fetchComment(currentRoute.params.id);
        // 좋아요 정보 초기세팅
        fetchBoardLike(currentRoute.params.id);

        // fetchLikeInfo(currentRoute.params.id);
    });
    
</script>

<style scoped>
#mainArea{
    margin-top: 5%;
    display: grid;
    grid-template-rows: 2fr 0.2fr 3fr;

}

#boardArea{
    padding: 5%;
    /* height: 500px; */
    border: 1px solid #ddd;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    border-top-left-radius: 20px;
    border-bottom-right-radius: 20px;
}

#boardInfo{
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    color: gray;
    height: 50px;
    margin-bottom: 0%;
    border-bottom: 2px solid rgb(51, 116, 51);
}
#boardContent{
    margin-top: 3%;
}

#btnArea{
    margin-top: 3%;
    display: flex;
    flex-direction: row;
    justify-content:flex-end;
    gap: 10px;
}

.bi-heart-fill::before{
    color: red;
}
#h2Div{
    display: flex;
    flex-direction: row;
    justify-content:space-between;
}

#commentArea {
    margin-top: 20px;
    padding: 10px;
}

.comment-item {
    padding: 10px;
    margin-bottom: 10px;
    background-color: #f9f9f9;
    border-radius: 5px;
    border: 1px solid #ddd;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.comment-header {
    font-size: 14px;
    margin-bottom: 8px;
    color: #333;
}

.comment-info {
    cursor: pointer;
    font-size: 12px;
    color: gray;
}

.comment-user {
    font-weight: bold;
}

.comment-date {
    font-size: 12px;
    color: #888;
}

.comment-content {
    font-size: 16px;
    line-height: 1.5;
}

.reply-form {
    margin-top: 10px;
    padding-left: 20px;
    font-size: 14px;
}

.reply-form input {
    width: 100%;
    max-width: 400px;
}

.child-comment {
    margin-top: 10px;
    padding-left: 20px;
    background-color: #f1f1f1;
    border-left: 3px solid #5db26c;
    margin-bottom: 10px;
}

.child-comment-header {
    font-size: 12px;
    color: #555;
}

.child-comment-content {
    font-size: 14px;
    color: #444;
    line-height: 1.4;
}


</style>