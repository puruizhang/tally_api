package com.bestzpr.tally.config;

import com.bestzpr.tally.dao.UserDao;
import com.bestzpr.tally.domain.entity.User;
import com.bestzpr.tally.util.ApiResponse;
import com.bestzpr.tally.util.JsonUtil;
import com.bestzpr.tally.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * @className AuthenticationAspect
 * @Desc 认证拦截
 * @Author 张埔枘
 * @Date 2023/12/11 20:55
 * @Version 1.0
 */
@RequiredArgsConstructor
@Aspect
@Component
public class AuthenticationAspect {

    private final UserDao userDao;


    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object aroundRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取请求的路径
        String requestPath = request.getRequestURI();
        if(requestPath.equals("/v1/api/getOpenId")){
            // 执行目标方法
            return joinPoint.proceed();
        }

        HttpServletResponse response = attributes.getResponse();

        String openId = request.getHeader("openId");
        String apiKey = request.getHeader("apiKey");
        assert response != null;
        if (!StringUtils.hasText(openId)) {
            // 可以根据需要设置其他响应头或返回错误信息
//            response.setHeader("Content-Type", "application/json");

            this.auth(response);

            return null; // 返回null表示不继续执行目标方法
        }

        // 在这里进行用户登录状态的判断逻辑，可以根据openId和apiKey进行验证
        // 如果验证失败，可以抛出异常或执行相应的操作
        User user = userDao.findByOpenId(openId);
        if(null == user){
            this.auth(response);
        }
        UserContext.setUser(user);
        // 执行目标方法
        return joinPoint.proceed();
    }

    private void auth(HttpServletResponse response) {
        try {
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null);
            response.getWriter().write(JsonUtil.toJson(apiResponse));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterReturning("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void afterRequest() {
        // 在请求处理完成后执行的逻辑
    }
}