package com.hackathon.finservice.data.mapper;

import com.hackathon.finservice.data.entity.User;
import com.hackathon.finservice.dto.UserRequest;
import com.hackathon.finservice.dto.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;


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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "accounts", ignore = true)
    })
    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);
}
