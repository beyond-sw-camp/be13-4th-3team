<template>
    <div v-if="userData" class="user-page">
      <h2 class="page-title">회원 정보 수정</h2>
      
      <div class="user-info">
        <div class="info-item">
          <strong class="info-label">이메일</strong>
          <div class="info-box"><p>{{userData.email}}</p></div>
        </div>
        <div class="info-item">
          <strong class="info-label">이름</strong>
          <div class="info-box">
            <input type="text" v-model="userData.name" class="form-input" />
          </div>
        </div>
        <div class="info-item">
          <strong class="info-label">역할</strong>
          <div class="info-box">
            <input type="text" v-model="userData.role" class="form-input" />
          </div>
        </div>
        <div class="info-item">
          <strong class="info-label">생년월일</strong>
          <div class="info-box">
            <input type="date" v-model="userData.birthday" class="form-input" />
          </div>
        </div>
        <div class="info-item">
          <strong class="info-label">성별</strong>
          <div class="info-box">
            <select v-model="userData.gender" class="form-input">
              <option value="Male">남성</option>
              <option value="Female">여성</option>
            </select>
          </div>
        </div>
        <div class="info-item">
          <strong class="info-label">전화번호</strong>
          <div class="info-box">
            <input type="text" v-model="userData.phone" class="form-input" />
          </div>
        </div>
        <div class="info-item">
          <strong class="info-label">주소</strong>
          <div class="info-box">
            <input type="text" v-model="userData.address" class="form-input" />
          </div>
        </div>
      </div>
  
      <div class="form-actions">
        <button @click="updateUserInfo" class="btn btn-primary">정보 수정</button>
      </div>
    </div>
  
    <div v-else class="loading-message">
      <p>로딩 중...</p>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import apiClient from '@/api';
  
  const userData = ref(null);
  const router = useRouter();
  
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
    } catch (error) {
      alert('에러가 발생했습니다.');
    }
  };
  
  // 사용자 정보를 수정하는 함수
  const updateUserInfo = async () => {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        // 토큰이 없으면 로그인 페이지로 이동하도록
      router.push('/login');
    }
  
    try {
      // 수정된 정보를 서버로 전송
      const response = await apiClient.post('/user/my-page', userData.value, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      
      alert('회원 정보가 성공적으로 수정되었습니다.');
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
  
  .info-item {
    display: flex;
    flex-direction: column;
    padding: 10px 0;
    border-bottom: 1px solid #e0e0e0; /* 각 항목 사이에 선 추가 */
  }
  
  .info-item:last-child {
    border-bottom: none;
  }
  
  .info-label {
    font-weight: bold;
    color: #333;
    margin-bottom: 5px;
  }
  
  .info-box {
    padding: 15px;
    background-color: #f4f4f4;
    border-radius: 8px;
    width: 100%;
  }
  
  .form-input {
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
    width: 100%;
  }
  
  .form-input:focus {
    border-color: #007bff;
    outline: none;
  }
  
  .form-actions {
    text-align: right;
    margin-top: 20px;
  }
  
  .btn-primary {
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .btn-primary:hover {
    background-color: #0056b3;
  }
  
  .loading-message {
    text-align: center;
    font-size: 1.2rem;
    color: #777;
  }
  </style>
  