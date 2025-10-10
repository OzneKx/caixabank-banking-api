package com.hackathon.finservice.data.mapper;

import com.hackathon.finservice.data.entity.User;
import com.hackathon.finservice.dto.UserRequest;
import com.hackathon.finservice.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "accounts", ignore = true)
    })
    User toEntity(UserRequest userRequest);

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "email", target = "email"),
            @Mapping(target = "accountNumber", ignore = true),
            @Mapping(target = "accountType", ignore = true)
    })
    UserResponse toResponse(User user);

    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);
}
