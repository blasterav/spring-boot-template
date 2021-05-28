package com.volans.template.converter;

import com.volans.template.model.command.MobileCommand;
import com.volans.template.model.entity.MobileEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public abstract class MobileEntityToMobileCommandConverter implements Converter<MobileEntity, MobileCommand> {

    public abstract MobileCommand convert(MobileEntity mobileCommand);

}
