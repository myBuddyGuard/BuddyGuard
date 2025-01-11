package buddyguard.mybuddyguard.jwt.utils;

import jakarta.servlet.http.Cookie;

public class TokenUtility {

    /**
     * Bearer 접두사를 제거합니다.
     *
     * @param token
     * @return
     */
    public static String deletePrefixToken(String token) {
        if (token == null) {
            return null;
        }
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    /**
     * 쿠키에서 refresh 토큰을 추출합니다.
     *
     * @param cookies
     * @return
     */
    public static String getRefreshToken(Cookie[] cookies) {
        String refresh = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }
        return refresh;
    }
}
