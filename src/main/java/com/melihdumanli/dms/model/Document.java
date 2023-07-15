package com.melihdumanli.dms.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_document")
public class Document implements Serializable {
    @Id
    @SequenceGenerator(name = "sq_document_id",sequenceName = "sq_document_id",allocationSize = 1,initialValue = 1)
    @GeneratedValue(generator = "sq_document_id")
    private Long id;
    private String fileName;
    private Long fileSize;
    private String extension;
    private Date createDate;
    private Date updateDate;
    private Boolean deleteFlag;
    private int version = 0;
    @Lob
    private byte[] fileContent;

}
