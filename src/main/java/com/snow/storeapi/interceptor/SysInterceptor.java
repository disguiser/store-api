package com.snow.storeapi.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.storeapi.constant.SystemConstant;
import com.snow.storeapi.entity.CheckResult;
import com.snow.storeapi.entity.R;
import com.snow.storeapi.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 拦截器 用户权限校验
 */
public class SysInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SysInterceptor.class);

    public static ObjectMapper objectMapper;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.isEmpty(authHeader)) {
                logger.info("验证失败");
                response.setStatus(SystemConstant.JWT_ERRCODE_NULL);
                print(response, R.error(SystemConstant.JWT_ERRCODE_NULL, "签名验证不存在"));
                return false;
            } else {
                //验证JWT的签名，返回CheckResult对象
                CheckResult checkResult = JwtUtils.validateJWT(authHeader.substring(7));
                if (checkResult.isSuccess()) {
                    return true;
                } else {
                    switch (checkResult.getErrCode()) {
                        // 签名验证不通过
                        case SystemConstant.JWT_ERRCODE_FAIL:
                            logger.info("签名验证不通过");
                            response.setStatus(SystemConstant.JWT_ERRCODE_FAIL);
                            print(response, R.error(checkResult.getErrCode(), "签名验证不通过"));
                            break;
                        // 签名过期，返回过期提示码
                        case SystemConstant.JWT_ERRCODE_EXPIRE:
                            logger.info("签名过期");
                            response.setStatus(SystemConstant.JWT_ERRCODE_EXPIRE);
                            print(response, R.error(checkResult.getErrCode(), "签名过期"));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    public void print(HttpServletResponse response, Object message) {
        try {
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print(JSON.toJSONString(message));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        if(response.getStatus()==500){
//            modelAndView.setViewName("/error/500");
//        }else if(response.getStatus()==404){
//            modelAndView.setViewName("/error/404");
//        }
    }

    /**
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行，
     * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}  