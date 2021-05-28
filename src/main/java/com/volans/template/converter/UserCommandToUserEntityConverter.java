package com.volans.template.converter;

import com.volans.template.model.command.UserCommand;
import com.volans.template.model.entity.UserEntity;
import com.volans.template.model.enums.UserLevel;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public abstract class UserCommandToUserEntityConverter implements Converter<UserCommand, UserEntity> {

    public abstract UserEntity convert(UserCommand source);

    public int userStatusToInt(UserStatus userStatus) {
        return userStatus.getValue();
    }

    public String userTypeToString(UserType userType) {
        return userType.getValue();
    }

    public int userLevelToInt(UserLevel userLevel) {
        return userLevel.getValue();
    }

}
