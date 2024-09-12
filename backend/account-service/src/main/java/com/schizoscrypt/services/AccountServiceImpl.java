package com.schizoscrypt.services;

import com.schizoscrypt.dtos.CreateWorkerAccountRequestDto;
import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.WorkerAccountDto;
import com.schizoscrypt.services.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final RabbitTemplate template;
    private final JwtServiceImpl jwtService;
    private final CountDownLatch latch = new CountDownLatch(1);
    private String userDto;

    @Override
    public String createWorkerAccount(String token) {

        String email = jwtService.extractEmail(token);

        template.convertAndSend("account_request", "account.request.key", email);

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return userDto;
    }

    @RabbitListener(queues = "auth_response")
    public void receiveAuthResponse(String userDto) {
        this.userDto = userDto;
        latch.countDown();
    }
}
