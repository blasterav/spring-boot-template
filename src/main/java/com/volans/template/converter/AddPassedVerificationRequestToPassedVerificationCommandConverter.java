package com.volans.template.converter;

import com.volans.template.exception.ServiceException;
import com.volans.template.model.command.PassedVerificationCommand;
import com.volans.template.model.enums.PassedVerificationType;
import com.volans.template.model.request.AddPassedVerificationRequest;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import static com.volans.template.model.StatusConstants.HttpConstants;

@Mapper(componentModel = "spring")
public abstract class AddPassedVerificationRequestToPassedVerificationCommandConverter implements Converter<AddPassedVerificationRequest, PassedVerificationCommand> {

    public abstract PassedVerificationCommand convert(AddPassedVerificationRequest source);

    public PassedVerificationType intToPassedVerificationType(int id) {
        return PassedVerificationType.find(id)
                .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));
    }

}
