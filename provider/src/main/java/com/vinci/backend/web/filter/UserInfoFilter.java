package com.vinci.backend.web.filter;

import com.vinci.backend.domain.user.model.UserModel;
import com.vinci.backend.domain.user.service.UserService;
import com.vinci.backend.web.user.UserContext;
import com.vinci.backend.web.user.UserInfo;
import com.vinci.common.base.monitor.QMonitor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户信息过滤器
 */
public class UserInfoFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(UserInfoFilter.class);

    @Resource
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            long startOfUserInfo = System.currentTimeMillis();
            UserInfo userInfo = parseUserInfo(httpServletRequest);
            UserContext.setUserInfo(userInfo);
            QMonitor.recordOne("UserInfoFilter_parseUserInfo_Success", System.currentTimeMillis() - startOfUserInfo);
        } catch (Exception e) {
            LOG.warn("UserInfoFilter parseUserInfo error", e);
            QMonitor.recordOne("UserInfoFilter_parseUserInfo_Failed");
            UserContext.setUserInfo(null);
            throw new RuntimeException(e);
        }

        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            LOG.warn("UserInfoFilter doFilter error", e);
            QMonitor.recordOne("UserInfoFilter_doFilter_Failed");
            throw new RuntimeException(e);
        } finally {
            UserContext.setUserInfo(null);
        }
    }

    private UserInfo parseUserInfo(HttpServletRequest httpServletRequest) {
        long userid = 0L;
        UserModel user = null;
        try {
            String strUserid = findCookieByName(httpServletRequest, "userid");
            if (!StringUtils.isEmpty(strUserid)) {
                userid = Long.parseLong(strUserid);
            }
            if (userid > 0) {
                user = userService.getUserByUserID(userid);
            }
        } catch (NumberFormatException e) {
            //ignore
        } catch (Exception e) {
            logger.warn("检查登录失败，请检查：", e);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userid);
        userInfo.setDeviceId(findCookieByName(httpServletRequest, "imei"));
        userInfo.setUserName(user == null ? "" : user.getNickName());
        userInfo.setUserIp(getUserIp(httpServletRequest));
        userInfo.setVid(findCookieByName(httpServletRequest, "appVersion"));
        return userInfo;
    }

    private String getUserIp(HttpServletRequest request) {
        String ip = request.getHeader("x-real-ip");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        int pos = ip.indexOf(',');
        if (pos >= 0) {
            ip = ip.substring(0, pos);
        }
        return ip;
    }

    private String findCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())) {
                return StringUtils.trimToNull(cookie.getValue());
            }
        }
        return null;
    }

    private void validateUserInfo(UserInfo userInfo) {
    }


}
