package com.schizoscrypt.dtos;

import com.schizoscrypt.storage.enums.AppRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;
    private String email;
    private AppRole role;
}
