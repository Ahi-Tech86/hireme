package com.schizoscrypt.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schizoscrypt.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthListener {

    private final UserServiceImpl service;
    private final RabbitTemplate template;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "account_request")
    public void listenAccountRequests(String email) {
        UserDto userDto = service.getByUserByEmail(email);

        try {
            String userDtoJson = objectMapper.writeValueAsString(userDto);
            template.convertAndSend("auth_exchange", "auth.response.key", userDtoJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
