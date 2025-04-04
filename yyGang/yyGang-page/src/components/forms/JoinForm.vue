<template>
    <form @submit.prevent="submitClick" class="signup-form">
      <h3 class="page-title">회원 가입</h3>
      <div class="form-container">
        <div class="mb-3">
          <label for="email" class="form-label" >이메일</label>
          <div class="input-group">
            <input type="email" class="form-control" id="email" v-model.trim="formData.email" placeholder="이메일 중복 확인 먼저 해주세요." required>
            <button type="button" class="btn btn-secondary" @click="checkEmailDuplicate">이메일 중복 확인</button>
          </div>
          <small v-if="emailError" class="text-danger">
            <i class="bi bi-x-circle"></i> {{ emailError }}
          </small>
          <small v-if="isEmailChecked" class="text-success">
            <i class="bi bi-check-circle"></i> 이메일 중복 확인 완료
          </small>
        </div>

        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input type="password" class="form-control" id="userPwd" v-model="formData.userPwd" placeholder="영문, 숫자, 특수기호 조합" required>
        </div>

        <div class="mb-3">
          <label for="confirmPassword" class="form-label">비밀번호 확인</label>
          <input type="password" class="form-control" id="userPwd2" v-model="formData.userPwd2" placeholder="비밀번호를 다시 입력해 주세요." required>
        </div>

        <div class="mb-3">
          <label for="name" class="form-label">이름</label>
          <input type="text" class="form-control" id="name" v-model.trim="formData.name" required>
        </div>

        <div class="mb-3">
          <label for="dob" class="form-label">생년월일</label>
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
          <select class="form-select" id="gender" v-model="formData.gender" required>
            <option value="">성별을 선택해주세요</option>
            <option value="Male">남성</option>
            <option value="Female">여성</option>
          </select>
        </div>

        <div class="mb-3">
          <label for="phone" class="form-label">전화번호</label>
          <input type="text" class="form-control" id="phone" v-model.trim="formData.phone" placeholder="010-1234-5678" required>
        </div>

        <div class="mb-3">
          <label for="address" class="form-label">주소</label>
          <input type="text" class="form-control" id="address" v-model.trim="formData.address" placeholder="주소 입력" required>
        </div>

        <button type="submit" class="btn btn-primary" v-text="submitButtonText"></button>
      </div>
    </form>
</template>

<script setup>
  import { ref, reactive, toRaw, watch, computed } from 'vue';
  import apiClient from '@/api';
  
  const formData = reactive({});
  const emailError = ref(null); 
  const isEmailChecked = ref(false);
  
  const birthData = reactive({
    year: '',
    month: '',
    day: '',
  });
  
  const props = defineProps({
    submitButtonText: String,
    initFormData: Object
  });
  
  const emit = defineEmits(['form-submit']);
  
  const submitClick = () => {
    if(!isEmailChecked.value){
      alert('이메일 중복 확인을 먼저 진행해주세요.');
      return;
    }
    if (formData.userPwd !== formData.userPwd2) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    emit('form-submit', toRaw(formData));
  };
  
  watch(() => props.initFormData, (newFormData) => {
    Object.assign(formData, newFormData);
  }, { immediate: true });
  
  const checkEmailDuplicate = async () => {
    emailError.value = null;
  
    if (!formData.email) {
      return;
    }
  
    try {
      const response = await apiClient.get('/user/email/exists', { params: { email: formData.email }});
      if (response.data) {
        emailError.value = '이미 존재하는 이메일입니다.';
        isEmailChecked.value = false;
      } else {
        isEmailChecked.value = true;
      }
    } catch (error) {
      console.error('이메일 중복 확인 실패', error);
      emailError.value = '이메일 중복 확인에 실패했습니다.';
      isEmailChecked.value = false;
    }
  };
  
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
      formData.birthday = `${birthData.year}-${birthData.month}-${birthData.day}`;
    } else {
      formData.birthday = '';
    }
  }, { immediate: true });
</script>

<style scoped>
.signup-form {
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

.form-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.form-label {
  font-weight: bold;
  color: #555;
}

.input-group {
  display: flex;
  align-items: stretch;
}

.input-group .form-control {
  flex-grow: 1;
}

.input-group .btn {
  margin-left: 10px;
  height: 100%;
  padding: 10px 15px;
  font-size: 1rem;
}

.text-success i {
  margin-right: 5px;
}

button[type="submit"] {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 1.2rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

button[type="submit"]:hover {
  background-color: #0056b3;
}

.mb-3 {
  margin-bottom: 20px;
}

.form-select, .form-control {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.form-select:focus, .form-control:focus {
  border-color: #007bff;
  outline: none;
}

.text-danger {
  font-size: 0.9rem;
  color: #e74c3c;
}

/* 항목 사이에 선 추가 */
.mb-3 + .mb-3 {
  border-top: 1px solid #e0e0e0;
}

.mb-3 .form-label {
  margin-top: 10px; /* 제목과 항목 간 간격 추가 */
}
</style>
