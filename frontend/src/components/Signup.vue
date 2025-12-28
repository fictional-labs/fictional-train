<script setup>
import { ref } from 'vue';
import axios from 'axios';
import closedImg from '@/asset/closed.png';
import openImg from '@/asset/open.png';
import letterImg from '@/asset/letter.png';

const isOpen = ref(false);
const isHovered = ref(false);
const step = ref('GREETING');

const formData = ref({
  name: '',
  password: '',
  passwordRe: ''
});

const toggleAssistant = () => {
  if (isOpen.value) {
    isOpen.value = false;
    setTimeout(() => resetFlow(), 300); 
  } else {
    isOpen.value = true;
    step.value = 'GREETING';
  }
};

const resetFlow = () => {
  step.value = 'GREETING';
  formData.value = { name: '', password: '', passwordRe: '' };
};

const nextStep = (next) => {
  step.value = next;
};

const submitName = () => {
  if (formData.value.name.trim()) {
    nextStep('CONFIRM_NAME');
  }
};

const confirmName = (isCorrect) => {
  if (isCorrect) {
    nextStep('ASK_PASSWORD');
  } else {
    nextStep('ASK_NAME');
  }
};

const submitPassword = () => {
  if (formData.value.password.length > 0) {
    nextStep('CONFIRM_PASSWORD');
  }
};

const checkPassword = async () => {
  if (formData.value.password === formData.value.passwordRe) {
    try {
      // API 호출
      await axios.post('/api/users', {
        username: formData.value.name,
        password: formData.value.password
      });

      nextStep('SUCCESS');
      console.log("Signup Success:", formData.value);
      
      setTimeout(() => {
          toggleAssistant();
      }, 2500);

    } catch (error) {
      console.error("Signup Failed:", error);
      if (error.response?.status === 409) {
        alert("이미 존재하는 사용자입니다.");
      } else {
        alert("회원가입 실패 (다시 시도해주세요)");
      }
    }
    
  } else {
    nextStep('PASSWORD_MISMATCH');
  }
};

const retryPassword = () => {
    formData.value.password = '';
    formData.value.passwordRe = '';
    nextStep('ASK_PASSWORD');
};

</script>

<template>
  <div class="signup-assistant-root">
    
    <transition name="fade">
      <div v-if="isOpen" class="overlay" @click="toggleAssistant"></div>
    </transition>

    <transition name="slide-up">
      <div v-if="isOpen" class="assistant-content">
         
         <div class="character-container">
           <img :src="letterImg" alt="Letter Character" class="character-img floating-anim" />
         </div>

         <div class="dialog-box">
            
            <transition name="fade-content" mode="out-in">
              
              <div v-if="step === 'GREETING'" class="step-container" key="greeting">
                <p class="dialog-text">안녕하세요! 회원가입을 진행하시겠습니까?</p>
                <div class="btn-group">
                    <button @click="nextStep('ASK_NAME')" class="mystic-btn primary">시작하기</button>
                    <button @click="toggleAssistant" class="mystic-btn secondary">돌아가기</button>
                </div>
              </div>

              <div v-else-if="step === 'ASK_NAME'" class="step-container" key="ask-name">
                <p class="dialog-text">이름을 입력해주세요.</p>
                <div class="input-group">
                    <input 
                    v-model="formData.name" 
                    type="text" 
                    placeholder="이름" 
                    @keyup.enter="submitName"
                    class="mystic-input"
                    autofocus
                    />
                    <button @click="submitName" class="icon-btn">➜</button>
                </div>
              </div>

              <div v-else-if="step === 'CONFIRM_NAME'" class="step-container" key="confirm-name">
                <p class="dialog-text"><strong>{{ formData.name }}</strong>... 이 이름이 맞나요?</p>
                <div class="btn-group">
                    <button @click="confirmName(true)" class="mystic-btn primary">네, 맞아요</button>
                    <button @click="confirmName(false)" class="mystic-btn secondary">아니오</button>
                </div>
              </div>

              <div v-else-if="step === 'ASK_PASSWORD'" class="step-container" key="ask-pw">
                <p class="dialog-text">사용할 비밀번호를 입력해주세요.</p>
                <div class="input-group">
                    <input 
                    v-model="formData.password" 
                    type="password" 
                    placeholder="비밀번호" 
                    @keyup.enter="submitPassword"
                    class="mystic-input"
                    />
                    <button @click="submitPassword" class="icon-btn">➜</button>
                </div>
              </div>

              <div v-else-if="step === 'CONFIRM_PASSWORD'" class="step-container" key="conf-pw">
                <p class="dialog-text">비밀번호를 한 번 더 입력해주세요.</p>
                <div class="input-group">
                    <input 
                    v-model="formData.passwordRe" 
                    type="password" 
                    placeholder="비밀번호 확인" 
                    @keyup.enter="checkPassword"
                    class="mystic-input"
                    />
                    <button @click="checkPassword" class="icon-btn">➜</button>
                </div>
              </div>

              <div v-else-if="step === 'PASSWORD_MISMATCH'" class="step-container" key="mismatch">
                <p class="dialog-text error-text">비밀번호가 일치하지 않습니다.</p>
                <button @click="retryPassword" class="mystic-btn primary">다시 입력하기</button>
              </div>

              <div v-else-if="step === 'SUCCESS'" class="step-container" key="success">
                <p class="dialog-text success-text">환영합니다, {{ formData.name }}님! 가입이 완료되었습니다.</p>
              </div>

            </transition>

         </div>
      </div>
    </transition>

    <div 
      class="toggle-btn"
      @mouseenter="isHovered = true"
      @mouseleave="isHovered = false"
      @click="toggleAssistant"
      :class="{ 'hidden-btn': isOpen }" 
    >
      <img :src="isHovered || isOpen ? openImg : closedImg" alt="Toggle Assistant" />
    </div>

  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700&family=Noto+Sans+KR:wght@300;400;500&display=swap');

