package com.volans.template.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volans.template.component.PassedVerificationComponent;
import com.volans.template.model.Response;
import com.volans.template.model.enums.PassedVerificationType;
import com.volans.template.model.request.AddPassedVerificationRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.volans.template.model.StatusConstants.HttpConstants;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PassedVerificationController.class)
class PassedVerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassedVerificationComponent passedVerificationComponent;

    @ParameterizedTest
    @EnumSource
    @DisplayName("Add passed verification - success")
    public void testAddPassedVerification_success(PassedVerificationType passedVerificationType) throws Exception {
        AddPassedVerificationRequest actualRequest = new AddPassedVerificationRequest()
                .setUserId(1L)
                .setPassedVerificationType(passedVerificationType.getValue());

        MockHttpServletResponse response = mockMvc.perform(post("/v1/passed-verifications")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        //Check passing arguments of passedVerificationComponent.addPassedVerification
        ArgumentCaptor<AddPassedVerificationRequest> addPassedVerificationRequestArgumentCaptor = ArgumentCaptor.forClass(AddPassedVerificationRequest.class);
        Mockito.verify(passedVerificationComponent, Mockito.times(1)).addPassedVerification(addPassedVerificationRequestArgumentCaptor.capture());
        Assertions.assertThat(addPassedVerificationRequestArgumentCaptor.getValue().getUserId()).isEqualTo(1L);
        Assertions.assertThat(addPassedVerificationRequestArgumentCaptor.getValue().getPassedVerificationType()).isEqualTo(passedVerificationType.getValue());

        //Check actual response
        TypeReference<Response<Void>> typeReference = new TypeReference<Response<Void>>(){};
        Response<Void> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.SUCCESS.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.SUCCESS.getDesc());
        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Add passed verification - user_id is required")
    public void testAddPassedVerification_userIdIsRequired() throws Exception {
        AddPassedVerificationRequest actualRequest = new AddPassedVerificationRequest()
                .setPassedVerificationType(PassedVerificationType.TYPE_1.getValue());

        MockHttpServletResponse response = mockMvc.perform(post("/v1/passed-verifications")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(passedVerificationComponent, Mockito.never()).addPassedVerification(Mockito.any(AddPassedVerificationRequest.class));

        //Check actual response
        TypeReference<Response<Void>> typeReference = new TypeReference<Response<Void>>(){};
        Response<Void> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.USER_ID_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.USER_ID_IS_REQUIRED.getDesc());
        Assertions.assertThat(actualResponse.getData()).isNull();

    }

    @Test
    @DisplayName("Add passed verification - passed_verification_type is required")
    public void testAddPassedVerification_passedVerificationTypeIsRequired() throws Exception {
        AddPassedVerificationRequest actualRequest = new AddPassedVerificationRequest()
                .setUserId(1L);

        MockHttpServletResponse response = mockMvc.perform(post("/v1/passed-verifications")
                .content(objectMapper.writeValueAsString(actualRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        Mockito.verify(passedVerificationComponent, Mockito.never()).addPassedVerification(Mockito.any(AddPassedVerificationRequest.class));

        //Check actual response
        TypeReference<Response<Void>> typeReference = new TypeReference<Response<Void>>(){};
        Response<Void> actualResponse = objectMapper.readValue(response.getContentAsString(), typeReference);
        Assertions.assertThat(actualResponse.getStatus().getCode()).isEqualTo(HttpConstants.PASSED_VERIFICATION_TYPE_IS_REQUIRED.getCode());
        Assertions.assertThat(actualResponse.getStatus().getMessage()).isEqualTo(HttpConstants.PASSED_VERIFICATION_TYPE_IS_REQUIRED.getDesc());
        Assertions.assertThat(actualResponse.getData()).isNull();

    }

}