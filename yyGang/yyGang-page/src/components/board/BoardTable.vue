<template>
    <table class="table">
        <thead>
            <tr>
                <th scope="col">번호</th>
                <th scope="col">제목</th>
                <th scope="col">작성자</th>
                <th scope="col">등록일</th>
            </tr>
        </thead>
        <tbody class="table-group-divider my-table">
            <tr v-for="board in boards" :key="board.id" @click.stop="emit('item-click', board.id)" id="boardItem">
                <th scope="row">{{ board.id }}</th>
                <td id="boardTitle">{{ board.title }}</td>
                <td>{{ board.userName }}</td>
                <td>{{ formatTime(board.createdAt) }}</td>
            </tr>
        </tbody>
    </table>
</template>

<script setup>

    const props = defineProps({
        boards:{
            type: Array,
            // required:true
        }
    });

    const formatTime = (dateString) => {
        const date = new Date(dateString);
  
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더해야 합니다.
        const day = String(date.getDate()).padStart(2, '0');
        
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    };

    const emit = defineEmits(['item-click'])


</script>

<style scoped>
    .table{
        /* text-align: center; */
        border-top: 1px solid #B4D1B6;
        padding: auto;
    }

    table > thead {
        text-align: center;
    }
    table tbody th, td {
        color: grey;
    }
    table tbody {
        color: #289631; /* 원하는 색상으로 변경 */
    }

    table tr{
        height: 50px;
        vertical-align: middle;
    }

    #boardItem{
        text-align: left;
    }
</style>