package com.volans.template.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MobileEntity {

    private Long id;

    private Long userId;

    private String mobileNumber;

    private String mobileBrand;

}
