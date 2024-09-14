package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.dtos.CreateEmployerAccountRequestDto;
import com.schizoscrypt.dtos.CreateWorkerAccountRequestDto;
import com.schizoscrypt.dtos.EmployerAccountDto;
import com.schizoscrypt.dtos.WorkerAccountDto;

public interface AccountService {

    WorkerAccountDto createWorkerAccount(String token, CreateWorkerAccountRequestDto request);

    EmployerAccountDto createEmployerAccount(String token, CreateEmployerAccountRequestDto request);
}
