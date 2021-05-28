package com.volans.template.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.volans.template.annotations.Required;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.volans.template.model.StatusConstants.HttpConstants;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddPassedVerificationRequest {

    @Required(exception = HttpConstants.USER_ID_IS_REQUIRED)
    private Long userId;

    @Required(exception = HttpConstants.PASSED_VERIFICATION_TYPE_IS_REQUIRED)
    private Integer passedVerificationType;

}
