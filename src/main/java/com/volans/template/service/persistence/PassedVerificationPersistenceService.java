package com.volans.template.service.persistence;

import com.volans.template.model.command.PassedVerificationCommand;
import com.volans.template.model.entity.PassedVerificationEntity;
import com.volans.template.model.enums.PassedVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassedVerificationPersistenceService {

    private final ConversionService conversionService;

    private List<PassedVerificationEntity> entities = new ArrayList<>();

    public Optional<PassedVerificationCommand> findByUserIdAndPassedVerificationType(long userId, PassedVerificationType type) {
        return entities.stream()
                .filter(item -> item.getUserId() == userId && item.getPassedVerificationType().equals(type.getValue()))
                .findFirst()
                .map(item -> conversionService.convert(item, PassedVerificationCommand.class));
    }

    public PassedVerificationCommand save(PassedVerificationCommand source) {
        PassedVerificationEntity entity = conversionService.convert(source, PassedVerificationEntity.class);
        if (entity.getId() == null) {
            long id = entities.isEmpty() ? 1L : entities.stream().mapToLong(PassedVerificationEntity::getId).max().getAsLong() + 1;
            entity.setId(id);
        }
        entities.add(entity);
        source.setId(entity.getId());
        return source;
    }
}
