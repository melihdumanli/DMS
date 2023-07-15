package com.melihdumanli.dms.dto.response;

import com.melihdumanli.dms.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String userName;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}
