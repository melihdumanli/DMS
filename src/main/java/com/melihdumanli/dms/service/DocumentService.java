package com.melihdumanli.dms.service;


import com.melihdumanli.dms.config.security.JwtSecurity;
import com.melihdumanli.dms.constant.Activity;
import com.melihdumanli.dms.dto.response.DocumentResponseDTO;
import com.melihdumanli.dms.exception.DmsBusinessException;
import com.melihdumanli.dms.exception.ExceptionSeverity;
import com.melihdumanli.dms.model.Document;
import com.melihdumanli.dms.model.User;
import com.melihdumanli.dms.model.UserActivityLog;
import com.melihdumanli.dms.repository.DocumentRepository;
import com.melihdumanli.dms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final JwtSecurity jwtSecurity;
    private final UserRepository userRepository;
    private final UserActivityLogService logService;

    static final String FILE_NOT_FOUND_MESSAGE= "File Not Found!";
    static final String FILE_EXTENSION_NOT_VALID_MESSAGE= "File Extension Is Not Valid!";
    static final String FILE_ALREADY_EXIST_MESSAGE = "The file name is already in use. Please choose a different name.";
    static final String TOKEN_EXPIRED_MESSAGE= "Token Expired. Please Sign In Again.";

    public DocumentResponseDTO getDocumentById(Long id) throws DmsBusinessException {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if(optionalDocument.isPresent())
            return convertToFileResponseDTO(optionalDocument.get(),true);
        else
            throw new DmsBusinessException(ExceptionSeverity.ERROR, FILE_NOT_FOUND_MESSAGE);
    }

    public DocumentResponseDTO saveDocument(HttpServletRequest request, MultipartFile file) throws IOException, DmsBusinessException {
        if(!isFileExtensionSupported(Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()), "File Must Not Be Null!")))
            throw new DmsBusinessException(ExceptionSeverity.ERROR, FILE_EXTENSION_NOT_VALID_MESSAGE);
        if(documentRepository.existsByFileName(file.getOriginalFilename().toUpperCase()))
            throw new DmsBusinessException(ExceptionSeverity.WARNING, FILE_ALREADY_EXIST_MESSAGE);
        User user = checkHeaderAndReturnUser(request);
        Document savedDoc = documentRepository.save(Document.builder()
                        .fileName(file.getOriginalFilename())
                        .fileSize(file.getSize())
                        .fileContent(file.getBytes())
                        .deleteFlag(false)
                        .version(0)
                        .createDate(new Date())
                        .extension(FilenameUtils.getExtension(file.getOriginalFilename()))
                .build());
        logService.saveUserActivity(generateUserActivity(user, Activity.CREATE, savedDoc.getFileName()));
        return convertToFileResponseDTO(savedDoc,false);
    }

    public DocumentResponseDTO updateDocument(HttpServletRequest request, MultipartFile file, Long id) throws IOException, DmsBusinessException {
        if(!isFileExtensionSupported(Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()), "File Must Not Be Null!")))
            throw new DmsBusinessException(ExceptionSeverity.ERROR, FILE_EXTENSION_NOT_VALID_MESSAGE);
        Optional<Document> optionalDocument = documentRepository.findById(id);
        int version = 0;
        User user = checkHeaderAndReturnUser(request);
        if(optionalDocument.isPresent()) {
            Document currentDoc = optionalDocument.get();
            version = currentDoc.getVersion();
            currentDoc.setDeleteFlag(true);
            currentDoc.setVersion(currentDoc.getVersion() + 1);
            currentDoc.setUpdateDate(new Date());
            documentRepository.save(currentDoc);
            logService.saveUserActivity(generateUserActivity(user, Activity.UPDATE, currentDoc.getFileName()));
        }
        else
            throw new DmsBusinessException(ExceptionSeverity.ERROR, FILE_NOT_FOUND_MESSAGE);
        Document savedDoc = documentRepository.save(Document.builder()
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileContent(file.getBytes())
                .deleteFlag(false)
                .version(version +1)
                .createDate(new Date())
                .extension(FilenameUtils.getExtension(file.getOriginalFilename()))
                .build());
        logService.saveUserActivity(generateUserActivity(user, Activity.CREATE, savedDoc.getFileName()));
        return convertToFileResponseDTO(savedDoc,false);
    }

    public void deleteDocumentById(HttpServletRequest request, Long id) throws DmsBusinessException{
        Optional<Document> optionalDocument = documentRepository.findById(id);
        User user = checkHeaderAndReturnUser(request);
        if(optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            document.setDeleteFlag(true);
            document.setUpdateDate(new Date());
            documentRepository.save(document);
            logService.saveUserActivity(generateUserActivity(user, Activity.DELETE, document.getFileName()));
        }
        else
            throw new DmsBusinessException(ExceptionSeverity.ERROR, FILE_NOT_FOUND_MESSAGE);
    }

    public boolean isFileExtensionSupported(String fileExtension) {
        Set<String> supportedExtensions = Set.of("png", "jpeg", "jpg", "docx", "pdf", "xlsx");
        return supportedExtensions.contains(fileExtension.toLowerCase());
    }

    public List<DocumentResponseDTO> getAllDocuments() {
        List<Document> documents = documentRepository.getDocumentsByDeleteFlagFalse();
        List<DocumentResponseDTO> documentResponseDTOS = new ArrayList<>();
        for (Document document : documents) {
            documentResponseDTOS.add(convertToFileResponseDTO(document, false));
        }
        return documentResponseDTOS;
    }

    private DocumentResponseDTO convertToFileResponseDTO(Document document, boolean includeFileContent) {
        DocumentResponseDTO documentResponseDTO = new DocumentResponseDTO();
        documentResponseDTO.setId(document.getId());
        documentResponseDTO.setFileName(document.getFileName());
        documentResponseDTO.setFileSize(document.getFileSize());
        documentResponseDTO.setExtension(document.getExtension());
        documentResponseDTO.setCreateDate(document.getCreateDate());
        documentResponseDTO.setUpdateDate(document.getUpdateDate());
        if (includeFileContent) {
            documentResponseDTO.setFileContent(document.getFileContent());
        }
        return documentResponseDTO;
    }

    private User checkHeaderAndReturnUser(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token, userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new DmsBusinessException(ExceptionSeverity.WARNING, TOKEN_EXPIRED_MESSAGE);
        }
        token = authHeader.substring(7);
        userEmail = jwtSecurity.extractUsername(token);
        User user = new User();
        if (userEmail != null)
            user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
        return user;
    }

    private UserActivityLog generateUserActivity(User user, Activity activity, String fileName) {
        UserActivityLog userActivityLog = new UserActivityLog();
        userActivityLog.setActivity(activity);
        userActivityLog.setUser(user);
        userActivityLog.setActionDate(new Date());
        userActivityLog.setFileName(fileName);
        return userActivityLog;
    }

}
