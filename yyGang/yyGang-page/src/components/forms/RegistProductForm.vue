<template>
  <form @submit.prevent="submitClick" class="regist-form">
    <h3 class="page-title">상품 등록</h3>
    <div class="form-container">
      <div class="regist-info">

      <!-- 상품명 -->
      <div class="mb-3">
        <label for="productName" class="form-label">상품명</label>
        <input type="text" class="form-control" id="productName" v-model.trim="formData.productName" required>
      </div>

      <!-- 브랜드명 -->
      <div class="mb-3">
        <label for="brand" class="form-label">브랜드명</label>
        <input type="text" class="form-control" id="brand" v-model.trim="formData.brand" required>
      </div>

      <!-- 주의사항 -->
      <div class="mb-3">
        <label for="caution" class="form-label">주의사항</label>
        <input type="text" class="form-control" id="caution" v-model.trim="formData.caution" required>
      </div>

      <!-- 상품 가격 -->
      <div class="mb-3">
        <label for="price" class="form-label">상품 가격</label>
        <input type="number" class="form-control" id="price" v-model.trim="formData.price" required>
      </div>

      <!-- 재고 수량 -->
      <div class="mb-3">
        <label for="stockQuantity" class="form-label">재고 수량</label>
        <input type="number" class="form-control" id="stockQuantity" v-model.trim="formData.stockQuantity" required>
      </div>

      <!-- 성분 선택 -->
      <div class="mb-3">
        <label for="ingredients" class="form-label">성분</label>
        <div class="form-check" v-for="(ingredient, index) in ingredientsOptions" :key="index">
          <input type="checkbox" class="form-check-input" id="ingredients" :value="ingredient" v-model="formData.ingredients">
          <label class="form-check-label" :for="ingredient">{{ getIngredientName(ingredient) }}</label>
        </div>
      </div>

      <!-- 건강 기능 선택 -->
      <div class="mb-3">
        <label for="hfunctionalItems" class="form-label">건강 기능</label>
        <div class="form-check" v-for="(hfunctiontitem, index) in hFunctionalOptions" :key="index">
          <input type="checkbox" class="form-check-input" :id="'hfunctionalItems-' + index" :value="hfunctiontitem" v-model="formData.hfunctionalItems">
          <label class="form-check-label" :for="'hfunctionalItems-' + index">{{ getFunctionalName(hfunctiontitem) }}</label>
        </div>
      </div>

      <!-- 상품 이미지 등록 -->
      <div class="mb-3">
        <label for="productImage" class="form-label">상품 이미지</label>
        <input type="file" class="form-control" id="productImage" @change="handleImageUpload" accept="image/*" ref="fileInput">
        <!-- 선택된 이미지 미리보기 -->
        <div v-if="formData.productImage" class="mt-2">
          <img :src="formData.productImage" alt="상품 이미지 미리보기" class="img-thumbnail" style="max-width: 200px;">
          <button type="button" class="btn btn-danger mt-2" @click="deleteImage">이미지 삭제</button>
        </div>
      </div>

      <button type="submit" class="btn btn-primary" v-text="submitButtonText"></button>
    </div>
  </div>
  </form>
</template>

