package com.volans.template.service.persistence;

import com.volans.template.model.command.MobileCommand;
import com.volans.template.model.entity.MobileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MobilePersistenceService {

    private final ConversionService conversionService;

    private List<MobileEntity> mobiles = new ArrayList<>();

    public Optional<MobileCommand> findById(long id) {
        return mobiles.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .map(item -> conversionService.convert(item, MobileCommand.class));
    }

    public MobileCommand save(MobileCommand mobileCommand) {
        MobileEntity mobileEntity = conversionService.convert(mobileCommand, MobileEntity.class);
        if (mobileEntity.getId() == null) {
            long id = mobiles.isEmpty() ? 1L : mobiles.stream().mapToLong(MobileEntity::getId).max().getAsLong() + 1;
            mobileEntity.setId(id);
        }
        mobiles.add(mobileEntity);
        mobileCommand.setId(mobileEntity.getId());
        return mobileCommand;
    }
}
