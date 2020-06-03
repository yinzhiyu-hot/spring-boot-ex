package com.example.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.common.constants.BaseConstants.USERINFO_HEADER;

/**
 * @Description 获取header中的信息
 * @PackagePath com.example.interceptor.ContextInterceptor
 * @Author YINZHIYU
 * @Date 2020/6/3 15:42
 * @Version 1.0.0.0
 **/
@Slf4j
@Component
public class ContextInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userinfo = request.getHeader(USERINFO_HEADER);
        log.debug("拦截器获取Header中的userinfo信息为: {}", userinfo);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
