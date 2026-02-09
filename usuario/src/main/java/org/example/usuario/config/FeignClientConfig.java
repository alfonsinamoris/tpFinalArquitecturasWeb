package org.example.usuario.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    String authUser = request.getHeader("X-Auth-User");
                    String authRoles = request.getHeader("X-Auth-Roles");

                    System.out.println(">>> FEIGN INTERCEPTOR (Usuario MS) <<<");
                    System.out.println("Reenviando a: " + template.url());
                    System.out.println("User: " + authUser);
                    System.out.println("Roles: " + authRoles);

                    if (authUser != null) template.header("X-Auth-User", authUser);
                    if (authRoles != null) template.header("X-Auth-Roles", authRoles);
                }
            }
        };
    }
}
