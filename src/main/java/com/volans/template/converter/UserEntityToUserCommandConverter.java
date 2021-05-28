package com.volans.template.converter;

import com.volans.template.exception.ServiceException;
import com.volans.template.model.command.UserCommand;
import com.volans.template.model.entity.UserEntity;
import com.volans.template.model.enums.UserLevel;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import static com.volans.template.model.StatusConstants.HttpConstants;

@Mapper(componentModel = "spring")
public abstract class UserEntityToUserCommandConverter implements Converter<UserEntity, UserCommand> {

    public abstract UserCommand convert(UserEntity source);

    public UserStatus intToUserStatus(int id) {
        return UserStatus.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

    public UserType stringToUserType(String id) {
        return UserType.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

    public UserLevel intToUserLevel(int id) {
        return UserLevel.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

}
