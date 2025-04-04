<template>
    <div class="pwd-modify">
      <h2 class="page-title">비밀번호 변경</h2>
      <div class="pwd-info">
        <!-- 기존 비밀번호 입력 -->
        <div class="mb-3">
          <label for="oldPassword" class="form-label">기존 비밀번호</label>
          <input type="password" class="form-control" id="oldPassword" v-model.trim="modifyForm.oldPassword" placeholder="기존 비밀번호를 입력해주세요." required>
        </div>
  
        <!-- 새 비밀번호 입력 -->
        <div class="mb-3">
          <label for="newPassword" class="form-label">새 비밀번호</label>
          <input type="password" class="form-control" id="newPassword" v-model.trim="modifyForm.newPassword" placeholder="새 비밀번호를 입력해 주세요." required>
          
          <label for="newPasswordConfirm" class="form-label">새 비밀번호 확인</label>
          <input type="password" class="form-control" id="newPasswordConfirm" v-model.trim="modifyForm.newPasswordConfirm" placeholder="비밀번호를 다시 입력해 주세요." required>
        </div>
  
        <!-- 저장 버튼 -->
        <div class="form-actions">
          <button @click="updatePassword" class="btn btn-primary" style="margin-right: 10px;">저장하기</button>
          <button @click="cancelEdit" class="btn btn-secondary">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue';
  import apiClient from '@/api';
  import { useRouter } from 'vue-router';
  import { useAuthStore } from '@/stores/auth';
  
  const router = useRouter();
  const authStore = useAuthStore();
  
  // 비밀번호 변경 폼 데이터
  const modifyForm = reactive({
    oldPassword: '',
    newPassword: '',
    newPasswordConfirm: '',
  });
  
  // 비밀번호 변경 요청 함수
  const updatePassword = async () => {

    if (!modifyForm.oldPassword) {
      alert('기존 비밀번호를 입력해주세요.');
      return;
    }
  
    // 기존 비밀번호 확인 API 호출
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
      // 로그인 상태가 아닐 경우 로그인 페이지로 리다이렉트
      router.push('/login');
      return;
    }
    try {
      const response = await apiClient.get(
        '/user/my-page/verify-pwd',
        { params: { oldPassword: modifyForm.oldPassword } },
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
  
      // 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
      if (modifyForm.newPassword !== modifyForm.newPasswordConfirm) {
        alert('새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.');
        return;
      }
  
      // 비밀번호 변경 API 호출
      const changePasswordResponse = await apiClient.post(
        '/user/my-page/pass-word',
        modifyForm,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

  
      if (changePasswordResponse.status === 200) {
        alert('비밀번호가 성공적으로 변경되었습니다.');
        alert('다시 로그인해주세요.');
        authStore.logout();
      }
    } catch (error) {
      console.error(error);
      alert('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
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
  /* border-bottom: 1px solid #e0e0e0; */ /* border-bottom 제거 */
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
  margin-bottom: 5px; /* 간격 줄이기 */
  border-radius: 5px;
  border: 1px solid #ccc;
}

.mb-3 + .mb-3 {
  border-top: 1px solid #ccc;
  padding-top: 10px;
  margin-top: 10px;
}

.form-actions {
  display: flex;
  justify-content: flex-start;
  gap: 10px;
}
</style>