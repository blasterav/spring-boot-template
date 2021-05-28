package com.volans.template.component;

import com.volans.template.exception.NotFoundException;
import com.volans.template.exception.ServiceException;
import com.volans.template.model.StatusConstants;
import com.volans.template.model.command.MobileCommand;
import com.volans.template.model.command.UserCommand;
import com.volans.template.model.enums.UserLevel;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import com.volans.template.model.request.CreateUserRequest;
import com.volans.template.model.request.UpdateUserRequest;
import com.volans.template.service.persistence.UserPersistenceService;
import com.volans.template.service.util.UserUtilService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.volans.template.model.StatusConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserComponentTest {

    @InjectMocks
    private UserComponent userComponent;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private UserUtilService userUtilService;

    @Mock
    private ConversionService conversionService;

    @Test
    public void testCreateUser() {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        UserCommand userCommand = new UserCommand()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(conversionService.convert(actualRequest, UserCommand.class))
                .thenReturn(userCommand);

        UserCommand actualResponse = userComponent.createUser(actualRequest);

        //Check passing arguments of passedVerificationComponent.addPassedVerification
        ArgumentCaptor<UserCommand> userCommandArgumentCaptor = ArgumentCaptor.forClass(UserCommand.class);
        Mockito.verify(userPersistenceService, Mockito.times(1)).save(userCommandArgumentCaptor.capture());
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getCardId()).isEqualTo("cardId");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getType()).isEqualTo(UserType.USER);
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getDateOfBirth()).isEqualTo("11-11-1991");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getAge()).isEqualTo(18);
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getMobile().getMobileNumber()).isEqualTo("12345678901");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getMobile().getMobileBrand()).isEqualTo("Apple");

        //Check actual response
        Assertions.assertThat(actualResponse.getCardId()).isEqualTo("cardId");
        Assertions.assertThat(actualResponse.getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(actualResponse.getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(actualResponse.getType()).isEqualTo(UserType.USER);
        Assertions.assertThat(actualResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(actualResponse.getDateOfBirth()).isEqualTo("11-11-1991");
        Assertions.assertThat(actualResponse.getAge()).isEqualTo(18);
        Assertions.assertThat(actualResponse.getMobile().getMobileNumber()).isEqualTo("12345678901");
        Assertions.assertThat(actualResponse.getMobile().getMobileBrand()).isEqualTo("Apple");

    }

    @Test
    public void testUpdateUser() {
        long id = 1L;
        UpdateUserRequest updateUserRequest = new UpdateUserRequest()
                .setCardId("cardId1")
                .setFirstName("firstName1")
                .setSecondName("secondName1")
                .setType(UserType.MODERATOR.getValue())
                .setStatus(UserStatus.REGISTERED.getValue())
                .setDateOfBirth("11-11-1992")
                .setAge(19)
                .setMobileNumber("12345678902")
                .setMobileBrand("Google");

        UserCommand userCommand = new UserCommand()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.of(userCommand));

        UserCommand actualResponse = userComponent.updateUser(id, updateUserRequest);

        //Check passing arguments of passedVerificationComponent.addPassedVerification
        ArgumentCaptor<UserCommand> userCommandArgumentCaptor = ArgumentCaptor.forClass(UserCommand.class);
        Mockito.verify(userPersistenceService, Mockito.times(1)).save(userCommandArgumentCaptor.capture());
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getCardId()).isEqualTo("cardId1");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getFirstName()).isEqualTo("firstName1");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getSecondName()).isEqualTo("secondName1");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getType()).isEqualTo(UserType.MODERATOR);
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getStatus()).isEqualTo(UserStatus.REGISTERED);
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getDateOfBirth()).isEqualTo("11-11-1992");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getAge()).isEqualTo(19);
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getMobile().getMobileNumber()).isEqualTo("12345678902");
        Assertions.assertThat(userCommandArgumentCaptor.getValue().getMobile().getMobileBrand()).isEqualTo("Google");

        //Check actual response
        Assertions.assertThat(actualResponse.getCardId()).isEqualTo("cardId1");
        Assertions.assertThat(actualResponse.getFirstName()).isEqualTo("firstName1");
        Assertions.assertThat(actualResponse.getSecondName()).isEqualTo("secondName1");
        Assertions.assertThat(actualResponse.getType()).isEqualTo(UserType.MODERATOR);
        Assertions.assertThat(actualResponse.getStatus()).isEqualTo(UserStatus.REGISTERED);
        Assertions.assertThat(actualResponse.getDateOfBirth()).isEqualTo("11-11-1992");
        Assertions.assertThat(actualResponse.getAge()).isEqualTo(19);
        Assertions.assertThat(actualResponse.getMobile().getMobileNumber()).isEqualTo("12345678902");
        Assertions.assertThat(actualResponse.getMobile().getMobileBrand()).isEqualTo("Google");

        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
    }

    @Test
    public void testUpdateUser_userNotFound() {
        long id = 1L;
        UpdateUserRequest updateUserRequest = new UpdateUserRequest()
                .setCardId("cardId1")
                .setFirstName("firstName1")
                .setSecondName("secondName1")
                .setType(UserType.MODERATOR.getValue())
                .setStatus(UserStatus.REGISTERED.getValue())
                .setDateOfBirth("11-11-1992")
                .setAge(19)
                .setMobileNumber("12345678902")
                .setMobileBrand("Google");

        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userComponent.updateUser(id, updateUserRequest));
        Assertions.assertThat(notFoundException.getMessage()).isEqualTo(HttpConstants.USER_NOT_FOUND.getDesc());

        Mockito.verify(userPersistenceService, Mockito.never()).save(Mockito.any(UserCommand.class));
        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
    }

    @Test
    public void testUpdateUser_wrongType() {
        long id = 1L;
        UpdateUserRequest updateUserRequest = new UpdateUserRequest()
                .setCardId("cardId1")
                .setFirstName("firstName1")
                .setSecondName("secondName1")
                .setType("999")
                .setStatus(UserStatus.REGISTERED.getValue())
                .setDateOfBirth("11-11-1992")
                .setAge(19)
                .setMobileNumber("12345678902")
                .setMobileBrand("Google");

        UserCommand userCommand = new UserCommand()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.of(userCommand));

        ServiceException exception = assertThrows(ServiceException.class, () -> userComponent.updateUser(id, updateUserRequest));
        Assertions.assertThat(exception.getMessage()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getDesc());

        Mockito.verify(userPersistenceService, Mockito.never()).save(Mockito.any(UserCommand.class));
        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
    }

    @Test
    public void testUpdateUser_wrongStatus() {
        long id = 1L;
        UpdateUserRequest updateUserRequest = new UpdateUserRequest()
                .setCardId("cardId1")
                .setFirstName("firstName1")
                .setSecondName("secondName1")
                .setType(UserType.USER.getValue())
                .setStatus(999)
                .setDateOfBirth("11-11-1992")
                .setAge(19)
                .setMobileNumber("12345678902")
                .setMobileBrand("Google");

        UserCommand userCommand = new UserCommand()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.of(userCommand));

        ServiceException exception = assertThrows(ServiceException.class, () -> userComponent.updateUser(id, updateUserRequest));
        Assertions.assertThat(exception.getMessage()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getDesc());

        Mockito.verify(userPersistenceService, Mockito.never()).save(Mockito.any(UserCommand.class));
        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
    }

    @Test
    public void testGetUser() {
        long id = 1L;

        UserCommand userCommand = new UserCommand()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.of(userCommand));

        UserCommand actualResponse = userComponent.getUser(id);

        //Check actual response
        Assertions.assertThat(actualResponse.getCardId()).isEqualTo("cardId");
        Assertions.assertThat(actualResponse.getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(actualResponse.getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(actualResponse.getType()).isEqualTo(UserType.USER);
        Assertions.assertThat(actualResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(actualResponse.getDateOfBirth()).isEqualTo("11-11-1991");
        Assertions.assertThat(actualResponse.getAge()).isEqualTo(18);
        Assertions.assertThat(actualResponse.getMobile().getMobileNumber()).isEqualTo("12345678901");
        Assertions.assertThat(actualResponse.getMobile().getMobileBrand()).isEqualTo("Apple");

        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
    }

    @Test
    public void testGetUser_userNotFound() {
        long id = 1L;

        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userComponent.getUser(id));
        Assertions.assertThat(notFoundException.getMessage()).isEqualTo(HttpConstants.USER_NOT_FOUND.getDesc());

        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
    }

    @Test
    public void testGetUserList() {

        UserCommand userCommand1 = new UserCommand()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        UserCommand userCommand2 = new UserCommand()
                .setCardId("cardId2")
                .setFirstName("firstName2")
                .setSecondName("secondName2")
                .setType(UserType.MODERATOR)
                .setStatus(UserStatus.REGISTERED)
                .setDateOfBirth("11-11-1992")
                .setAge(19)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678902")
                        .setMobileBrand("Google"));
        Mockito.when(userPersistenceService.findAll())
                .thenReturn(Arrays.asList(userCommand1, userCommand2));

        List<UserCommand> userCommandList = userComponent.getUserList();

        //Check actual response
        Assertions.assertThat(userCommandList.size()).isEqualTo(2);

        Assertions.assertThat(userCommandList.get(0).getCardId()).isEqualTo("cardId");
        Assertions.assertThat(userCommandList.get(0).getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(userCommandList.get(0).getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(userCommandList.get(0).getType()).isEqualTo(UserType.USER);
        Assertions.assertThat(userCommandList.get(0).getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(userCommandList.get(0).getDateOfBirth()).isEqualTo("11-11-1991");
        Assertions.assertThat(userCommandList.get(0).getAge()).isEqualTo(18);
        Assertions.assertThat(userCommandList.get(0).getMobile().getMobileNumber()).isEqualTo("12345678901");
        Assertions.assertThat(userCommandList.get(0).getMobile().getMobileBrand()).isEqualTo("Apple");

        Assertions.assertThat(userCommandList.get(1).getCardId()).isEqualTo("cardId2");
        Assertions.assertThat(userCommandList.get(1).getFirstName()).isEqualTo("firstName2");
        Assertions.assertThat(userCommandList.get(1).getSecondName()).isEqualTo("secondName2");
        Assertions.assertThat(userCommandList.get(1).getType()).isEqualTo(UserType.MODERATOR);
        Assertions.assertThat(userCommandList.get(1).getStatus()).isEqualTo(UserStatus.REGISTERED);
        Assertions.assertThat(userCommandList.get(1).getDateOfBirth()).isEqualTo("11-11-1992");
        Assertions.assertThat(userCommandList.get(1).getAge()).isEqualTo(19);
        Assertions.assertThat(userCommandList.get(1).getMobile().getMobileNumber()).isEqualTo("12345678902");
        Assertions.assertThat(userCommandList.get(1).getMobile().getMobileBrand()).isEqualTo("Google");

        Mockito.verify(userPersistenceService, Mockito.times(1)).findAll();
    }

    @Test
    public void testDeleteUser() {
        long id = 1L;

        UserCommand userCommand = new UserCommand()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.of(userCommand));

        userComponent.deleteUser(id);

        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
        Mockito.verify(userPersistenceService, Mockito.times(1)).delete(id);

    }

    @Test
    public void testDeleteUser_userNotFound() {
        long id = 1L;

        Mockito.when(userPersistenceService.findById(id))
                .thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userComponent.deleteUser(id));
        Assertions.assertThat(notFoundException.getMessage()).isEqualTo(HttpConstants.USER_NOT_FOUND.getDesc());

        Mockito.verify(userPersistenceService, Mockito.times(1)).findById(id);
        Mockito.verify(userPersistenceService, Mockito.never()).delete(id);
    }

    @Test
    public void testCalculateLevel() {
        long id = 1L;

        Mockito.when(userUtilService.calculateUserLevel(id))
                .thenReturn(UserLevel.LEVEL_1);

        UserLevel userLevel = userComponent.calculateUserLevel(id);

        Assertions.assertThat(userLevel).isEqualTo(UserLevel.LEVEL_1);

        Mockito.verify(userUtilService, Mockito.times(1)).calculateUserLevel(id);

    }

}