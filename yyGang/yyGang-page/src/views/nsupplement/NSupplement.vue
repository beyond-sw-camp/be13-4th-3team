<template>
    <main>
        <div class="text-center my-3">
            <input
                v-model="searchKeyword"
                @keyup.enter="onSearch"
                class="form-control w-50 mx-auto"
                placeholder="상품명을 입력하세요"
            />
            <button class="btn btn-primary" @click="onSearch">검색</button>
            <!-- 정렬 옵션 -->
            <select v-model="sortType" class="form-select w-auto" @change="onSearch">
                <option value="">기본정렬</option>
                <option value="PRICE_ASC">가격낮은순</option>
                <option value="PRICE_DESC">가격높은순</option>
                <option value="NAME_ASC">이름순↓</option>
                <option value="NAME_DESC">이름순↑</option>
                <option value="REVIEW_ASC">리뷰순↓</option>
                <option value="REVIEW_DESC">리뷰순↑</option>
            </select>
    
            <!-- 초기화 버튼 -->
            <button class="btn btn-outline-secondary" @click="resetSearch">초기화</button>
        </div>

        <!-- 건강 카테고리 -->
<!-- 건강 카테고리 -->
<div class="mb-4">
  <h5 class="mb-2">건강 카테고리</h5>
  <div class="d-flex flex-wrap">
    <button
      v-for="tag in healthTags"
      :key="tag.id"
      :class="['btn btn-tag', { 'selected-health': selectedHealthIds.includes(tag.id) }]"
      @click="toggleHealthId(tag.id)"
    >
    # {{ healthNameMap[tag.name] || tag.name }}
    </button>
  </div>
</div>


<!-- 성분 카테고리 -->
<div class="mb-4">
  <h5 class="mb-2">성분 카테고리</h5>
  <div class="d-flex flex-wrap">
    <button
      v-for="tag in ingredientTags"
      :key="tag.id"
      :class="['btn btn-tag', { 'selected-ingredient': selectedingredientIds.includes(tag.id) }]"
      @click="toggleIngredientId(tag.id)"
    >
    # {{ ingredientNameMap[tag.name] || tag.name }}
    </button>
  </div>
</div>


        <NSupplementTable :nsupplements="nsupplements" :pageInfo="pageInfo" @item-click="itemClick"/>
        <Pagination :pageInfo="pageInfo" 
            @change-page="changePage"/>
    </main>
</template>