.signup-assistant-root {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9999;
  font-family: 'Noto Sans KR', sans-serif;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(5, 5, 10, 0.6);
  backdrop-filter: blur(4px);
  pointer-events: auto;
}

/* --- Toggle Button --- */
.toggle-btn {
  position: absolute;
  bottom: 30px;
  right: 30px;
  cursor: pointer;
  pointer-events: auto;
  transition: all 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  filter: drop-shadow(0 0 10px rgba(100, 100, 255, 0.3));
}

.toggle-btn.hidden-btn {
  opacity: 0;
  transform: scale(0.5) translateY(50px);
  pointer-events: none;
}

.toggle-btn img {
  width: 100px;
  height: auto;
}

.toggle-btn:hover {
  transform: scale(1.1);
  filter: drop-shadow(0 0 15px rgba(150, 150, 255, 0.6));
}

/* --- Layout --- */
.assistant-content {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center; 
  padding-bottom: 40px;
}

/* --- Character --- */
.character-container {
  position: relative;
  z-index: 2;
  margin-bottom: -20px;
  width: 100%;
  max-width: 800px;
  padding-left: 40px; 
}

.character-img {
  width: 140px;
  height: auto;
  filter: drop-shadow(0 5px 15px rgba(0,0,0,0.5));
}

/* Floating Animation for Character */
.floating-anim {
  animation: floating 3s ease-in-out infinite;
}

@keyframes floating {
  0% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
  100% { transform: translateY(0px); }
}

/* --- Mysterious Dialog Box --- */
.dialog-box {
  pointer-events: auto;
  width: 90%;
  max-width: 700px;
  min-height: 120px;
  
  /* Mysterious Gradient Background */
  background: linear-gradient(135deg, rgba(20, 15, 35, 0.85) 0%, rgba(40, 30, 70, 0.9) 100%);
  
  /* Glassmorphism */
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  
  /* Borders & Glow */
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  box-shadow: 
    0 10px 40px rgba(0, 0, 0, 0.6), 
    0 0 20px rgba(120, 100, 255, 0.2),
    inset 0 0 30px rgba(0, 0, 0, 0.5);

  display: flex;
  justify-content: center;
  align-items: center;
  padding: 30px 40px;
  position: relative;
  overflow: hidden;
}

/* Optional: Shining line effect */
.dialog-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 50%;
  height: 100%;
  background: linear-gradient(to right, transparent, rgba(255,255,255,0.05), transparent);
  transform: skewX(-25deg);
  animation: shine 6s infinite;
}

