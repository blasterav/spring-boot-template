package com.volans.template.service.persistence;

import com.volans.template.model.command.UserCommand;
import com.volans.template.model.entity.UserEntity;
import com.volans.template.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPersistenceService {

    private final ConversionService conversionService;
    private final UserRepository userRepository;

    public Optional<UserCommand> findById(long id) {
        return userRepository.findById(id)
                .map(item -> conversionService.convert(item, UserCommand.class));
    }

    public List<UserCommand> findAll() {
        return userRepository.findAll().stream()
                .map(item -> conversionService.convert(item, UserCommand.class))
                .collect(Collectors.toList());
    }

    public UserCommand save(UserCommand userCommand) {
        UserEntity userEntity = conversionService.convert(userCommand, UserEntity.class);
        userRepository.save(userEntity);
        userCommand.setId(userEntity.getId());
        return userCommand;
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
