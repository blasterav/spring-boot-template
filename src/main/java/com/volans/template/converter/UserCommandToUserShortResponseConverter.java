package com.volans.template.converter;

import com.volans.template.model.command.UserCommand;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import com.volans.template.model.response.UserShortResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public abstract class UserCommandToUserShortResponseConverter implements Converter<UserCommand, UserShortResponse> {

    public abstract UserShortResponse convert(UserCommand source);

    public int userStatusToInt(UserStatus userStatus) {
        return userStatus.getValue();
    }

    public String userTypeToString(UserType userType) {
        return userType.getValue();
    }

}
