<template>
    <table class="table table-striped table-hover text-center">
        <thead>
            <tr>
                <th><input type="checkbox" v-model="allSelected" @change="toggleSelectAll" /></th> <!-- 전체 선택/해제 체크박스 -->
                <th>상품 Id</th>
                <th>상품 사진</th>
                <th>상품명</th>
                <th>브랜드</th>
                <th>주의사항</th>
                <th>가격</th>
                <th>재고</th>
                <th>성분</th>
                <th>건강기능</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="nsupplement in nsupplements" :key="nsupplement.productId">
                <td>
                    <input type="checkbox" v-model="selectedItems" :value="nsupplement.productId" />
                </td>
                <td>{{ nsupplement.productId }}</td>
                <td v-if="!nsupplement.isEditing">
                    <img :src="nsupplement.productImage" alt="상품 이미지" style="max-width: 100px; max-height: 100px;">
                </td>
                <td v-else>
                    <input type="file" class="form-control" id="productImage" @change="handleImageUpload(nsupplement, $event)" accept="image/*">
                    <div v-if="nsupplement.productImage" class="mt-2">
                        <img :src="nsupplement.productImage" alt="상품 이미지 미리보기" class="img-thumbnail" style="max-width: 200px;">
                        <button type="button" class="btn btn-danger mt-2" @click="deleteImage(nsupplement)">이미지 삭제</button>
                    </div>
                </td>
                
                <td v-if="!nsupplement.isEditing">{{ nsupplement.productName }}</td>
                <td v-else>
                    <input type="text" v-model="nsupplement.productName" />
                </td>

                <td v-if="!nsupplement.isEditing">{{ nsupplement.brand }}</td>
                <td v-else>
                    <input type="text" v-model="nsupplement.brand" />
                </td>

                <td v-if="!nsupplement.isEditing">{{ nsupplement.caution }}</td>
                <td v-else>
                    <input type="text" v-model="nsupplement.caution" />
                </td>

                <td v-if="!nsupplement.isEditing">{{ nsupplement.price }}</td>
                <td v-else>
                    <input type="number" v-model="nsupplement.price" />
                </td>

                <td v-if="!nsupplement.isEditing">{{ nsupplement.stockQuantity }}</td>
                <td v-else>
                    <input type="number" v-model="nsupplement.stockQuantity" />
                </td>
                
                <td v-if="!nsupplement.isEditing">
                    {{ nsupplement.ingredients.map(getIngredientName).join(', ') }}</td>
                <td v-else>
                    <div v-for="(ingredient, index) in ingredientsOptions" :key="index">
                        <input type="checkbox" class="form-check-input" id="ingredients" :value="ingredient" v-model="nsupplement.ingredients">
                        <label class="form-check-label" :for="ingredient">{{ getIngredientName(ingredient) }}</label>
                    </div>
                </td> 

                <td v-if="!nsupplement.isEditing">
                    {{ nsupplement.healthNames.map(getFunctionalName).join(', ') }}</td>
                    <td v-else>
                    <div v-for="(healthName, index) in hFunctionalOptions" :key="index">
                        <input type="checkbox" class="form-check-input" id="healthNames" :value="healthName" v-model="nsupplement.healthNames">
                        <label class="form-check-label" :for="healthName">{{ getIngredientName(healthName) }}</label>
                    </div>
                </td> 

                <td v-if="!nsupplement.isEditing">
                    <button @click="editItem(nsupplement)">수정</button>
                </td>
                <td v-else>
                    <button @click="saveItem(nsupplement)">저장</button>
                    <button @click="cancel(nsupplement)">취소</button>
                </td>
            </tr>
        </tbody>
    </table>
    <button type="button" class="btn btn-outline-danger" @click="deleteSelectedItems">선택된 상품 삭제</button>
</template>

<script setup>
import { ref } from 'vue';
import apiClient from '@/api';

const props = defineProps({
    nsupplements: {
        type: Array,
        required: true
    }
});

const emit = defineEmits(['delete-item']);
    
const selectedItems = ref([]);
const allSelected = ref(false);

// 전체 선택/해제 기능
const toggleSelectAll = () => {
    if (allSelected.value) {
        selectedItems.value = nsupplements.map(item => item.productId); // 모든 상품 선택
    } else {
        selectedItems.value = []; // 모든 선택 해제
    }
};

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

