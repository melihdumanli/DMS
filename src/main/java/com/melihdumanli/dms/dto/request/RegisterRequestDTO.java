package com.melihdumanli.dms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
