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
@Table(name = "tbl_token")
public class Token {

    @Id
    @SequenceGenerator(name = "sq_token_id",sequenceName = "sq_token_id",allocationSize = 1,initialValue = 1)
    @GeneratedValue(generator = "sq_token_id")
    public Long id;

    @Column(unique = true)
    public String token;


    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
