package com.warehouse.message.configuration;


import com.warehouse.message.repository.MessageReadRepository;
import com.warehouse.message.repository.MessageRepository;
import com.warehouse.message.repository.MessageRepositoryImpl;
import com.warehouse.message.domain.service.MessageService;
import com.warehouse.message.domain.service.MessageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfiguration {

    @Bean
    public MessageService messageService(final MessageRepository messageRepository) {
        return new MessageServiceImpl(messageRepository);
    }

    @Bean
    public MessageRepository messageRepository(final MessageReadRepository repository) {
        return new MessageRepositoryImpl(repository);
    }
}
