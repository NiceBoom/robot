package com.fly.robot.aop;

import com.fly.robot.annotation.JwtAuthenticated;
import com.fly.robot.dao.UserRepository;
import com.fly.robot.entity.User;
import com.fly.robot.util.JwtTokenUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@Aspect
@Component
public class JwtAuthAspect {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    @Pointcut("@annotation(com.fly.robot.annotation.JwtAuthenticated) || @within(com.fly.robot.annotation.JwtAuthenticated)")
    public void authPointcut() {
    }

    @Around("authPointcut()")
    public Object doJwtAuth(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取方法上的@Auth注解中的权限信息
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        JwtAuthenticated auth = signature.getMethod().getAnnotation(JwtAuthenticated.class);
        int[] roles = auth.value();
        // 从请求中获取Jwt，并解析出用户的身份信息和权限信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("No current request");
        }
        HttpServletRequest request = (attributes.getRequest());
        //根据token从mysql中查询用户数据
        String token = request.getHeader("Authorization");
        String username = jwtTokenUtil.getUserNameFromToken(token);
        User user = userRepository.findByUsername(username);
        //验证token是否还有效
        if (jwtTokenUtil.validateToken(token, user)) {
            //校验权限
            for (Integer role : roles) {
                if (user.getPermission() == role) {
                    return proceedingJoinPoint.proceed();
                }
            }
            throw new AccessDeniedException("您没有足够的权限");
        }
        throw new AccessDeniedException("您的token已失效");
    }
}