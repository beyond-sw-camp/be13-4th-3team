import { useAuthStore } from "@/stores/auth";
import axios from "axios";  // axios 라이브러리를 사용해 HTTP 통신 구현

// Axios
//  - HTTP 기반 통신을 지원하는 자바스크립트 라이브러리이다.
//  - 브라우저 호환성, JSON 자동 변환 기능 등의 장점을 가지고 있다.
const apiClient = axios.create({
    // 새로운 Axios 인스턴스 생성 -> 기본 URL + 만료 시간 설정 
    baseURL: 'http://localhost:8080',
    timeout: 2000   // 2초를 넘기면 타임아웃 발생 
});

// Axios 인터셉터
//  - 요청(request) 또는 응답(response)이 처리되기 전에 가로채서 특정 로직을 수행하도록 하는 기능이다.

// 요청(request) 인터셉터
//  - HTTP 요청이 서버로 전송되기 전에 실행된다.
apiClient.interceptors.request.use(
    // HTTP 요청을 서버로 보내기 전에 config 객체를 수정함
    // Authorization 헤더에 accessToken을 추가하는 로직이 포함되어 있음
    (config) => {
        if (config._skipInterceptor) {
            // 만약 요청 객체에 _skipInterceptor가 설정되어 있으면 그냥 건너뜀
            return config;
        }

        // 로컬 스토리지에서 accessToken을 가져온다.
        const accessToken = localStorage.getItem('accessToken');

        // accessToken 확인 후 Authorization 해더에 accessToken을 추가한다.
        if (accessToken) {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }

        return config;
    },
    (error) => {
        // 비동기 코드에서 에러를 처리하거나 에러를 즉시 반환할 때 사용한다.
        return Promise.reject(error);
    }
);

// 응답(response) 인터셉터
//  - 서버에서 HTTP 응답이 도착한 후에 실행된다.
apiClient.interceptors.response.use(
    (response) => {
        // 평범한 response가 온 경우, 그냥 response 그대로 반환
        return response;
    },
    // 비동기 함수
    async (error) => {
        // error가 발생한 경우, 

        // 이전 요청에 대한 config 객체를 얻어온다.
        const originalRequest = error.config;


        // 토큰이 만료되어 401 에러가 발생한 경우
        if (error.response.status === 401 && !originalRequest._retry) {
            console.log(originalRequest._retry);
            console.log("AccessToken 만료")
            console.log("Error:", error);
            console.log("Response:", error.response);
            // 무한 요청 재시도를 방지하기 위한 체크 변수
            originalRequest._retry = true;  // 객체에서 동적으로 추가된 변수 -> 응답 인터셉터 내에서 직접 추가됨
            console.log(originalRequest._retry);
            try {
                // localStorage에서 refreshToken을 가져옴
                const refreshToken = localStorage.getItem('refreshToken');
                console.log(refreshToken);

                // refreshToken이 존재하지 않는 경우~~~~ -> 무한 루프 방지ㅎㅎ
                if (!refreshToken) {
                    console.log("리프레시 토큰이 존재하지 않습니다.");
                    return Promise.reject(error);  // 리프레시 토큰이 없으면 바로 에러 반환
                }

                // 이 토큰을 Authorization 헤더에 Bearer ${refresh} 형태로 담아서 해당 엔드포인트에 POST 요청 전송
                // apiClient.post -> axios의 POST 메서드 호출 코드
                 
                // await : Promise가 해결될 때까지 기다리고, 값을 반환
                //  -> refreshToken을 이용해 새로운 AccessToken을 얻기 위해 서버에 비동기 요청을 보냄
                const response = await apiClient.post(
                    '/user/refresh', // 해당 URL로 post 요청을 보낼거다  
                    null,   // 근데 Data는 없다
                    {   // config 설정은 이러하다. -> 헤더에 Bearer ${refreshToken} 형태로 토큰을 담아서 보낼 예정이다.
                        headers: {
                            'Authorization': `Bearer ${refreshToken}`
                        },
                        _skipInterceptor: true
                    }
                );

                // 새로운 accessToken 받기
                const accessToken = response.data.accessToken;
                console.log("AccessToken 발급 완료 ")
                console.log(accessToken);

                // 새 액세스 토큰을 로컬 스토리지에 저장
                localStorage.setItem('accessToken', accessToken);

                const parsedToken = parseJwt(accessToken);
                const authStore = useAuthStore();

                authStore.isLoggedIn = true;
                authStore.userInfo.username = parsedToken.username;
                authStore.userInfo.role = parsedToken.role;

                const userNameResponse = await apiClient.get(
                    '/user/user-name',
                    {
                        headers: { 
                            'Authorization': `Bearer ${response.data.accessToken}` 
                        }
                    }
                );
                authStore.userInfo.name = userNameResponse.data;

                console.log("원래 요청 재시도")
                // 원래 요청을 재시도
                return apiClient(originalRequest);
            } catch (error) {
                // 리프레시 토큰이 만료된 경우, 로그아웃 처리
                const authStore = useAuthStore();

                authStore.logout();

                console.log("refreshToken 또한 만료됨ㅠ")
                return Promise.reject(error);
            }
        }

        return Promise.reject(error);
    }
);

// axios 객체로 apiClient 를 반환
export default apiClient;