package com.schizoscrypt.dtos;

import com.schizoscrypt.storage.enums.AppRole;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String firstname;
    private String lastname;
    private String email;
    private AppRole role;
}
