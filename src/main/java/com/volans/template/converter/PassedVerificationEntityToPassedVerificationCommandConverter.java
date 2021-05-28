package com.volans.template.converter;

import com.volans.template.exception.ServiceException;
import com.volans.template.model.command.PassedVerificationCommand;
import com.volans.template.model.entity.PassedVerificationEntity;
import com.volans.template.model.enums.PassedVerificationType;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import static com.volans.template.model.StatusConstants.HttpConstants;

@Mapper(componentModel = "spring")
public abstract class PassedVerificationEntityToPassedVerificationCommandConverter implements Converter<PassedVerificationEntity, PassedVerificationCommand> {

    public abstract PassedVerificationCommand convert(PassedVerificationEntity source);

    public PassedVerificationType intToPassedVerificationType(int id) {
        return PassedVerificationType.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

}
