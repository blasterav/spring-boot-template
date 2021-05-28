package com.volans.template.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volans.template.component.UserComponent;
import com.volans.template.exception.NotFoundException;
import com.volans.template.exception.ServiceException;
import com.volans.template.model.Response;
import com.volans.template.model.command.MobileCommand;
import com.volans.template.model.command.UserCommand;
import com.volans.template.model.enums.UserLevel;
import com.volans.template.model.enums.UserStatus;
import com.volans.template.model.enums.UserType;
import com.volans.template.model.request.CreateUserRequest;
import com.volans.template.model.request.UpdateUserRequest;
import com.volans.template.model.response.CalculateUserLevelResponse;
import com.volans.template.model.response.UserResponse;
import com.volans.template.model.response.UserShortResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.volans.template.model.StatusConstants.HttpConstants;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserComponent userComponent;

    @Autowired
    private ConversionService conversionService;

    @Test
    @DisplayName("Create user - success")
    public void testCreateUser_success() throws Exception {
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
                .setId(1L)
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setLevel(UserLevel.LEVEL_1)
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userComponent.createUser(Mockito.any(CreateUserRequest.class)))
                .thenReturn(userCommand);

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SUCCESS.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SUCCESS.getDesc());

        Assertions.assertThat(actualResponse.getData().getId()).isEqualTo(1L);
        Assertions.assertThat(actualResponse.getData().getCardId()).isEqualTo("cardId");
        Assertions.assertThat(actualResponse.getData().getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(actualResponse.getData().getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(actualResponse.getData().getType()).isEqualTo(UserType.USER.getValue());
        Assertions.assertThat(actualResponse.getData().getStatus()).isEqualTo(UserStatus.ACTIVE.getValue());
        Assertions.assertThat(actualResponse.getData().getLevel()).isEqualTo(UserLevel.LEVEL_1.getValue());
        Assertions.assertThat(actualResponse.getData().getAge()).isEqualTo(18);
        Assertions.assertThat(actualResponse.getData().getMobile().getMobileNumber()).isEqualTo("12345678901");
        Assertions.assertThat(actualResponse.getData().getMobile().getMobileBrand()).isEqualTo("Apple");

    }

    @Test
    @DisplayName("Create user - ServiceException")
    public void testCreateUser_ServiceException() throws Exception {
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

        Mockito.when(userComponent.createUser(Mockito.any(CreateUserRequest.class)))
                .thenThrow(new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - Exception")
    public void testCreateUser_Exception() throws Exception {
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

        Mockito.doThrow(new RuntimeException()).when(userComponent).createUser(Mockito.any(CreateUserRequest.class));

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }


    @Test
    @DisplayName("Create user - card_id is required")
    public void testCreateUser_cardIdIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.CARD_ID_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.CARD_ID_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - first_name is required")
    public void testCreateUser_firstNameIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.FIRST_NAME_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.FIRST_NAME_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - second_name is required")
    public void testCreateUser_secondNameIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SECOND_NAME_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SECOND_NAME_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - type is required")
    public void testCreateUser_typeIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.TYPE_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.TYPE_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - status is required")
    public void testCreateUser_statusIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.STATUS_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.STATUS_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - date_of_birth is required")
    public void testCreateUser_dateOfBirthIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.DATE_OF_BIRTH_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.DATE_OF_BIRTH_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - age is required")
    public void testCreateUser_ageIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.AGE_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.AGE_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - mobile_number is required")
    public void testCreateUser_mobileNumberIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.MOBILE_NUMBER_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.MOBILE_NUMBER_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - mobile_brand is required")
    public void testCreateUser_mobileBrandIsRequired() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.MOBILE_BRAND_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.MOBILE_BRAND_IS_REQUIRED.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }


    @Test
    @DisplayName("Create user - type is invalid")
    public void testCreateUser_typeIsInvalid() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType("wrong")
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.TYPE_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.TYPE_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - status is invalid")
    public void testCreateUser_statusIsInvalid() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(99)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.STATUS_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.STATUS_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - date_of_birth is invalid")
    public void testCreateUser_dateOfBirthIsInvalid() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11/11/1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.DATE_OF_BIRTH_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.DATE_OF_BIRTH_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - age is invalid (MIN)")
    public void testCreateUser_ageIsInvalidMin() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(14)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.AGE_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.AGE_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Create user - age is invalid (MAX)")
    public void testCreateUser_ageIsInvalidMax() throws Exception {
        CreateUserRequest actualRequest = new CreateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(99)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).createUser(Mockito.any(CreateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.AGE_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.AGE_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Update user - success")
    public void testUpdateUser_success() throws Exception {
        UpdateUserRequest actualRequest = new UpdateUserRequest()
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
                .setId(1L)
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setLevel(UserLevel.LEVEL_1)
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userComponent.updateUser(Mockito.eq(1L), Mockito.any(UpdateUserRequest.class)))
                .thenReturn(userCommand);

        MockHttpServletResponse response = mockMvc.perform(put("/v1/users/1")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).updateUser(Mockito.eq(1L), Mockito.any(UpdateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SUCCESS.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SUCCESS.getDesc());

        Assertions.assertThat(actualResponse.getData().getId()).isEqualTo(1L);
        Assertions.assertThat(actualResponse.getData().getCardId()).isEqualTo("cardId");
        Assertions.assertThat(actualResponse.getData().getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(actualResponse.getData().getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(actualResponse.getData().getType()).isEqualTo(UserType.USER.getValue());
        Assertions.assertThat(actualResponse.getData().getStatus()).isEqualTo(UserStatus.ACTIVE.getValue());
        Assertions.assertThat(actualResponse.getData().getLevel()).isEqualTo(UserLevel.LEVEL_1.getValue());
        Assertions.assertThat(actualResponse.getData().getAge()).isEqualTo(18);
        Assertions.assertThat(actualResponse.getData().getMobile().getMobileNumber()).isEqualTo("12345678901");
        Assertions.assertThat(actualResponse.getData().getMobile().getMobileBrand()).isEqualTo("Apple");

    }

    @Test
    @DisplayName("Update user - ServiceException")
    public void testUpdateUser_ServiceException() throws Exception {
        UpdateUserRequest actualRequest = new UpdateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        Mockito.when(userComponent.updateUser(Mockito.eq(1L), Mockito.any(UpdateUserRequest.class)))
                .thenThrow(new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));

        MockHttpServletResponse response = mockMvc.perform(put("/v1/users/1")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).updateUser(Mockito.eq(1L), Mockito.any(UpdateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Update user - Exception")
    public void testUpdateUser_Exception() throws Exception {
        UpdateUserRequest actualRequest = new UpdateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        Mockito.when(userComponent.updateUser(Mockito.eq(1L), Mockito.any(UpdateUserRequest.class)))
                .thenThrow(new RuntimeException());

        MockHttpServletResponse response = mockMvc.perform(put("/v1/users/1")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).updateUser(Mockito.eq(1L), Mockito.any(UpdateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Update user - type is invalid")
    public void testUpdateUser_typeIsInvalid() throws Exception {
        UpdateUserRequest actualRequest = new UpdateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType("wrong")
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(put("/v1/users/1")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).updateUser(Mockito.anyLong(), Mockito.any(UpdateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.TYPE_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.TYPE_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Update user - status is invalid")
    public void testUpdateUser_statusIsInvalid() throws Exception {
        UpdateUserRequest actualRequest = new UpdateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(99)
                .setDateOfBirth("11-11-1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(put("/v1/users/1")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).updateUser(Mockito.anyLong(), Mockito.any(UpdateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.STATUS_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.STATUS_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Update user - date_of_birth is invalid")
    public void testUpdateUser_dateOfBirthIsInvalid() throws Exception {
        UpdateUserRequest actualRequest = new UpdateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11/11/1991")
                .setAge(18)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(put("/v1/users/1")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).updateUser(Mockito.anyLong(), Mockito.any(UpdateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.DATE_OF_BIRTH_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.DATE_OF_BIRTH_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Update user - age is invalid (MIN)")
    public void testUpdateUser_ageIsInvalidMin() throws Exception {
        UpdateUserRequest actualRequest = new UpdateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(14)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(put("/v1/users/1")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).updateUser(Mockito.anyLong(), Mockito.any(UpdateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.AGE_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.AGE_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Update user - age is invalid (MAX)")
    public void testUpdateUser_ageIsInvalidMax() throws Exception {
        UpdateUserRequest actualRequest = new UpdateUserRequest()
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER.getValue())
                .setStatus(UserStatus.ACTIVE.getValue())
                .setDateOfBirth("11-11-1991")
                .setAge(99)
                .setMobileNumber("12345678901")
                .setMobileBrand("Apple");

        MockHttpServletResponse response = mockMvc.perform(put("/v1/users/1")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.never()).updateUser(Mockito.anyLong(), Mockito.any(UpdateUserRequest.class));

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.AGE_IS_INVALID.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.AGE_IS_INVALID.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Get user - success")
    public void testGetUser_success() throws Exception {
        UserCommand userCommand = new UserCommand()
                .setId(1L)
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setLevel(UserLevel.LEVEL_1)
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userComponent.getUser(Mockito.eq(1L)))
                .thenReturn(userCommand);

        MockHttpServletResponse response = mockMvc.perform(get("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).getUser(Mockito.anyLong());

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SUCCESS.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SUCCESS.getDesc());

        Assertions.assertThat(actualResponse.getData().getId()).isEqualTo(1L);
        Assertions.assertThat(actualResponse.getData().getCardId()).isEqualTo("cardId");
        Assertions.assertThat(actualResponse.getData().getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(actualResponse.getData().getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(actualResponse.getData().getType()).isEqualTo(UserType.USER.getValue());
        Assertions.assertThat(actualResponse.getData().getStatus()).isEqualTo(UserStatus.ACTIVE.getValue());
        Assertions.assertThat(actualResponse.getData().getLevel()).isEqualTo(UserLevel.LEVEL_1.getValue());
        Assertions.assertThat(actualResponse.getData().getAge()).isEqualTo(18);
        Assertions.assertThat(actualResponse.getData().getMobile().getMobileNumber()).isEqualTo("12345678901");
        Assertions.assertThat(actualResponse.getData().getMobile().getMobileBrand()).isEqualTo("Apple");
    }

    @Test
    @DisplayName("Get user - NotFoundException")
    public void testGetUser_NotFoundException() throws Exception {

        Mockito.when(userComponent.getUser(Mockito.eq(1L)))
                .thenThrow(new NotFoundException(HttpConstants.USER_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(get("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).getUser(Mockito.anyLong());

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.USER_NOT_FOUND.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.USER_NOT_FOUND.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Get user - ServiceException")
    public void testGetUser_ServiceException() throws Exception {
        Mockito.when(userComponent.getUser(Mockito.eq(1L)))
                .thenThrow(new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));

        MockHttpServletResponse response = mockMvc.perform(get("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).getUser(Mockito.anyLong());

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Get user - Exception")
    public void testGetUser_Exception() throws Exception {
        Mockito.when(userComponent.getUser(Mockito.eq(1L)))
                .thenThrow(new RuntimeException());

        MockHttpServletResponse response = mockMvc.perform(get("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).getUser(Mockito.anyLong());

        //Check actual response
        TypeReference<Response<UserResponse>> typeReference = new TypeReference<Response<UserResponse>>() {
        };
        Response<UserResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Get user list - success")
    public void testGetUserList_success() throws Exception {
        UserCommand userCommand1 = new UserCommand()
                .setId(1L)
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setLevel(UserLevel.LEVEL_1)
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        UserCommand userCommand2 = new UserCommand()
                .setId(2L)
                .setCardId("cardId")
                .setFirstName("firstName")
                .setSecondName("secondName")
                .setType(UserType.USER)
                .setStatus(UserStatus.ACTIVE)
                .setLevel(UserLevel.LEVEL_1)
                .setAge(18)
                .setMobile(new MobileCommand()
                        .setMobileNumber("12345678901")
                        .setMobileBrand("Apple"));

        Mockito.when(userComponent.getUserList())
                .thenReturn(Arrays.asList(userCommand1, userCommand2));

        MockHttpServletResponse response = mockMvc.perform(get("/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).getUserList();

        //Check actual response
        TypeReference<Response<List<UserShortResponse>>> typeReference = new TypeReference<Response<List<UserShortResponse>>>() {
        };
        Response<List<UserShortResponse>> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SUCCESS.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SUCCESS.getDesc());

        Assertions.assertThat(actualResponse.getData().size()).isEqualTo(2);
        Assertions.assertThat(actualResponse.getData().get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(actualResponse.getData().get(0).getCardId()).isEqualTo("cardId");
        Assertions.assertThat(actualResponse.getData().get(0).getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(actualResponse.getData().get(0).getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(actualResponse.getData().get(0).getType()).isEqualTo(UserType.USER.getValue());
        Assertions.assertThat(actualResponse.getData().get(0).getStatus()).isEqualTo(UserStatus.ACTIVE.getValue());
        Assertions.assertThat(actualResponse.getData().get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(actualResponse.getData().get(1).getCardId()).isEqualTo("cardId");
        Assertions.assertThat(actualResponse.getData().get(1).getFirstName()).isEqualTo("firstName");
        Assertions.assertThat(actualResponse.getData().get(1).getSecondName()).isEqualTo("secondName");
        Assertions.assertThat(actualResponse.getData().get(1).getType()).isEqualTo(UserType.USER.getValue());
        Assertions.assertThat(actualResponse.getData().get(1).getStatus()).isEqualTo(UserStatus.ACTIVE.getValue());
    }

    @Test
    @DisplayName("Get user list - ServiceException")
    public void testGetUserList_ServiceException() throws Exception {

        Mockito.when(userComponent.getUserList())
                .thenThrow(new ServiceException(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM));

        MockHttpServletResponse response = mockMvc.perform(get("/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).getUserList();

        //Check actual response
        TypeReference<Response<List<UserShortResponse>>> typeReference = new TypeReference<Response<List<UserShortResponse>>>() {
        };
        Response<List<UserShortResponse>> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.FAILED_TO_CONVERT_VALUE_TO_ENUM.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Get user list - Exception")
    public void testGetUserList_Exception() throws Exception {

        Mockito.when(userComponent.getUserList())
                .thenThrow(new RuntimeException());

        MockHttpServletResponse response = mockMvc.perform(get("/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).getUserList();

        //Check actual response
        TypeReference<Response<List<UserShortResponse>>> typeReference = new TypeReference<Response<List<UserShortResponse>>>() {
        };
        Response<List<UserShortResponse>> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Delete user - success")
    public void testDeleteUser_success() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(delete("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).deleteUser(1L);

        //Check actual response
        TypeReference<Response<Void>> typeReference = new TypeReference<Response<Void>>() {
        };
        Response<Void> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SUCCESS.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SUCCESS.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Delete user - NotFoundException")
    public void testDeleteUser_NotFoundException() throws Exception {

        Mockito.doThrow(new NotFoundException(HttpConstants.USER_NOT_FOUND))
                .when(userComponent).deleteUser(1L);

        MockHttpServletResponse response = mockMvc.perform(delete("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).deleteUser(1L);

        //Check actual response
        TypeReference<Response<Void>> typeReference = new TypeReference<Response<Void>>() {
        };
        Response<Void> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.USER_NOT_FOUND.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.USER_NOT_FOUND.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Delete user - Exception")
    public void testDeleteUser_Exception() throws Exception {

        Mockito.doThrow(new RuntimeException())
                .when(userComponent).deleteUser(1L);

        MockHttpServletResponse response = mockMvc.perform(delete("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).deleteUser(1L);

        //Check actual response
        TypeReference<Response<Void>> typeReference = new TypeReference<Response<Void>>() {
        };
        Response<Void> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Calculate user level - success")
    public void testCalculateUserLevel_success() throws Exception {

        Mockito.when(userComponent.calculateUserLevel(1L))
                .thenReturn(UserLevel.LEVEL_2);

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users/1/calculate-level")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).calculateUserLevel(1L);

        //Check actual response
        TypeReference<Response<CalculateUserLevelResponse>> typeReference = new TypeReference<Response<CalculateUserLevelResponse>>() {
        };
        Response<CalculateUserLevelResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SUCCESS.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SUCCESS.getDesc());

        Assertions.assertThat(actualResponse.getData().getLevel()).isEqualTo(UserLevel.LEVEL_2.getValue());
    }

    @Test
    @DisplayName("Calculate user level - ServiceException")
    public void testCalculateUserLevel_ServiceException() throws Exception {

        Mockito.when(userComponent.calculateUserLevel(1L))
                .thenThrow(new ServiceException(HttpConstants.USER_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users/1/calculate-level")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).calculateUserLevel(1L);

        //Check actual response
        TypeReference<Response<CalculateUserLevelResponse>> typeReference = new TypeReference<Response<CalculateUserLevelResponse>>() {
        };
        Response<CalculateUserLevelResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.USER_NOT_FOUND.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.USER_NOT_FOUND.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

    @Test
    @DisplayName("Calculate user level - Exception")
    public void testCalculateUserLevel_Exception() throws Exception {

        Mockito.when(userComponent.calculateUserLevel(1L))
                .thenThrow(new RuntimeException());

        MockHttpServletResponse response = mockMvc.perform(post("/v1/users/1/calculate-level")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        Mockito.verify(userComponent, Mockito.times(1)).calculateUserLevel(1L);

        //Check actual response
        TypeReference<Response<CalculateUserLevelResponse>> typeReference = new TypeReference<Response<CalculateUserLevelResponse>>() {
        };
        Response<CalculateUserLevelResponse> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.INTERNAL_SERVER_ERROR.getDesc());

        Assertions.assertThat(actualResponse.getData()).isNull();
    }

}