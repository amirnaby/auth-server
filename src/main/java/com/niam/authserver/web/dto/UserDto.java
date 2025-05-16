package com.niam.authserver.web.dto;

import com.niam.authserver.persistence.model.Role;
import com.niam.authserver.validation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
public class UserDto {
    private Boolean enabled;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.firstName}")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.lastName}")
    private String lastName;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.username}")
    private String username;

    @Size(min = 1, message = "{Size.userDto.phone}")
    private String phone;

    @NotNull
    @ValidPassword
    private String password;

    @NotNull
    private Collection<Role> roles;
}