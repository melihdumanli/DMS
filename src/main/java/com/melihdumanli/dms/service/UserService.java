package com.melihdumanli.dms.service;

import com.melihdumanli.dms.constant.Role;
import com.melihdumanli.dms.dto.response.UserResponseDTO;
import com.melihdumanli.dms.exception.DmsBusinessException;
import com.melihdumanli.dms.exception.ExceptionSeverity;
import com.melihdumanli.dms.mapper.UserMapper;
import com.melihdumanli.dms.model.User;
import com.melihdumanli.dms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    static final String USER_NOT_FOUND_MESSAGE= "User Not Found! Wrong E-Mail or Password.";

    public UserResponseDTO getUserInfo(Long userId) throws DmsBusinessException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent())
            return UserMapper.INSTANCE.convertToDTO(optionalUser.get());
        else
            throw new DmsBusinessException(ExceptionSeverity.ERROR, USER_NOT_FOUND_MESSAGE);
    }

    public List<UserResponseDTO> fetchUsers()  {
        List<User> users = userRepository.getUsersByDeleteFlagFalse();
        return UserMapper.INSTANCE.convertToDTOList(users);
    }

    public UserResponseDTO addAuthorityToUser(Long id) throws DmsBusinessException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(Role.ADMIN);
            user.setUpdateDate(new Date());
            userRepository.save(user);
            return UserMapper.INSTANCE.convertToDTO(user);
        } else
            throw new DmsBusinessException(ExceptionSeverity.ERROR, USER_NOT_FOUND_MESSAGE);
    }

    public UserResponseDTO removeAuthorityFromUser(Long id) throws DmsBusinessException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(Role.USER);
            user.setUpdateDate(new Date());
            userRepository.save(user);
            return UserMapper.INSTANCE.convertToDTO(user);
        } else
            throw new DmsBusinessException(ExceptionSeverity.ERROR, USER_NOT_FOUND_MESSAGE);
    }

    public void deleteUserById(Long id) throws DmsBusinessException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleteFlag(true);
            user.setUpdateDate(new Date());
            userRepository.save(user);
        }
        else
            throw new DmsBusinessException(ExceptionSeverity.ERROR, USER_NOT_FOUND_MESSAGE);
    }
}

