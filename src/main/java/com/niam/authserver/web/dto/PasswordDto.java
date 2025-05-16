package com.niam.authserver.web.dto;

import com.niam.authserver.validation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PasswordDto {

    @NotNull
    private String username;

    @NotNull
    private String oldPassword;

    @ValidPassword
    @NotNull
    private String newPassword;

}
