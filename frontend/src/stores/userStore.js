import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";

export const useUserStore = defineStore('user', () => {
  const isLogin = ref(false);     // 로그인 성공 여부
  const loginUser = ref(null);    // 로그인한 사용자 ID
  const BASE_URL = 'http://localhost:8080';

  const login = async (loginObj) => {
    try {
      await axios.post(`${BASE_URL}/api/auth/login`, loginObj);

      isLogin.value = true;
      loginUser.value = loginObj.username;

      return true;

    } catch (error) {
      console.error(error);
      alert("로그인 실패");
      return false;
    }
  };

  const logout = () => {
    axios.post(`${BASE_URL}/api/auth/logout`)
      .then(() => {
        isLogin.value = false;
        loginUser.value = null;
        alert("로그아웃 되었습니다.");
      })
      .catch((err) => {
        console.error(err);
        isLogin.value = false;
        loginUser.value = null;
      });
  }

  const createUser = (user) => {

    return axios.post(`${BASE_URL}/api/users`, user)
      .then(() => {
        alert("회원가입이 완료되었습니다.");
        return true;
      })
      .catch((err) => {
        console.error(err);
        alert("회원가입 실패");
        throw err;
      });
  };

  return {
    isLogin,
    loginUser,
    login,
    logout,
    createUser
  };
});
