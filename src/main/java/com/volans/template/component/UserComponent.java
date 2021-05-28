package com.volans.template.component;

import com.volans.template.exception.NotFoundException;
import com.volans.template.exception.ServiceException;
import com.volans.template.model.command.UserCommand;
import com.volans.template.model.enums.UserLevel;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import com.volans.template.model.request.CreateUserRequest;
import com.volans.template.model.request.UpdateUserRequest;
import com.volans.template.service.persistence.UserPersistenceService;
import com.volans.template.service.util.UserUtilService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.volans.template.model.StatusConstants.HttpConstants;

@Component
@RequiredArgsConstructor
public class UserComponent {

    private final UserPersistenceService userPersistenceService;
    private final UserUtilService userUtilService;
    private final ConversionService conversionService;

    public UserCommand createUser(CreateUserRequest request) {
        UserCommand userCommand = conversionService.convert(request, UserCommand.class);
        userCommand.setLevel(UserLevel.LEVEL_1);
        userPersistenceService.save(userCommand);
        return userCommand;
    }

    public UserCommand updateUser(long id, UpdateUserRequest request) {
        UserCommand userCommand = userPersistenceService.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpConstants.USER_NOT_FOUND));
        if (StringUtils.isNotBlank(request.getCardId())) {
            userCommand.setCardId(request.getCardId());
        }
        if (StringUtils.isNotBlank(request.getFirstName())) {
            userCommand.setFirstName(request.getFirstName());
        }
        if (StringUtils.isNotBlank(request.getSecondName())) {
            userCommand.setSecondName(request.getSecondName());
        }
        if (StringUtils.isNotBlank(request.getType())) {
            userCommand.setType(UserType.find(request.getType())
            .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM)));
        }
        if (request.getStatus() != null) {
            userCommand.setStatus(UserStatus.find(request.getStatus())
                    .orElseThrow(() -> new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM)));
        }
        if (StringUtils.isNotBlank(request.getDateOfBirth())) {
            userCommand.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAge() != null) {
            userCommand.setAge(request.getAge());
        }
        if (StringUtils.isNotBlank(request.getMobileNumber())) {
            userCommand.getMobile().setMobileNumber(request.getMobileNumber());
        }
        if (StringUtils.isNotBlank(request.getMobileBrand())) {
            userCommand.getMobile().setMobileBrand(request.getMobileBrand());
        }
        userPersistenceService.save(userCommand);
        return userCommand;
    }

    public UserCommand getUser(Long id) {
        return userPersistenceService.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpConstants.USER_NOT_FOUND));
    }

    public List<UserCommand> getUserList() {
        return userPersistenceService.findAll();
    }

    public void deleteUser(long id) {
        userPersistenceService.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpConstants.USER_NOT_FOUND));
        userPersistenceService.delete(id);
    }

    public UserLevel calculateUserLevel(Long id) {
        return userUtilService.calculateUserLevel(id);
    }
}
