package com.schizoscrypt.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schizoscrypt.dtos.*;
import com.schizoscrypt.factories.EmployerAccountDtoFactory;
import com.schizoscrypt.factories.EmployerAccountEntityFactory;
import com.schizoscrypt.factories.WorkerAccountDtoFactory;
import com.schizoscrypt.factories.WorkerAccountEntityFactory;
import com.schizoscrypt.services.interfaces.AccountService;
import com.schizoscrypt.storage.entities.EmployerAccountEntity;
import com.schizoscrypt.storage.entities.WorkerAccountEntity;
import com.schizoscrypt.storage.enums.Gender;
import com.schizoscrypt.storage.repositories.EmployerAccountRepository;
import com.schizoscrypt.storage.repositories.WorkerAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final RabbitTemplate template;
    private final JwtServiceImpl jwtService;
    private final ObjectMapper objectMapper;
    private final WorkerAccountDtoFactory workerAccountDtoFactory;
    private final WorkerAccountRepository workerAccountRepository;
    private final EmployerAccountDtoFactory employerAccountDtoFactory;
    private final EmployerAccountRepository employerAccountRepository;
    private final WorkerAccountEntityFactory workerAccountEntityFactory;
    private final EmployerAccountEntityFactory employerAccountEntityFactory;

    private final Map<String, UserDto> userDtoMap = new ConcurrentHashMap<>();
    private final Map<String, CountDownLatch> latches = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public WorkerAccountDto createWorkerAccount(String token, CreateWorkerAccountRequestDto request) {

        String email = jwtService.extractEmail(token);

        CountDownLatch latch = new CountDownLatch(1);
        latches.put(email, latch);

        template.convertAndSend("account_request", "account.request.key", email);

        String firstname;
        String lastname;
        try {
            if (latch.await(5, TimeUnit.SECONDS)) {
                UserDto userDto = userDtoMap.get(email);
                firstname = userDto.getFirstname();
                lastname = userDto.getLastname();
            } else {
                throw new RuntimeException("Timeout waiting for response");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted");
        } finally {
            latches.remove(email);
            userDtoMap.remove(email);
        }

        Gender gender = conversionStringToGender(request.getGender());
        String phoneNumber = validatePhoneNumber(request.getPhoneNumber());
        LocalDate birthdate = conversionStringToBirthdate(request.getBirthdate());

        WorkerAccountEntity workerAccount = workerAccountEntityFactory.makeWorkerAccountEntity(
                email, firstname, lastname, phoneNumber, birthdate, gender
        );
        WorkerAccountEntity savedWorker = workerAccountRepository.saveAndFlush(workerAccount);
        log.info("Created account for worker user with email {}", email);

        return workerAccountDtoFactory.makeWorkerAccountDto(savedWorker);
    }

    @Override
    @Transactional
    public EmployerAccountDto createEmployerAccount(String token, CreateEmployerAccountRequestDto request) {

        String email = jwtService.extractEmail(token);

        CountDownLatch latch = new CountDownLatch(1);
        latches.put(email, latch);

        template.convertAndSend("account_request", "account.request.key", email);

        String firstname;
        String lastname;
        try {
            if (latch.await(5, TimeUnit.SECONDS)) {
                UserDto userDto = userDtoMap.get(email);
                firstname = userDto.getFirstname();
                lastname = userDto.getLastname();
            } else {
                throw new RuntimeException("Timeout waiting for response");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted");
        } finally {
            latches.remove(email);
            userDtoMap.remove(email);
        }

        if (!isUrlValid(request.getCompanyWebsite())) {
            throw new RuntimeException("Company website URL is invalid");
        }
        String phoneNumber = validatePhoneNumber(request.getPhoneNumber());

        EmployerAccountEntity employerAccount = employerAccountEntityFactory.makeEmployerAccountEntity(
                email, firstname, lastname, phoneNumber, request
        );
        EmployerAccountEntity savedEmployer = employerAccountRepository.saveAndFlush(employerAccount);
        log.info("Created account for employer user with email {}", email);

        return employerAccountDtoFactory.makeEmployerAccountDto(savedEmployer);
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

            userDtoMap.put(email, userDto);

            CountDownLatch latch = latches.get(email);
            if (latch != null) {
                latch.countDown();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private boolean isUrlValid(String url) {
        if (url == null) {
            throw new RuntimeException("Website link can't be empty");
        }

        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | IllegalArgumentException | URISyntaxException exception) {
            return false;
        }
    }

    private String validatePhoneNumber(String phoneNumber) {

        final String PHONE_REGEX = "^\\+[1-9]\\d{1,14}$";

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new RuntimeException("Phone number can't be empty");
        }

        phoneNumber.replace("-", "");
        phoneNumber.replace(" ", "");

        if (!phoneNumber.startsWith("+")) {
            throw new RuntimeException("Phone number is invalid, missing + char");
        }

        Pattern pattern = Pattern.compile(PHONE_REGEX);

        if (pattern.matcher(phoneNumber).matches()) {
            return phoneNumber;
        } else {
            throw new RuntimeException("Phone number is invalid");
        }
    }

    private Gender conversionStringToGender(String genderString) {
        Gender gender;
        genderString.toUpperCase();

        if (genderString.equals("MAN") || genderString.equals("MALE")) {
            gender = Gender.MAN;
        } else if (genderString.equals("WOMAN") || genderString.equals("FEMALE")) {
            gender = Gender.WOMAN;
        } else {
            throw new RuntimeException("Invalid gender value");
        }

        return gender;
    }

    private LocalDate conversionStringToBirthdate(String birthdateString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthdate = null;

        try {
            birthdate = LocalDate.parse(birthdateString, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format");
        }

        return birthdate;
    }
}