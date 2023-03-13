package com.snow.storeapi.interceptor;

import cn.hutool.core.util.StrUtil;
import com.snow.storeapi.entity.JwtCheckResult;
import com.snow.storeapi.security.HasRoles;
import com.snow.storeapi.security.JwtComponent;
import com.snow.storeapi.util.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

/**
 * 拦截器 用户权限校验
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class SysInterceptor implements HandlerInterceptor {
    private final JwtComponent jwtComponent;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            String authHeader = request.getHeader("Authorization");
            if (StrUtil.isEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
                log.info("验证失败");
                response.setContentType("text/plain;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print("token不存在");
                return false;
            } else {
                JwtCheckResult jwtCheckResult = jwtComponent.validateGetSubject(authHeader.substring(7).trim());
                if (jwtCheckResult.isSuccess()) {
                    BaseContext.setCurrentUser(jwtCheckResult.getUser());
                    // 认证成功 继续鉴权
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    HasRoles hasRoles = handlerMethod.getMethodAnnotation(HasRoles.class);
                    if (hasRoles == null) {
                        return true;
                    }
                    String[] identify = hasRoles.value();
                    boolean result = Arrays.stream(identify).anyMatch(e -> jwtCheckResult.getUser().getRoles().contains(e));
                    if (result) {
                        return true;
                    } else {
                        response.setContentType("text/plain;charset=UTF-8");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().print("未授权");
                        return false;
                    }
                } else {
                    response.setContentType("text/plain;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().print(jwtCheckResult.getErrMsg());
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    /**
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行，
     * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}  