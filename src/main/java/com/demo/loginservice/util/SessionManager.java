package com.demo.loginservice.util;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

public class SessionManager {

    private final static String IS_LOGIN          = "isLogin";
    private final static String LOGIN_SUCCESS_STR = "TRUE";
    private final static String LOGIN_EMAIL       = "LOGIN_EMAIL";

    public static boolean isLogin(HttpSession session){
        if (session.getAttribute(IS_LOGIN) != null && session.getAttribute(IS_LOGIN).equals(LOGIN_SUCCESS_STR)){
            return true;
        }

        return false;
    }

    /**
     * 세션 로그인 성공
     *
     * @param session
     * @param loginEmail
     */
    public static void setLogin(HttpSession session, String loginEmail){
        session.setAttribute(IS_LOGIN, LOGIN_SUCCESS_STR);
        session.setAttribute(LOGIN_EMAIL, loginEmail);
    }

    /**
     * 세션에 로그인 한 아이디를 표시하는 함수
     *
     * @param session
     */
    public static String getLoginId(HttpSession session) {
        return session.getAttribute(LOGIN_EMAIL) == null ? "" : (String) session.getAttribute(LOGIN_EMAIL);
    }

    /**
     *
     * Session의 모든 key-value를 비우는 함수
     *
     * @param session
     */
    @SuppressWarnings("unchecked")
    public static void clearSession(HttpSession session){
        Enumeration<String> keys = session.getAttributeNames();
        while(keys.hasMoreElements()){
            String curKey = keys.nextElement();
            session.removeAttribute(curKey);
        }
    }

    /**
     * Session을 비활성화
     *
     * @param session
     */
    public static void invalidateSession(HttpSession session){
        session.invalidate();
    }

}
