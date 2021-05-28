package com.volans.template.component;

import com.volans.template.model.command.PassedVerificationCommand;
import com.volans.template.model.enums.PassedVerificationType;
import com.volans.template.model.request.AddPassedVerificationRequest;
import com.volans.template.service.util.PassedVerificationUtilService;
import com.volans.template.service.util.UserUtilService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.function.Consumer;

@ExtendWith(MockitoExtension.class)
class PassedVerificationComponentTest {

    @InjectMocks
    private PassedVerificationComponent passedVerificationComponent;

    @Mock
    private PassedVerificationUtilService passedVerificationUtilService;

    @Mock
    private UserUtilService userUtilService;

    @Mock
    private ConversionService conversionService;

    @ParameterizedTest
    @EnumSource
    public void testAddPassedVerification(PassedVerificationType passedVerificationType) {
        AddPassedVerificationRequest request = new AddPassedVerificationRequest()
                .setUserId(1L)
                .setPassedVerificationType(passedVerificationType.getValue());

        PassedVerificationCommand passedVerificationCommand = new PassedVerificationCommand()
                .setUserId(1L)
                .setPassedVerificationType(passedVerificationType);

        Mockito.when(conversionService.convert(request, PassedVerificationCommand.class))
                .thenReturn(passedVerificationCommand);

        passedVerificationComponent.addPassedVerification(request);

        //Check passing arguments of passedVerificationComponent.addPassedVerification
        ArgumentCaptor<PassedVerificationCommand> passedVerificationCommandArgumentCaptor = ArgumentCaptor.forClass(PassedVerificationCommand.class);
        Mockito.verify(passedVerificationUtilService, Mockito.times(1)).addPassedVerification(passedVerificationCommandArgumentCaptor.capture(), Mockito.any(Consumer.class));
        Assertions.assertThat(passedVerificationCommandArgumentCaptor.getValue().getUserId()).isEqualTo(1L);
        Assertions.assertThat(passedVerificationCommandArgumentCaptor.getValue().getPassedVerificationType().getValue()).isEqualTo(passedVerificationType.getValue());

        Mockito.verify(conversionService, Mockito.times(1)).convert(request, PassedVerificationCommand.class);

    }
}