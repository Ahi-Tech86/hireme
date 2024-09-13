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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final RabbitTemplate template;
    private final JwtServiceImpl jwtService;
    private final ObjectMapper objectMapper;

    private final Map<String, UserDto> userDtos = new ConcurrentHashMap<>();
    private final Map<String, CountDownLatch> latches = new ConcurrentHashMap<>();

    @Override
    public String createWorkerAccount(String token) {

        String email = jwtService.extractEmail(token);

        CountDownLatch latch = new CountDownLatch(1);
        latches.put(email, latch);

        template.convertAndSend("account_request", "account.request.key", email);

        try {
            if (latch.await(5, TimeUnit.SECONDS)) {
                UserDto userDto = userDtos.get(email);
                return userDto.getFirstname() + " " + userDto.getLastname() + " " + userDto.getEmail();
            } else {
                return "Timeout waiting for response";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Thread was interrupted";
        } finally {
            latches.remove(email);
            userDtos.remove(email);
        }
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
            String email = userDto.getEmail();

            userDtos.put(email, userDto);

            CountDownLatch latch = latches.get(email);
            if (latch != null) {
                latch.countDown();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}