<script setup>
    import apiClient from '@/api';
    import { onMounted, reactive, ref, watch } from 'vue';
    import { onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router';
    import Pagination from '@/components/common/Pagination.vue';
    import NSupplementTable from '@/components/tables/NSupplementTable.vue';

    const nsupplements = ref([]);
    const currentRoute = useRoute();
    const router = useRouter();
    const searchKeyword = ref(currentRoute.query.productName || '');
    const sortType = ref(currentRoute.query.sortType || '');
    const healthTags = ref([]);
    const ingredientTags = ref([]);
    const selectedHealthIds = ref([]);
    const selectedingredientIds = ref([]);
    
    const pageInfo = reactive({
        currentPage: parseInt(currentRoute.query.page) || 1,
        totalElements: 0, // 전체 데이터 수
        pageLimit: 5, // 페이지네이션에 보이는 페이지의 수
        listLimit: 0 // 한 페이지에 표시될 리스트의 수
    });
    

    const healthNameMap = {
  GUT_HEALTH: "장 건강",
  EYE: "눈 건강",
  FATIGUE: "피로 개선",
  SKIN_HEALTH: "피부 건강",
  LIVER: "간 건강",
  SLEEP: "수면 개선",
  JOINT_HEALTH: "관절 건강",
  FITNESS: "체력 증진",
  BODY_FAT: "체지방 감소"
}

const ingredientNameMap = {
  LUTEIN: "루테인",
  MAGNESIUM: "마그네슘",
  PROTEIN: "단백질",
  VITAMIN_B1: "비타민 B1",
  VITAMIN_B3: "비타민 B3"
}

    const fetchNSupplement = async (page) => {
        try {
            // 이렇게 page -1 하는 것이 맞을까?
            const response = await apiClient.get('/nsupplement/info/search', {
                params: {
                    productName: searchKeyword.value || undefined,
                    sortType: sortType.value || undefined,
                    healthIds: selectedHealthIds.value.length > 0 ? selectedHealthIds.value : undefined,
                    ingredientIds: selectedingredientIds.value.length > 0 ? selectedingredientIds.value : undefined,
                    page: page - 1,
                    size: 10
                },
                paramsSerializer: params => {
                    // axios가 배열을 healthIds=1&healthIds=2 형태로 보내도록 설정
                    const searchParams = new URLSearchParams();
                    for (const key in params) {
                        if (Array.isArray(params[key])) {
                            params[key].forEach(val => searchParams.append(key, val));
                        } else if (params[key] !== undefined) {
                        searchParams.append(key, params[key]);
                        }
                    }
                    return searchParams.toString();
                }
            });
            
            console.log(response);
            nsupplements.value = response.data.content;
            pageInfo.totalElements = response.data.totalElements;
            pageInfo.listLimit = 10;
        } catch (error) {
            
            console.log(error);
        }
    };

    const fetchHealthTags = async () => {
    try {
        const response = await apiClient.get('/hfuncitems')
        
        const map = response.data.healthMap;

        // 객체를 [{ id: 1, name: '비타민' }, ...] 형태로 변환
        healthTags.value = Object.entries(map).map(([id, name]) => ({
        id: Number(id),
        name
        }))
    } catch (error) {
        console.log(error);
        
    }
    };

    const fetchIngredientTags = async () => {
    try {
        const response = await apiClient.get('/ingredients')
        
        const map = response.data.ingredientMap;

        // 객체를 [{ id: 1, name: '비타민' }, ...] 형태로 변환
        ingredientTags.value = Object.entries(map).map(([id, name]) => ({
        id: Number(id),
        name
        }))
    } catch (error) {
        console.log(error);
        
    }
    };

    const changePage = ({page, totalPages}) => {
        
        if (page >= 1 && page <= totalPages) {
            router.push({name: 'nsupplement', query: {page /* , productName: searchKeyword.value || undefined */ }});
        }
    };

    const itemClick = (productId) => {
        router.push({name: 'nsupplement/productId', params: {productId}});
    };

    const onSearch = () => {
        router.push({
            name: 'nsupplement',
            query: {
                page: 1,
                productName: searchKeyword.value || undefined,
                sortType: sortType.value || undefined,
                healthIds: selectedHealthIds.value.length > 0 ? selectedHealthIds.value : undefined,
                ingredientIds: selectedingredientIds.value.length > 0 ? selectedingredientIds.value : undefined
            }
        });
    };

    const resetSearch = () => {
        searchKeyword.value = '';
        sortType.value = '';
        selectedHealthIds.value = [];
        selectedingredientIds.value = [];

        router.push({ name: 'nsupplement', query: { page: 1 } });
    };

    const toggleHealthId = (id) => {
        const index = selectedHealthIds.value.indexOf(id);
        
        if (index === -1) {
            selectedHealthIds.value.push(id); // 선택 추가
        } else {
            selectedHealthIds.value.splice(index, 1); // 선택 해제
        }

        onSearch();
        
    };

    const toggleIngredientId = (id) => {
        const index = selectedingredientIds.value.indexOf(id);
        
        if (index === -1) {
            selectedingredientIds.value.push(id); 
        } else {
            selectedingredientIds.value.splice(index, 1); 
        }

        onSearch();
        
    };

    onBeforeRouteUpdate((to, form) => {
        pageInfo.currentPage = parseInt(to.query.page) || 1;
        // searchKeyword.value = to.query.productName || '';
        // sortType.value = to.query.sortType || '';

//         const ids = to.query.healthIds;
//   if (Array.isArray(ids)) {
//     selectedHealthIds.value = ids.map(Number);
//   } else if (ids) {
//     selectedHealthIds.value = [Number(ids)];
//   } else {
//     selectedHealthIds.value = [];
//   }

        fetchNSupplement(pageInfo.currentPage);
    });

    onMounted(() => {
        // searchKeyword.value = currentRoute.query.productName || '';
        // sortType.value = currentRoute.query.sortType || '';
        // pageInfo.currentPage = parseInt(currentRoute.query.page) || 1;

//         const ids = currentRoute.query.healthIds;
//   if (Array.isArray(ids)) {
//     selectedHealthIds.value = ids.map(Number);
//   } else if (ids) {
//     selectedHealthIds.value = [Number(ids)];
//   } else {
//     selectedHealthIds.value = [];
//   }

        fetchHealthTags();
        fetchIngredientTags();

        fetchNSupplement(pageInfo.currentPage);
    });
</script>

<style scoped>

h5 {
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.btn-tag {
  border-radius: 20px;
  border: 1px solid #ccc;
  background-color: #fff;
  padding: 5px 15px;
  margin: 3px;
  font-size: 0.9rem;
  transition: background-color 0.2s, color 0.2s;
}
.btn-tag:hover {
  background-color: #f0f0f0;
}

.btn-tag.selected-health {
  background-color: #198754; 
  color: #fff;
  border-color: #198754;
}

.btn-tag.selected-ingredient {
  background-color: #fd7e14; 
  color: #fff;
  border-color: #fd7e14;
}

</style>