package com.melihdumanli.dms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDTO {
    private Long id;
    private String fileName;
    private Long fileSize;
    private String extension;
    private Date createDate;
    private Date updateDate;
    private byte[] fileContent;
}
