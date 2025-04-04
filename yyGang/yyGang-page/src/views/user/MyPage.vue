<template>
  <div class="welcome-box d-flex justify-content-between align-items-center p-4 mb-5">
    <div>
      <p class="fw-bold mb-2">{{ userInfo.name }}님 환영합니다!</p>
      <p>오늘도 건강한 하루 보내세요!</p>
    </div>
      <img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/SupplementCharactor.png" alt="캐릭터" class="welcome-character" />
  </div>
  <div v-if="userData" class="user-page">
    <h2 class="page-title">마이 페이지</h2>

    <!-- 회원 정보 -->
    <div v-if="!isEditing" class="user-info">
      <div class="info-item">
        <strong class="info-label">이메일</strong>
        <div class="info-box"><p>{{ userData.email }}</p></div>
      </div>
      <div class="info-item">
        <strong class="info-label">이름</strong>
        <div class="info-box"><p>{{ userData.name }}</p></div>
      </div>
      <div class="info-item">
        <strong class="info-label">역할</strong>
        <div class="info-box"><p>{{ userData.role }}</p></div>
      </div>
      <div class="info-item">
        <strong class="info-label">생년월일</strong>
        <div class="info-box"><p>{{ userData.birthday }}</p></div>
      </div>
      <div class="info-item">
        <strong class="info-label">성별</strong>
        <div class="info-box"><p>{{ userData.gender }}</p></div>
      </div>
      <div class="info-item">
        <strong class="info-label">전화번호</strong>
        <div class="info-box"><p>{{ userData.phone }}</p></div>
      </div>
      <div class="info-item">
        <strong class="info-label">주소</strong>
        <div class="info-box"><p>{{ userData.address }}</p></div>
      </div>
      <br>
      <!-- 수정하기 버튼 -->
      <div class="form-actions">
        <button @click="isEditing = true" class="btn btn-primary">수정하기</button>
      </div>
    </div>

    <!-- 수정 폼 -->
    <div v-if="isEditing" class="user-info">
      <div class="info-item">
        <strong class="info-label">이메일</strong>
        <div class="info-box"><p>{{ userData.email }}</p></div>
      </div>

      <div class="mb-3">
          <label for="name" class="form-label">이름</label>
          <input type="text" class="form-control" id="name" v-model.trim="modifyForm.name" required>
      </div>

      <div class="mb-3">
        <label for="role" class="form-label">역할</label>
        <select class="form-select" id="role" v-model="modifyForm.role" required>
          <option value="">역할을 선택해주세요</option>
            <option value="CUSTOMER">일반회원</option>
            <option value="SELLER">판매자</option>
            <option value="PHARMACIST">약사</option>
        </select>
      </div>

      <div class="mb-3">
          <label for="birthday" class="form-label">생년월일</label>
          <div class="d-flex">
            <select class="form-select" v-model="birthData.year" required>
              <option value="">년</option>
              <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
            </select>
            <select class="form-select" v-model="birthData.month" required>
              <option value="">월</option>
              <option v-for="month in months" :key="month" :value="month">{{ month }}</option>
            </select>
            <select class="form-select" v-model="birthData.day" required>
              <option value="">일</option>
              <option v-for="day in days" :key="day" :value="day">{{ day }}</option>
            </select>
          </div>
        </div>

      <div class="mb-3">
        <label for="gender" class="form-label">성별</label>
        <select class="form-select" id="gender" v-model="modifyForm.gender" required>
          <option value="">성별을 선택해주세요</option>
            <option value="Male">남성</option>
            <option value="Female">여성</option>
        </select>
      </div>

      <div class="mb-3">
        <label for="phone" class="form-label">전화번호</label>
        <input type="text" class="form-control" id="phone" v-model.trim="modifyForm.phone" required>
      </div>

      <div class="mb-3">
        <label for="address" class="form-label">주소</label>
        <input type="text" class="form-control" id="address" v-model.trim="modifyForm.address" required>
      </div>

      <!-- 저장 버튼 -->
      <div class="form-actions">
        <button @click="updateUserInfo" class="btn btn-primary" style="margin-right: 10px;">저장하기</button>
        <button @click="isEditing = false" class="btn btn-secondary">취소</button>
      </div>
    </div>
  </div>

  <div v-else class="loading-message">
    <p>로딩 중...</p>
  </div>
</template>


<script setup>
import { ref, onMounted, reactive, watch, computed  } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/api';
import { useAuthStore } from '@/stores/auth';

const userData = ref(null);
const router = useRouter();
const isEditing = ref(false);  // 수정 상태 관리
const authStore = useAuthStore();
const userInfo = authStore.userInfo;

const modifyForm = reactive({
        name: '',
        role: '',
        birthday: '',
        gender: '',
        phone: '',
        address: ''
    });


