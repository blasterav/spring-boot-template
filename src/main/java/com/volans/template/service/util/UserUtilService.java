package com.volans.template.service.util;

import com.volans.template.exception.ServiceException;
import com.volans.template.model.StatusConstants;
import com.volans.template.model.command.ExternalUserCommand;
import com.volans.template.model.command.UserCommand;
import com.volans.template.model.enums.UserLevel;
import com.volans.template.service.MessagePublisherService;
import com.volans.template.service.connector.ExternalUserConnectorService;
import com.volans.template.service.persistence.UserPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUtilService {

    private final UserPersistenceService userPersistenceService;
    private final MessagePublisherService messagePublisherService;
    private final ExternalUserConnectorService externalUserConnectorService;
    private final ConversionService conversionService;

    public UserLevel calculateUserLevel(Long id) {
        UserCommand userCommand = userPersistenceService.findById(id)
                .orElseThrow(() -> new ServiceException(StatusConstants.HttpConstants.USER_NOT_FOUND));
        return this.calculateUserLevel(userCommand);
    }

    public UserLevel calculateUserLevel(UserCommand userCommand) {
        userCommand.setLevel(UserLevel.LEVEL_2);
        userPersistenceService.save(userCommand);
        messagePublisherService.publishMessage(userCommand);
        externalUserConnectorService.updateExternalUser(conversionService.convert(userCommand, ExternalUserCommand.class));
        return userCommand.getLevel();
    }

}
