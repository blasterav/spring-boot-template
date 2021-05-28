package com.volans.template.model.command;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MobileCommand {

    private Long id;

    private Long userId;

    private String mobileNumber;

    private String mobileBrand;

}
