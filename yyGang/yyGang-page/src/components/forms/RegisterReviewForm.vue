<template>
  <div class="review-wrapper">
    <!-- 안내 영역 -->
    <div class="review-header">
      <h4>리뷰 쓰기</h4>
      <p class="title">상품은 어떠셨나요?</p>
      <p class="desc">
        저희 제품을 구매해주셔서 감사합니다. 상품이 마음에 드셨다면, 소중한 리뷰를 남겨주세요! 😊<br />
        <span class="warn">상품 리뷰와 관련 없는 비방, 욕설, 광고 게시글 등은 삭제될 수 있습니다.</span>
      </p>
    </div>

    <!-- 입력 영역 -->
    <div class="review-input">
      <textarea
        v-model="formData.content"
        :maxlength="maxLength"
        placeholder="내용을 입력해주세요"
      ></textarea>
      <div class="text-count">{{ formData.content.length }}/{{ maxLength }}자</div>
    </div>

    <!-- 버튼 영역 -->
    <div class="btn-group">
      <button class="btn cancel" @click="cancel">취소</button>
      <button class="btn submit" :disabled="!formData.content.trim()" @click="submitClick" v-text="submitButtonText"></button>
      <button class="btn delete" v-if="submitButtonText === '수정'" @click="deleteClick">삭제</button>
    </div>
  </div>
</template>

<script setup>
import apiClient from '@/api';
import { reactive, ref, toRaw, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const currentRoute = useRoute();
    const router = useRouter();
const maxLength = 1000;
const formData = reactive({
  content: ''
});

    const props = defineProps({
        submitButtonText: String,
        initFormData: Object
    });

const cancel = () => {
  router.push({ name: 'nsupplement/nSupplementId/review', params: {nSupplementId: currentRoute.params.nSupplementId}});
};

const deleteClick = async () => {
  const confirmDelete = confirm('정말로 이 리뷰를 삭제하시겠습니까?');
  if (!confirmDelete) return;

  try {
    const response = await apiClient.delete(
      `/nsupplement/${currentRoute.params.nSupplementId}/review`
    );

    if (response.status === 200) {
      alert('리뷰가 삭제되었습니다.');
      router.push({ 
        name: 'nsupplement/nSupplementId/review',
        params: { nSupplementId: currentRoute.params.nSupplementId }
      });
    }
  } catch (error) {
    console.error(error);
    alert('리뷰 삭제에 실패했습니다.');
  }
};

const emit = defineEmits(['form-submit']);

const submitClick = () => {
  emit('form-submit', toRaw(formData));
};

    watch(
        // () => props.initFormData,
        props.initFormData,
        (newFormData) => {
            Object.assign(formData, newFormData);
        },
        {immediate: true} 
    );


</script>

<style scoped>
.review-wrapper {
  background: #fdfcf7;
  border: 1px solid #e6e6e6;
  padding: 24px;
  border-radius: 4px;
  font-family: 'Noto Sans KR', sans-serif;
}

.review-header {
  border-bottom: 1px solid #4a9d47;
  padding-bottom: 12px;
  margin-bottom: 16px;
}

.review-header h4 {
  font-size: 16px;
  color: #444;
  margin-bottom: 4px;
}

.review-header .title {
  font-weight: 600;
  margin-bottom: 8px;
  font-size: 18px;
  color: #333;
}

.review-header .desc {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
}

.review-header .warn {
  color: #999;
  font-size: 13px;
}

.review-input {
  border: 2px solid #4a9d47;
  padding: 8px;
  border-radius: 4px;
  background: white;
}

.review-input textarea {
  width: 100%;
  height: 150px;
  border: none;
  outline: none;
  font-size: 14px;
  resize: none;
}

.text-count {
  text-align: right;
  font-size: 13px;
  color: #888;
  margin-top: 4px;
}

.btn-group {
  margin-top: 16px;
  display: flex;
  justify-content: center;
  gap: 12px;
}

.btn {
  padding: 10px 24px;
  font-size: 14px;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
}

.btn.cancel {
  background: #eee;
  color: #333;
  border: none;
}

.btn.submit {
  background: #c5f100;
  color: #000;
  border: none;
}

.btn.submit:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.btn.delete {
  background: #f33;
  color: #fff;
  border: none;
}
</style>