// 선택된 상품 삭제
const deleteSelectedItems = () => {
    if (selectedItems.value.length === 0) {
        alert('삭제할 상품을 선택하세요.');
        return;
    }
    emit('delete-item', selectedItems.value);
};

// 수정할 상품 선택
const editItem = (item) => {
    item.isEditing = true;
    item.originalData = { ...item }; // 기존 데이터 임시 저장
};

// 수정 저장
const saveItem = async (item) => {
    let uploadedImageUrl = item.productImage; // 기존 이미지를 기본값으로 설정

    try {
        // 이미지가 변경된 경우만 서버로 업로드
        if (item.newImageFile) {
            const uploadedImage = await uploadImageToServer(item.newImageFile);
            uploadedImageUrl = uploadedImage || uploadedImageUrl; // 서버에서 반환된 이미지 URL
        }

        // 이미지가 삭제된 경우, 서버에 null 전송
        if (item.productImage === '') {
            uploadedImageUrl = null;
        }

        const ingredients = item.ingredients || [];
        const healthNames = item.healthNames || [];

        // API 호출로 수정된 데이터 저장
        const response = await apiClient.post(`/nsupplement/${item.productId}`, {
            productImage: uploadedImageUrl,
            productName: item.productName,
            brand: item.brand,
            caution: item.caution,
            price: item.price,
            stockQuantity: item.stockQuantity,
            ingredients: ingredients,
            hfunctionalItems: healthNames
        });

        if (response.status === 200) {
            item.isEditing = false;
            delete item.originalData; // 원본 데이터 삭제
            delete item.newImageFile; // 새로운 이미지 파일 삭제
            alert('상품이 성공적으로 수정되었습니다.');
        }
    } catch (error) {
        console.error(error);
        alert('상품 수정에 실패했습니다.');
    }
};

// 수정 취소
const cancel = (item) => {
    Object.assign(item, item.originalData); // 원본 데이터로 복구
    item.isEditing = false;
    delete item.originalData; // 원본 데이터 삭제
    delete item.newImageFile; // 새로운 이미지 파일 삭제
};

// 이미지 삭제 처리
const deleteImage = (nsupplement) => {
    nsupplement.productImage = ''; // 이미지 초기화
    nsupplement.newImageFile = null; // 새로 업로드된 이미지 파일 초기화
};

// 이미지 업로드 처리
const handleImageUpload = (nsupplement, event) => {
    const file = event.target.files[0];
    if (file) {
        const tmpimageUrl = URL.createObjectURL(file);
        nsupplement.productImage = tmpimageUrl; // 로컬 미리보기 제공
        nsupplement.newImageFile = file; // 새로운 이미지 파일 저장
    }
};

// 이미지 업로드 (서버 처리)
const uploadImageToServer = async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    try {
        const uploadResponse = await apiClient.post('/s3/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return uploadResponse.data; // 서버에서 반환된 이미지 URL
    } catch (error) {
        console.error('Error uploading image:', error);
        alert('이미지 업로드에 실패했습니다.');
        return null;
    }
};
</script>

<style scoped>
/* Table Styling */
.table {
    font-size: 1rem;
    border-collapse: collapse;
    margin-bottom: 30px;
}

.table th, .table td {
    padding: 12px;
    text-align: center;
    vertical-align: middle;
    border: 1px solid #ddd;
}

.table th {
    background-color: #f8f9fa;
    font-weight: bold;
}

.table tbody tr:hover {
    background-color: #e9ecef;
}

/* Checkbox */
input[type="checkbox"] {
    cursor: pointer;
}

/* Product Image Styling */
.product-image {
    max-width: 100px;
    max-height: 100px;
    object-fit: cover;
    border-radius: 8px;
}

/* Buttons */
button {
    font-size: 0.9rem;
    padding: 8px 16px;
    border-radius: 5px;
    transition: background-color 0.3s, transform 0.3s;
    cursor: pointer;
}

button:hover {
    background-color: #0056b3;
    color: white;
    transform: scale(1.05);
}

.btn-outline-danger {
    border-color: #dc3545;
    color: #dc3545;
}

.btn-outline-danger:hover {
    background-color: #dc3545;
    color: white;
}

.btn-primary {
    background-color: #007bff;
    border-color: #007bff;
    color: white;
}

</style>