<script setup>
  import { reactive, watch, ref } from 'vue';
  import apiClient from '@/api';

  const fileInput = ref(null);  // ref로 fileInput을 정의

  // 폼 데이터
  const formData = reactive({
    productName: '',
    caution: '',
    brand: '',
    price: '',
    stockQuantity: '',
    ingredients: [],  // 빈 배열로 초기화
    hfunctionalItems: [],  // 빈 배열로 초기화
    productImage: ''
  });

  const props = defineProps({
    submitButtonText: String,
    initRegistData: Object
  });

  const ingredientsOptions = [
    'VITAMIN_B3', 
    'VITAMIN_B1', 
    'MAGNESIUM', 
    'PROTEIN', 
    'LUTEIN'
  ];

  // 성분 한글로 변환
  const getIngredientName = (ingredient) => {
    switch(ingredient) {
      case 'VITAMIN_B3': return '비타민 B3';
      case 'VITAMIN_B1': return '비타민 B1';
      case 'MAGNESIUM': return '마그네슘';
      case 'PROTEIN': return '단백질';
      case 'LUTEIN': return '루테인';
      default: return ingredient;
    }
  };

  const hFunctionalOptions = [
    'GUT_HEALTH', 
    'EYE', 
    'FATIGUE', 
    'SKIN_HEALTH', 
    'LIVER', 
    'SLEEP', 
    'JOINT_HEALTH', 
    'FITNESS', 
    'BODY_FAT'
  ];

  // 건강 기능 한글로 변환
  const getFunctionalName = (hfunctiontitem) => {
    switch(hfunctiontitem) {
      case 'GUT_HEALTH': return '장 건강';
      case 'EYE': return '눈 건강';
      case 'FATIGUE': return '피로 회복';
      case 'SKIN_HEALTH': return '피부 건강';
      case 'LIVER': return '간 건강';
      case 'SLEEP': return '수면';
      case 'JOINT_HEALTH': return '관절 건강';
      case 'FITNESS': return '운동';
      case 'BODY_FAT': return '체지방';
      default: return hfunctiontitem;
    }
  };

  const emit = defineEmits(['form-submit']);

  // formData 초기화
  watch(() => props.initRegistData, (newFormData) => {
    Object.assign(formData, newFormData);
  }, { immediate: true });

  // 이미지 업로드 처리
  const handleImageUpload = async (event) => {
    console.log(event);
    console.log(event.target.files[0]);
    const file = event.target.files[0];
    if (file) {
      const tmpimageUrl = URL.createObjectURL(file);
      console.log(tmpimageUrl);
      formData.productImage = tmpimageUrl;  // 로컬 미리보기 먼저 제공
      // const imageUrl = await uploadImageToServer(file);
      // formData.productImage = imageUrl;  // 업로드된 이미지 URL을 formData에 저장
    }
  };

  // 이미지 삭제 처리
  const deleteImage = () => {
    formData.productImage = '';  // 이미지 초기화
    fileInput.value = '';  // file input의 값 초기화
  };

  // 이미지 업로드
  const uploadImageToServer = async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    try {
      const uploadResponse = await apiClient.post('/s3/upload', 
      formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      const data = uploadResponse.data;
      return data; // S3에서 반환된 URL
    } catch (error) {
      console.error('Error uploading image:', error);
      alert('에러가 발생했습니다.');
      return;
    }
  };

  // 제출 처리
  const submitClick = async () => {

    if (!formData.productImage) {
      alert("이미지를 선택해주세요.");
      return;
    }

    if(formData.productImage){
      // 사진 파일이 업로드 된 경우,
      const file = fileInput.value.files[0]; 
      console.log(file)
      console.log(fileInput);
      console.log(fileInput.value);
      console.log(fileInput.value.files[0]);
      const imageUrl = await uploadImageToServer(file);
      formData.productImage = imageUrl; // formData에 다시 저장
    }
    console.log(formData); 
    emit('form-submit', formData);
  };
</script>


<style scoped>
.regist-form {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  background-color: #D9D9D9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.regist-info {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}


.page-title {
  font-size: 1.6rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 30px;
}

.form-container {
  display: flex;
  flex-direction: column;
}

.mb-3 {
  margin-bottom: 1.5rem;
}

.form-label {
  font-weight: bold;
  color: #333;
}

.form-control {
  padding: 12px;
  background-color: #f4f4f4;
  border-radius: 8px;
  width: 100%;
  border: 1px solid #ddd;
  margin-top: 5px;
}

.form-control:focus {
  background-color: #fff;
  box-shadow: 0 0 5px rgba(0, 100, 255, 0.5);
}

.form-check {
  margin-top: 10px;
}

.form-check-input {
  margin-right: 8px;
}

.form-check-label {
  font-size: 1rem;
  color: #555;
}

.img-thumbnail {
  max-width: 100%;
  border-radius: 8px;
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

.btn-danger {
  margin-top: 10px;
  background-color: #dc3545;
  color: white;
  border-radius: 5px;
  border: none;
}

.btn-danger:hover {
  background-color: #c82333;
}

input.form-control, select.form-select {
  background-color: white;
}
</style>
