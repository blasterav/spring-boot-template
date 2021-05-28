package com.volans.template.component;

import com.volans.template.model.command.PassedVerificationCommand;
import com.volans.template.model.request.AddPassedVerificationRequest;
import com.volans.template.service.util.PassedVerificationUtilService;
import com.volans.template.service.util.UserUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassedVerificationComponent {

    private final PassedVerificationUtilService passedVerificationUtilService;
    private final UserUtilService userUtilService;
    private final ConversionService conversionService;

    public void addPassedVerification(AddPassedVerificationRequest request) {
        PassedVerificationCommand passedVerificationCommand = conversionService.convert(request, PassedVerificationCommand.class);
        passedVerificationUtilService.addPassedVerification(passedVerificationCommand, userUtilService::calculateUserLevel);
    }

}
