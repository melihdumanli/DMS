package com.melihdumanli.dms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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


}
