package com.schizoscrypt.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schizoscrypt.dtos.CreateWorkerAccountRequestDto;
import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.WorkerAccountDto;
import com.schizoscrypt.services.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final RabbitTemplate template;
    private final JwtServiceImpl jwtService;
    private final CountDownLatch latch = new CountDownLatch(1);
    private UserDto userDto;
    private final ObjectMapper objectMapper;

    @Override
    public String createWorkerAccount(String token) {

        String email = jwtService.extractEmail(token);

        template.convertAndSend("account_request", "account.request.key", email);

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return userDto.getFirstname() + " " + userDto.getLastname() + " " + userDto.getEmail();
    }

    @RabbitListener(queues = "auth_response")
    public void receiveAuthResponse(String json) {
        try {

            json = json.replace("\\\"", "\"");
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = json.substring(1, json.length() - 1);
            }
            json = json.replace("\\", "");

            UserDto userDto = objectMapper.readValue(json, UserDto.class);
            this.userDto = userDto;
            latch.countDown();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}