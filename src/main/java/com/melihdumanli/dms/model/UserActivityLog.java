package com.melihdumanli.dms.model;

import com.melihdumanli.dms.constant.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user_log")
public class UserActivityLog {
    @Id
    @SequenceGenerator(name = "sq_log_id",sequenceName = "sq_log_id",allocationSize = 1,initialValue = 1)
    @GeneratedValue(generator = "sq_log_id")
    public Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
    @Enumerated(EnumType.STRING)
    private Activity activity;
    private String fileName;
    private Date actionDate;
}
