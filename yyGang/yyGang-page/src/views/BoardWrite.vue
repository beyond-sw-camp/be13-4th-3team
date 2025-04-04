<template>
    <main>
        <h2 id="boardTitle" class="titleLine">게시판 작성</h2>
        <BoardForm @form-submit="formSubmit" submit-button-text="등록" id="boardContent"/>
    </main>
</template>

<script setup>
    import apiClient from '@/api';
    import BoardForm from '@/components/board/BoardForm.vue';
    import { useRouter } from 'vue-router';

    const router = useRouter();

    const formSubmit= async(formData)=>{
        try {
            const response = await apiClient.post(`/board`,formData);

            if(response.status === 201){
                alert(`정상적으로 등록`);

                router.push({name:'board'});
            }
        } catch (error) {
            if(error.response.status===400){
                alert('500자이내작성')
            }
            
        }
    }

</script>


<style scoped>
#boardTitle{
    padding-bottom: 3%;
}
.titleLine{
    border-bottom: 2px solid rgb(51, 116, 51);
}
#boardContent{
    margin-top: 3%;
}
</style>