package com.volans.template.converter;

import com.volans.template.model.command.PassedVerificationCommand;
import com.volans.template.model.entity.PassedVerificationEntity;
import com.volans.template.model.enums.PassedVerificationType;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public abstract class PassedVerificationCommandToPassedVerificationEntityConverter implements Converter<PassedVerificationCommand, PassedVerificationEntity> {

    public abstract PassedVerificationEntity convert(PassedVerificationCommand source);

    public int passedVerificationTypeToInt(PassedVerificationType passedVerificationType) {
        return passedVerificationType.getValue();
    }

}
