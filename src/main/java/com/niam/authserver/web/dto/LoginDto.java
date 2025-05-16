package com.niam.authserver.web.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    private String input;
    private String token;
}