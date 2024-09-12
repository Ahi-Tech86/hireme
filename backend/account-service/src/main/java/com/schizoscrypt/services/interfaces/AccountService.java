package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.dtos.CreateWorkerAccountRequestDto;
import com.schizoscrypt.dtos.WorkerAccountDto;

public interface AccountService {

    String createWorkerAccount(String token);
}
