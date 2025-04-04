<template>
    <div class="container my-5">
      <div class="row">
        <!-- 상품 이미지 -->
        <div class="col-md-4 text-center">
          <img :src="nsupplement.productImage" alt="상품 이미지" class="img-fluid mb-3"/>
        </div>
  
        <!-- 상품 정보 -->
        <div class="col-md-8">
          <h4>{{ nsupplement.productName }}</h4>
          <div class="mb-2">
            <del class="text-muted">{{ nsupplement.price }}원</del>
            <strong class="text-danger ms-2">0%</strong>
            <span class="ms-2 fs-5">{{ nsupplement.price }}원</span>
          </div>
  
          <div class="mb-3 text-muted">
            <p>택배 배송 | <span class="text-success">무료 배송</span></p>
            <p class="small">제주/도서지역 추가 배송비 있음</p>
          </div>
  
          <!-- 수량 선택 및 총 금액 -->
          <div class="border p-3 rounded bg-light mb-3">
            <div class="d-flex justify-content-between align-items-center">
              <div>{{ nsupplement.productName }}</div>
              <div>
                <button class="btn btn-sm btn-outline-secondary me-1" @click="decreaseQty">-</button>
                <span>{{ quantity }}</span>
                <button class="btn btn-sm btn-outline-secondary ms-1" @click="increaseQty">+</button>
              </div>
              <div>{{ totalPrice.toLocaleString() }}원</div>
            </div>
          </div>
  
          <!-- 액션 버튼 -->
          <div class="d-flex gap-2 mb-4">
            <button class="btn btn-success w-100">
              <RouterLink class="nav-link" :to="{name:'order', query: { quantity: quantity } }">구매하기</RouterLink></button>
            <button class="btn btn-outline-dark" @click="goToCart">장바구니</button>
            <button class="btn btn-outline-dark">🤍</button>
          </div>
        </div>
      </div>
  
      <!-- 상세 정보, 리뷰, Q&A -->
      <ul class="nav nav-tabs mt-5">
        <li class="nav-item">
          <a class="nav-link" :class="{ active: activeTab === 'detail' }" href="#"
            @click.prevent="activeTab = 'detail'">상세 정보</a></li>
     
            <button
            type="button"
            class="btn"
            :class="activeTab === 'review' ? 'btn-primary' : 'btn-outline-primary'"
            @click="emit('review-click', nsupplement.productId)"
          
          >
            리뷰 {{ nsupplement.reviewCount }}
          </button>
        <button type="button" class="btn" :class="activeTab === 'qna' ? 'btn-primary' : 'btn-outline-primary'">
          Q&A </button>
      </ul>

<div class="mt-3">
  <!-- 상세 정보 -->
  <div v-if="activeTab === 'detail'">
    <div class="table-responsive">
      <table class="table table-bordered">
        <tbody>
          <tr>
            <th>상품번호</th>
            <td>{{ nsupplement.productId }}</td>
            <th>상품명</th>
            <td>{{ nsupplement.productName }}</td>
            <th>브랜드</th>
            <td>{{ nsupplement.brand }}</td>
          </tr>
          <tr>
            <th>주성분</th>
            <td>
                  <div class="ingredients-box">
                    <!-- 성분 박스들 -->
                    <span v-if="Array.isArray(nsupplement.ingredients) && nsupplement.ingredients.length > 0" v-for="(ingredient, index) in nsupplement.ingredients" :key="index" class="ingredient-box">
                      {{ getIngredientName(ingredient) }}
                    </span>
                    <span v-else class="ingredient-box">
                      정보 없음
                    </span>
                  </div>
                </td>
                <th>효능</th>
            <td colspan="3">
                  <div class="healthNames-box">
                    <!-- 건강기능 박스들 -->
                    <span v-if="Array.isArray(nsupplement.healthNames) && nsupplement.healthNames.length > 0" v-for="(healthName, index) in nsupplement.healthNames" :key="index" class="healthName-box">
                      {{ getIngredientName(healthName) }}
                    </span>
                    <span v-else class="healthName-box">
                      정보 없음
                    </span>
                  </div>
            </td>
          </tr>
          <tr>
            <th>주의사항</th>
              <td colspan="5">{{ nsupplement.caution }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- 리뷰 -->
  <div v-else-if="activeTab === 'review'">
    <p>리뷰</p>
    <!-- 리뷰 리스트가 있다면 여기서 v-for로 렌더링 -->
  </div>

  <!-- Q&A -->
  <div v-else-if="activeTab === 'qna'">
    <p>Q&A</p>
  </div>
</div>

</div>
</template>
  
  <script setup>
    import { ref, computed } from 'vue';
    import { useRouter } from 'vue-router';
    import apiClient from '@/api';

    const router = useRouter();

    const ingredientsOptions = [
    'VITAMIN_B3', 
    'VITAMIN_B1', 
    'MAGNESIUM', 
    'PROTEIN', 
    'LUTEIN'
  ];

  const emit = defineEmits(['review-click']);

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
  
    const props = defineProps({
      nsupplement: Object
    });

     // 장바구니 추가
     const goToCart = async () => {

    if (!props.nsupplement || !props.nsupplement.productId) {
    alert("상품 정보가 아직 로드되지 않았습니다.");
    return;
    } 

    const requestBody = {
    nSupplementId: props.nsupplement.productId,
    quantity: quantity.value
    };

    console.log("전송될 requestBody:", requestBody);

        try {
        await apiClient.post('/cart/nsupplement', requestBody);

        alert('장바구니에 상품이 추가되었습니다.');
        router.push({ name: 'cart' });
    } catch (error) {
        const { code, message } = error.response.data;
        if (code === 409 || code === 404 || code === 400) {
        alert(message);
        } else {
        alert('장바구니 추가 중 오류가 발생했습니다.');
        }
    }   
    };


    const activeTab = ref('detail');
    const quantity = ref(1);
    const increaseQty = () => quantity.value++;
    const decreaseQty = () => {
      if (quantity.value > 1) quantity.value--;
    };
  
  const totalPrice = computed(() => props.nsupplement.price * quantity.value);


  </script>
  
  <style scoped>
  img {
    max-width: 100%;
    height: auto;
  }

  .ingredients-box, .healthNames-box {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }

  .ingredient-box, .healthName-box {
    padding: 5px 10px;
    background-color: #F2FFF0;
    border-radius: 20px;
    border: 1px solid #ccc;
    font-size: 0.9rem;
  }

  .ingredient-box:hover, .healthName-box:hover {
    background-color: #e0e0e0;
  }
  
  </style>
  