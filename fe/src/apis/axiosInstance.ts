import axios from 'axios';

/** Axios 인스턴스 */
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_SERVER_URL,
  timeout: 10000,
  withCredentials: true, // 쿠키를 포함하도록 설정
});

// 요청 인터셉터: 요청마다 Authorization 헤더에 accessToken 자동 추가
apiClient.interceptors.request.use(
  (config) => {
    // 여기서 토큰을 하드코딩하여 Authorization 헤더에 추가
    const accessToken = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjUsInJvbGUiOiJST0xFX1VTRVIiLCJ0b2tlblR5cGUiOiJBQ0NFU1MiLCJpYXQiOjE3MjkxODQzNjIsImV4cCI6NjE3MjkxODQzNjJ9.tclLX9BIEMbZoRFaY5kkaf_p_u3QbPuoW2rSygIAe4I';
    
    if (!config.headers['skipAuth']) {
      config.headers['Authorization'] = accessToken;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default apiClient;