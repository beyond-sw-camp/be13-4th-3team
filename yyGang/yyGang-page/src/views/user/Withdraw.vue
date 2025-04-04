<template>
    <div class="pwd-modify">
      <h2 class="page-title">회원 탈퇴</h2>
      <div class="pwd-info">
        <!-- 비밀번호 입력 -->
        <h5 class="warning-title">
            <img src="https://yygang-bucket.s3.ap-northeast-2.amazonaws.com/free-icon-crying-face-18404984.png" style="width: 36px; height: auto;">
            진짜 가시려구요..?
        </h5>
        <p class="warning-text">
            비밀번호를 입력하시면 회원 탈퇴가 완료됩니다.<br>
            탈퇴 후 모든 개인정보는 삭제됩니다.<br>
            회원 탈퇴를 하시겠습니까? 
          </p>
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input type="password" class="form-control" id="password" v-model.trim="pwd.password" placeholder="비밀번호를 입력해 주세요." required />
        </div>
  
        <!-- 저장 버튼 -->
        <div class="form-actions">
          <button @click="deleteAccount" class="btn btn-primary" style="margin-right: 10px;">탈퇴하기</button>
          <button @click="cancelEdit" class="btn btn-secondary">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import apiClient from '@/api';
  import { useRouter } from 'vue-router';
  import { useAuthStore } from '@/stores/auth';
  
  const router = useRouter();
  const authStore = useAuthStore();
  
  // 회원 탈퇴 폼 데이터
  const pwd = ref({
    password: '',
  });
  
  // 회원 탈퇴 요청 함수
  const deleteAccount = async () => {
    console.log(pwd.value.password);
    if (!pwd.value.password) {
      alert('비밀번호를 입력해주세요.');
      return;
    }
  
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
      // 로그인 상태가 아닐 경우 로그인 페이지로 리다이렉트
      router.push('/login');
      return;
    }
  
    try {
    const response = await apiClient.get(
        '/user/my-page/verify-pwd',
        { params: { oldPassword: pwd.value.password } },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      if (!response.data) {
        alert('기존 비밀번호가 일치하지 않습니다.');
        return;
      }

      // 회원 탈퇴 API 호출
      const withdrawUser = await apiClient.delete(
        '/user/my-page/withdraw', 
        { params: { password: pwd.value.password } },
        {
            headers: {
            Authorization: `Bearer ${accessToken}`,
            },
        }
        );
  
      if (withdrawUser.status === 200) {
        // 탈퇴가 완료되면 로그아웃 처리
        alert('회원 탈퇴가 완료되었습니다.');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userInfo');

        // 로그인 상태를 변경한다.
        authStore.isLoggedIn = false;

        // 사용자 정보를 지운다.
        // userInfo.username = '';
        // userInfo.role = '';

        // 로그인 페이지로 리다이렉트
        router.push({name: 'login'});
      }
    } catch (error) {
      console.error(error);
      alert('회원 탈퇴에 실패했습니다. 다시 시도해주세요.');
    }
  };
  
  // 취소 버튼 클릭 시 이전 페이지로 돌아가기
  const cancelEdit = () => {
    router.go(-1);  // 또는 router.back()
  };

  </script>
  
  <style scoped>
  .pwd-modify {
    max-width: 600px;
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
  
  .pwd-info {
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  }
  .mb-3 {
    display: flex;
    flex-direction: column;
    padding: 10px 0;
    border-bottom: 1px solid #e0e0e0;
  }
  .form-label {
    font-weight: bold;
    color: #333;
    margin-bottom: 5px;
  }
  
  .form-control {
    padding: 15px;
    background-color: #f4f4f4;
    border-radius: 8px;
    width: 100%;
  }
  
  input.form-control {
    background-color: white;
  }
  
  input[type="password"] {
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
  }

  .withdrawal-message {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  font-family: 'Arial', sans-serif;
}

.warning-title {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  font-weight: bold;
  color:coral;
  margin-bottom: 20px;
}

.warning-title .icon {
  width: 40px;
  height: auto;
  margin-right: 10px;
}

.warning-text {
  font-size: 1.1rem;
  color: #333;
  line-height: 1.6;
  margin-bottom: 20px;
}

.warning-text br {
  margin-bottom: 10px;
}
  </style>
  