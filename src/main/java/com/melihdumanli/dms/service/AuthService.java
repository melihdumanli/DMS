package com.melihdumanli.dms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melihdumanli.dms.config.security.JwtSecurity;
import com.melihdumanli.dms.constant.Activity;
import com.melihdumanli.dms.constant.Role;
import com.melihdumanli.dms.dto.request.LoginRequestDTO;
import com.melihdumanli.dms.dto.request.RegisterRequestDTO;
import com.melihdumanli.dms.dto.response.AuthResponseDTO;
import com.melihdumanli.dms.exception.DmsBusinessException;
import com.melihdumanli.dms.exception.ExceptionSeverity;
import com.melihdumanli.dms.model.Token;
import com.melihdumanli.dms.model.User;
import com.melihdumanli.dms.model.UserActivityLog;
import com.melihdumanli.dms.repository.TokenRepository;
import com.melihdumanli.dms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtSecurity jwtSecurity;
    private final AuthenticationManager authenticationManager;
    private final UserActivityLogService logService;

    static final String EMAIL_ALREADY_EXISTS_MESSAGE= "The email address you provided is already registered.";

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent())
            throw new DmsBusinessException(ExceptionSeverity.ERROR, EMAIL_ALREADY_EXISTS_MESSAGE);
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .createDate(new Date())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        logService.saveUserActivity(generateUserActivity(savedUser, Activity.REGISTER));
        String jwtToken = jwtSecurity.generateToken(user);
        String refreshToken = jwtSecurity.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthResponseDTO authenticate(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        logService.saveUserActivity(generateUserActivity(user, Activity.LOGIN));
        String jwtToken = jwtSecurity.generateToken(user);
        String refreshToken = jwtSecurity.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtSecurity.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtSecurity.isTokenValid(refreshToken, user)) {
                String accessToken = jwtSecurity.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthResponseDTO authResponse = AuthResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private UserActivityLog generateUserActivity(User user, Activity activity) {
        UserActivityLog userActivityLog = new UserActivityLog();
        userActivityLog.setActivity(activity);
        userActivityLog.setUser(user);
        userActivityLog.setActionDate(new Date());
        return userActivityLog;
    }
}
