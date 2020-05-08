package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.common.constants.BaseConstants.USERINFO_HEADER;

/**
 * @Description
 * @PackagePath com.example.config.WebMvcAutoConfiguration
 * @Author YINZHIYU
 * @Date 2020/5/8 13:42
 * @Version 1.0.0.0
 **/
@Slf4j
@Configuration
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
public class WebMvcAutoConfiguration implements WebMvcConfigurer {
    /**
     * 获取header中的信息
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String userinfo = request.getHeader(USERINFO_HEADER);
                log.debug("拦截器获取Header中的userinfo信息为: {}", userinfo);

                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

            }
        });
    }

    /**
     * 设置资源文件目录
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/**").addResourceLocations("classpath:/public/");
        registry.addResourceHandler("swagger-ui.html", "doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 设置允许跨域的路径
                .allowedOrigins("*")// 设置允许跨域请求的域名
                .allowCredentials(true)// 是否允许证书 不再默认开启
                .allowedMethods("GET", "POST", "PUT", "DELETE")// 设置允许的方法
                .maxAge(3600);// 跨域允许时间
    }
}