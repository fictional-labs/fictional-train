<template>
  <div class="home-container">

    <div v-if="store.isLogin" class="logged-in-container">
      <div class="user-info">현재 접속자: <span class="username">{{ store.loginUser }}</span></div>
      <button class="compact-logout-btn" @click="handleLogout">로그아웃</button>
    </div>

    <!-- 로그인 뷰 -->
    <div v-else class="login-scene">
      
      <!-- 중앙 로그인 영역 -->
      <div class="login-wrapper" @click="handleLogin">
        <img src="@/asset/login.png" alt="Login Board" class="login-board-img" />
        
        <div class="login-inputs">
          <input 
            type="text" 
            v-model="loginObj.username" 
            placeholder="______" 
            class="custom-input"
            @click.stop
          />
          <input 
            type="password" 
            v-model="loginObj.password" 
            placeholder="______" 
            class="custom-input password-input"
            @keyup.enter="handleLogin"
            @click.stop
          />
        </div>
        
        <div class="ticket-hover-hint">
          정보를 입력하고 티켓을 누르세요
        </div>
      </div>
      <!-- 중앙 로그인 영역 끝 -->

    </div>
    <!-- 로그인 뷰 끝 -->

  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore';

const store = useUserStore();

// 로그인 로직
const loginObj = ref({
  username: '',
  password: ''
});

const handleLogin = async () => {
  if (!loginObj.value.username || !loginObj.value.password) {
    alert("아이디와 비밀번호를 모두 입력해주세요.");
    return;
  }
  try {
    await store.login(loginObj.value);
  } catch (error) {
    alert("로그인 중 오류가 발생했습니다.");
  }
};

// 로그아웃 로직
const handleLogout = () => {
  store.logout();
  loginObj.value.username = '';
  loginObj.value.password = '';
};
</script>

<style scoped>
.home-container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
  background: url('@/asset/background.png') no-repeat center center;
  background-size: cover;
}

.home-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(0, 0, 0, 0.4) 0%, rgba(0, 0, 0, 0.95) 80%);
  z-index: 1;
  pointer-events: none;
}

/* 로그인 성공 시 레이아웃 */
.logged-in-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
  color: white;
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 15px;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5); /* 가독성을 위한 검은 반투명 배경 */
  padding: 30px 50px;
  border-radius: 10px;
  backdrop-filter: blur(5px);
}

.user-info {
  font-size: 1.5rem;
  font-weight: bold;
}

.username {
  color: #f39c12; /* 닉네임 강조 색상 */
}

.compact-logout-btn {
  padding: 8px 20px;
  font-size: 0.9rem;
  background-color: transparent;
  border: 1px solid rgba(255, 255, 255, 0.6);
  color: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.compact-logout-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
  border-color: white;
  color: white;
}

/* 로그인 뷰 레이아웃 */
.login-scene {
  width: 100%;
  height: 100%;
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 로그인 보드 (중앙) */
.login-wrapper {
  position: relative;
  width: 500px; /* 이미지 크기에 따라 조정 */
  cursor: pointer;
  transition: transform 0.2s;
}

.login-wrapper:hover {
  transform: scale(1.02);
}

.login-board-img {
  width: 100%;
  display: block;
  /* 효과 초기 상태 */
  filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.6));
  animation: dramatic-glow 2s infinite alternate;
}

@keyframes dramatic-glow {
  0% {
    filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.6));
  }
  100% {
    filter: drop-shadow(0 0 25px rgba(255, 255, 255, 1)) 
            drop-shadow(0 0 40px rgba(200, 200, 255, 0.8));
  }
}

.login-inputs {
  position: absolute;
  top: 53.7%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  gap: 16.7px; /* 입력값 사이 간격 */
  width: 30%; /* 이미지에 대한 너비 */
}

.custom-input {
  padding: 10px;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  background: transparent;
  text-align: center;
}

.password-input {
  margin-top: 25px;
}

.ticket-hover-hint {
  position: absolute;
  top: -40px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.9);
  padding: 8px 15px;
  border-radius: 20px;
  font-weight: bold;
  opacity: 0;
  transition: opacity 0.3s;
  pointer-events: none;
  white-space: nowrap;
  color: #333;
}

.login-wrapper:hover .ticket-hover-hint {
  opacity: 1;
}
</style>
