package com.melihdumanli.dms.model;

import com.melihdumanli.dms.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @SequenceGenerator(name = "sq_user_id",sequenceName = "sq_user_id",allocationSize = 1,initialValue = 1)
    @GeneratedValue(generator = "sq_user_id")
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Date createDate;
    private Date updateDate;
    private boolean deleteFlag;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "user")
    private List<UserActivityLog> activities;

}
