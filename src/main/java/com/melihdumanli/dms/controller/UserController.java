package com.melihdumanli.dms.controller;

import com.melihdumanli.dms.dto.response.UserResponseDTO;
import com.melihdumanli.dms.exception.DmsBusinessException;
import com.melihdumanli.dms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Service", description = "The User API. Contains all the operations that can be performed on a user.")
public class UserController {

    private final UserService userService;

    @GetMapping()
    @Operation(summary = "This method is used o get user info by user id", description = "Enter the user Id to get user info.")
    public ResponseEntity<UserResponseDTO> getUserInfo(@RequestParam Long userId) throws DmsBusinessException {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @GetMapping("/fetch-users")
    @Operation(summary = "This method is used o get all users", description = "No parameters required.")
    public ResponseEntity<List<UserResponseDTO>> fetchUsers() {
        return ResponseEntity.ok(userService.fetchUsers());
    }

    @PostMapping("/add-authority")
    @Operation(summary = "This method is used o add ADMIN authority to the user.", description = "Enter the user id of the user to be given ADMIN privilege.")
    public ResponseEntity<UserResponseDTO> addAuthorityToUser(@RequestParam Long userId) throws DmsBusinessException {
        return ResponseEntity.ok(userService.addAuthorityToUser(userId));
    }

    @PostMapping("/remove-authority")
    @Operation(summary = "This method is used o remove ADMIN authority from user.", description = "Enter the user id of the user whose ADMIN authority will be deleted.")
    public ResponseEntity<UserResponseDTO> removeAuthorityFromUser(@RequestParam Long userId) throws DmsBusinessException {
        return ResponseEntity.ok(userService.removeAuthorityFromUser(userId));
    }

    @DeleteMapping()
    @Operation(summary = "This method is used to delete existing user", description = "Enter the user id of the user that will be deleted")
    public ResponseEntity<String> deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().body("Your account has been deleted!");
    }

}
