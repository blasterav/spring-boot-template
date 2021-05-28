package com.volans.template.converter;

import com.volans.template.model.command.MobileCommand;
import com.volans.template.model.response.MobileResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public abstract class MobileCommandToMobileResponseConverter implements Converter<MobileCommand, MobileResponse> {

    public abstract MobileResponse convert(MobileCommand mobileCommand);

}
