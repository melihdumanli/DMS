package com.melihdumanli.dms.controller;

import com.melihdumanli.dms.dto.response.DocumentResponseDTO;
import com.melihdumanli.dms.exception.DmsBusinessException;
import com.melihdumanli.dms.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/document")
@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Document Services", description = "The Document API. Contains all the operations that can be performed on documents.")
public class DocumentController {
    private final DocumentService documentService;
    @GetMapping()
    @Operation(summary = "This method is used to get file by file id.",  description = "Enter the document Id to get document info.")
    public ResponseEntity<DocumentResponseDTO> getDocumentById(Long id) throws DmsBusinessException {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @GetMapping("/findall")
    @Operation(summary = "This method is used to get all files.", description = "No parameter required to get files.")
    public ResponseEntity<List<DocumentResponseDTO>> fetchFiles() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "This method is used to add a new file.", description = "Upload a document.")
    public ResponseEntity<DocumentResponseDTO> addFile(HttpServletRequest request, @RequestPart("file") MultipartFile file) throws IOException, DmsBusinessException {
        return ResponseEntity.ok(documentService.saveDocument(request, file));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "This method is used to update existing file.", description = "Upload updated document.")
    public ResponseEntity<DocumentResponseDTO> updateFile(HttpServletRequest request, @RequestPart("file") MultipartFile file, @RequestParam Long id) throws IOException, DmsBusinessException {
        return ResponseEntity.ok(documentService.updateDocument(request, file, id));
    }

    @DeleteMapping()
    @Operation(summary = "This method is used to delete file.", description = "Enter an existing document id to delete a document")
    public ResponseEntity<String> deleteFile(HttpServletRequest request, @RequestParam Long id) {
        documentService.deleteDocumentById(request, id);
        return ResponseEntity.ok().body("File has been deleted!");
    }
}