@keyframes shine {
  0% { left: -100%; }
  20% { left: 200%; }
  100% { left: 200%; }
}

/* --- Content Styling --- */
.step-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  gap: 20px;
  z-index: 3;
}

.dialog-text {
  margin: 0;
  color: #e0e0ff; 
  font-size: 1.1rem;
  font-weight: 300;
  letter-spacing: 0.5px;
  line-height: 1.4;
  flex: 1;
  text-shadow: 0 0 5px rgba(0,0,0,0.5);
}

.dialog-text strong {
  color: #fff;
  font-weight: 500;
  text-shadow: 0 0 8px rgba(255, 255, 255, 0.4);
}

.error-text { color: #ff9999; }
.success-text { color: #aaddff; }

/* --- Inputs --- */
.input-group {
  display: flex;
  align-items: center;
  background: rgba(0, 0, 0, 0.3);
  border-radius: 30px;
  padding: 5px 10px 5px 20px;
  border: 1px solid rgba(255,255,255,0.1);
  box-shadow: inset 0 2px 5px rgba(0,0,0,0.5);
  transition: border-color 0.3s, box-shadow 0.3s;
}

.input-group:focus-within {
  border-color: rgba(150, 150, 255, 0.5);
  box-shadow: 0 0 10px rgba(100, 100, 255, 0.2);
}

.mystic-input {
  background: transparent;
  border: none;
  color: white;
  font-size: 1rem;
  width: 140px;
  outline: none;
}
.mystic-input::placeholder {
  color: rgba(255, 255, 255, 0.3);
  font-size: 0.9rem;
}

/* --- Buttons --- */
.icon-btn {
  background: transparent;
  border: none;
  color: rgba(255,255,255,0.7);
  font-size: 1.2rem;
  cursor: pointer;
  padding: 5px;
  transition: color 0.3s, transform 0.2s;
}
.icon-btn:hover {
  color: #fff;
  transform: translateX(3px);
  text-shadow: 0 0 5px #fff;
}

.btn-group {
  display: flex;
  gap: 12px;
}

.mystic-btn {
  padding: 8px 20px;
  border-radius: 20px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-family: inherit;
  letter-spacing: 0.5px;
}

.mystic-btn.primary {
  background: linear-gradient(135deg, rgba(100, 80, 255, 0.2), rgba(150, 100, 255, 0.1));
  border: 1px solid rgba(180, 180, 255, 0.3);
  color: white;
  box-shadow: 0 4px 15px rgba(0,0,0,0.2);
}

.mystic-btn.primary:hover {
  background: linear-gradient(135deg, rgba(120, 100, 255, 0.4), rgba(180, 150, 255, 0.2));
  box-shadow: 0 0 15px rgba(150, 100, 255, 0.4);
  transform: translateY(-2px);
}

.mystic-btn.secondary {
  background: transparent;
  border: 1px solid transparent;
  color: #aaa;
}
.mystic-btn.secondary:hover {
  color: #fff;
  text-shadow: 0 0 5px rgba(255,255,255,0.3);
}

/* --- Vue Transitions --- */
/* Overlay Fade */
.fade-enter-active, .fade-leave-active { transition: opacity 0.4s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

/* Main Container Slide */
.slide-up-enter-active { transition: all 0.5s cubic-bezier(0.19, 1, 0.22, 1); }
.slide-up-leave-active { transition: all 0.4s ease-in; }
.slide-up-enter-from { transform: translateY(50px); opacity: 0; }
.slide-up-leave-to { transform: translateY(50px); opacity: 0; }

/* Content Cross-fade */
.fade-content-enter-active, .fade-content-leave-active { transition: opacity 0.3s ease, transform 0.3s ease; }
.fade-content-enter-from { opacity: 0; transform: translateY(10px); }
.fade-content-leave-to { opacity: 0; transform: translateY(-10px); }

/* --- Mobile --- */
@media (max-width: 600px) {
  .dialog-box {
    flex-direction: column;
    padding: 20px;
    text-align: center;
  }
  .step-container {
    flex-direction: column;
    gap: 15px;
  }
  .btn-group {
    width: 100%;
    justify-content: center;
  }
  .character-container {
    padding-left: 0;
    display: flex;
    justify-content: center;
  }
}
</style>
