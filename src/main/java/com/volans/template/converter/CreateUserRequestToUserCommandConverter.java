package com.volans.template.converter;

import com.volans.template.exception.ServiceException;
import com.volans.template.model.command.UserCommand;
import com.volans.template.model.enums.UserLevel;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import com.volans.template.model.request.CreateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import static com.volans.template.model.StatusConstants.HttpConstants;

@Mapper(componentModel = "spring")
public abstract class CreateUserRequestToUserCommandConverter implements Converter<CreateUserRequest, UserCommand> {

    @Mapping(source = "mobileNumber", target = "mobile.mobileNumber")
    @Mapping(source = "mobileBrand", target = "mobile.mobileBrand")
    public abstract UserCommand convert(CreateUserRequest source);

    public UserStatus intToUserStatus(Integer id) {
        return UserStatus.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

    public UserType stringToUserType(String id) {
        return UserType.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

    public UserLevel intToUserLevel(Integer id) {
        return UserLevel.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

}
