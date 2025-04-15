package ru.cinimex.taskservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class FeignClientConfig implements RequestInterceptor {

    @Value("${jwt.token}")
    String authHeader;


    @Override
    public void apply(RequestTemplate requestTemplate) {

        if (authHeader != null) {
            requestTemplate.header("Authorization", "Bearer " + authHeader);
        }
        else{
            throw new RuntimeException("Отсутствует jwt-Токен");
        }
    }

}
