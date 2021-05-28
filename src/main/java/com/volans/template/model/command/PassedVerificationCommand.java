package com.volans.template.model.command;


import com.volans.template.model.enums.PassedVerificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class PassedVerificationCommand {

    private Long id;

    private Long userId;

    private PassedVerificationType passedVerificationType;

}
