package com.volans.template.service.connector;

import com.volans.template.model.command.ExternalUserCommand;
import org.springframework.stereotype.Service;

@Service
public class ExternalUserConnectorService {

    public void updateExternalUser(ExternalUserCommand externalUserCommand) {

    }

    public ExternalUserCommand getExternalUser(String cardId) {
        return new ExternalUserCommand();
    }

}
