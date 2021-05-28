package com.volans.template.service.util;

import com.volans.template.model.command.PassedVerificationCommand;
import com.volans.template.service.persistence.PassedVerificationPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PassedVerificationUtilService {

    private final PassedVerificationPersistenceService passedVerificationPersistenceService;

    public void addPassedVerification(PassedVerificationCommand passedVerificationType, Consumer<Long> consumer) {
        passedVerificationPersistenceService.save(passedVerificationType);
        consumer.accept(passedVerificationType.getUserId());
    }
}
