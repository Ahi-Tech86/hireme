package com.schizoscrypt.services;

import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.services.interfaces.UserService;

public class UserServiceImpl implements UserService {

    // todo
    // сделать получение пользователя из бд по мылу
    // подключить кэширование
    //      сначала мы идём в кэш и ищем по ключу мыло с пользаком, если ключа нет то выполняем запрос и
    //      сохраняем в кэш

    @Override
    public UserDto getByUserByEmail(String email) {
        return null;
    }
}