const birthData = reactive({
    year: '',
    month: '',
    day: '',
  });

  const years = computed(() => {
    const currentYear = new Date().getFullYear();
    let yearArray = [];
    for (let i = 1900; i <= currentYear; i++) {
      yearArray.push(i);
    }
    return yearArray;
  });
  
  const months = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
  
  const days = computed(() => {
    const month = parseInt(birthData.month);
    let dayArray = [];
    if (month > 0) {
      const daysInMonth = new Date(birthData.year, month, 0).getDate();
      for (let i = 1; i <= daysInMonth; i++) {
        dayArray.push(i < 10 ? `0${i}` : i);
      }
    }
    return dayArray;
  });
  
  watch(() => [birthData.year, birthData.month, birthData.day], () => {
    if (birthData.year && birthData.month && birthData.day) {
      modifyForm.birthday = `${birthData.year}-${birthData.month}-${birthData.day}`;
    } else {
      modifyForm.birthday = '';
    }
  }, { immediate: true });



const fetchUserData = async () => {
  const accessToken = localStorage.getItem('accessToken');
  if (!accessToken) {
    // 토큰이 없는 경우 -> 로그인 페이지로 이동하도록
    router.push('/login');
  }

  try {
    // 토큰 담아서 전송
    const response = await apiClient.get('/user/my-page', {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    userData.value = response.data;

    console.log(response.data);
    console.log(response);
    console.log(response.name);

    modifyForm.name = response.data.name;
    modifyForm.role = response.data.role;
    modifyForm.birthday = response.data.birthday;
    modifyForm.gender = response.data.gender;
    modifyForm.phone = response.data.phone;
    modifyForm.address = response.data.address;

    console.log(modifyForm);
    console.log(modifyForm.data);
    console.log(modifyForm.value);
    console.log(modifyForm.name);

  } catch (error) {
    alert('에러가 발생했습니다.');
  }
};

// 사용자 정보를 수정하는 함수
const updateUserInfo = async () => {
  const accessToken = localStorage.getItem('accessToken');
  if (!accessToken) {
    // 사용자 토큰 먼저 확인 -> 있어? 오케이 허락!
    router.push('/login');
  }

  try {
    console.log(modifyForm);
    // 수정된 정보를 서버로 전송
    const response = await apiClient.post(
      '/user/my-page', 
      modifyForm, 
      {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    alert('회원 정보가 성공적으로 수정되었습니다.');
    // 수정된 정보를 userData에 반영
    userData.value = { ...userData.value, ...modifyForm };

    authStore.userInfo.name = userData.value.name;

    isEditing.value = false;  // 수정이 완료되면 수정 상태 종료
  } catch (error) {
    alert('수정에 실패했습니다.');
    console.error('Error updating user info:', error);
  }
};

onMounted(() => {
  fetchUserData();
});

</script>

<style scoped>
.user-page {
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

.user-info {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.info-item, .mb-3 {
  display: flex;
  flex-direction: column;
  padding: 10px 0;
  border-bottom: 1px solid #e0e0e0; /* 각 항목 사이에 선 추가 */
}

.info-item:last-child {
  border-bottom: none;
}

.info-label, .form-label {
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.info-box, .form-control, .form-select {
  padding: 15px;
  background-color: #f4f4f4;
  border-radius: 8px;
  width: 100%;
}

.info-box p {
  margin: 0;
  font-size: 1rem;
  color: #666;
}

.loading-message {
  text-align: center;
  font-size: 1.2rem;
  color: #777;
}

input.form-control, select.form-select {
  background-color: white;
}

.welcome-box {
  background-color: #ffffff; /* 흰색 배경 */
  border-radius: 15px; /* 둥근 모서리 */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 효과 */
  padding: 30px 40px; /* 여백 넉넉하게 */
  max-width: 800px; /* 최대 너비 제한 */
  width: 100%; /* 가로 폭 100% */
  margin: 0 auto; /* 가로 중앙 정렬 */
  transition: all 0.3s ease-in-out; /* 마우스를 올렸을 때의 애니메이션 효과 */
}

.welcome-box:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2); /* 마우스 올리면 그림자 강조 */
  transform: translateY(-5px); /* 약간 떠 있는 효과 */
}

.welcome-name {
  font-size: 1.5rem; /* 글꼴 크기 조정 */
  color: #333; /* 어두운 색상 */
  font-weight: bold;
}

.welcome-message {
  font-size: 1.1rem; /* 메시지 크기 */
  color: #666; /* 조금 더 부드러운 회색 */
  margin-top: 5px;
}

.welcome-character {
  width: 80px; /* 캐릭터 이미지 크기 조정 */
  height: auto;
  margin-left: 20px; /* 텍스트와 이미지 간 간격 */
  transition: transform 0.3s ease-in-out; /* 이미지 애니메이션 효과 */
}

.welcome-character:hover {
  transform: scale(1.1); /* 마우스 올리면 이미지 확대 */
}

</style>
