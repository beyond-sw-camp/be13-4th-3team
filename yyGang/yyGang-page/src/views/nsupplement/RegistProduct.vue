<template>
    <div>
        <RegistProductForm submit-button-text="등록" :init-regist-data="initRegistData"
        @form-submit="formSubmit"/>
        <!-- <Pagination :pageInfo="pageInfo" 
            @change-page="changePage"/> -->
    </div>
</template>

<script setup>
import RegistProductForm from '@/components/forms/RegistProductForm.vue';
import { reactive } from 'vue';
import apiClient from '@/api';
import { useRouter } from 'vue-router';
import Pagination from '@/components/common/Pagination.vue';

const router = useRouter();

const initRegistData = reactive({
        productName: '',
        caution: '',
        brand: '',
        price: '',
        stockQuantity: '',
        ingredients: [],
        hfunctionalItems: [],
        productImage: ''
    });

// 비동기 통신
const formSubmit = async (formData) => {
    const accessToken = localStorage.getItem('accessToken');

    try{

        const response = await apiClient.post(
            '/nsupplement',
            formData,
            {
                headers: { 
                    'Authorization': `Bearer ${accessToken}` 
                }
            }
        );

        if(response.status === 200){
            alert('정상적으로 등록되었습니다.');

                // 정상적으로 등록이 완료된 경우, 회원 상세 정보 조회로 이동시키도록
            router.replace({name: 'registProductList'});   // 상품 등록 리스트로 이동
        }
        } catch(error){
            alert(error);
            alert('에러가 발생했습니다.')
        }
    };



</script>

<style scoped>
.regist-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 1.6rem;
  font-weight: bold;
  letter-spacing: 2px;
  margin-bottom: 30px;
  margin-left: 3%;
}

.regist-form {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.form-item {
  display: flex;
  flex-direction: column;
  padding: 10px 0;
  border-bottom: 1px solid #e0e0e0;
}

.form-item:last-child {
  border-bottom: none;
}

.form-label {
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.form-control, .form-select {
  padding: 15px;
  background-color: #f4f4f4;
  border-radius: 8px;
  width: 100%;
  margin-top: 5px;
}

.form-control:focus, .form-select:focus {
  background-color: white;
  box-shadow: 0 0 5px rgba(0, 100, 255, 0.5); /* Focus effect */
}

.form-control {
  background-color: white;
}

.submit-btn {
  margin-top: 20px;
  padding: 12px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  width: 100%;
  transition: background-color 0.3s ease;
}

.submit-btn:hover {
  background-color: #0056b3;
}

.submit-btn:active {
  background-color: #004085;
}
</style>