package com.volans.template.converter;

import com.volans.template.model.command.ExternalUserCommand;
import com.volans.template.model.command.UserCommand;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public abstract class UserCommandToExternalUserCommandConverter implements Converter<UserCommand, ExternalUserCommand> {

    public abstract ExternalUserCommand convert(UserCommand source);

}
