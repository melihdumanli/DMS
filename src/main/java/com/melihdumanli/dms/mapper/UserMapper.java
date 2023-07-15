package com.melihdumanli.dms.mapper;

import com.melihdumanli.dms.dto.response.UserResponseDTO;
import com.melihdumanli.dms.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDTO convertToDTO(User user);
    List<UserResponseDTO> convertToDTOList(List<User> users);
}
