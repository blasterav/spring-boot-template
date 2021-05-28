package com.volans.template.service.persistence;

import com.volans.template.model.command.UserCommand;
import com.volans.template.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPersistenceService {

    private final ConversionService conversionService;

    private List<UserEntity> users = new ArrayList<>();

    public Optional<UserCommand> findById(long id) {
        return users.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .map(item -> conversionService.convert(item, UserCommand.class));
    }

    public List<UserCommand> findAll() {
        return users.stream()
                .map(item -> conversionService.convert(item, UserCommand.class))
                .collect(Collectors.toList());
    }

    public UserCommand save(UserCommand userCommand) {
        UserEntity userEntity = conversionService.convert(userCommand, UserEntity.class);
        if (userEntity.getId() == null) {
            long id = users.isEmpty() ? 1L : users.stream().mapToLong(UserEntity::getId).max().getAsLong() + 1;
            userEntity.setId(id);
            users.add(userEntity);
            userCommand.setId(userEntity.getId());
        } else {
            users.stream()
                    .filter(item -> item.getId().equals(userEntity.getId()))
                    .findFirst()
                    .ifPresent(item -> {
                        users.remove(item);
                        users.add(userEntity);
                    });
        }

        return userCommand;
    }

    public void delete(long id) {
        users.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .ifPresent(item -> {
                    users.remove(item);
                });
    }
}
