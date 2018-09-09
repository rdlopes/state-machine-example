package com.rdlopes.fsm;

import com.rdlopes.fsm.parser.ConfigurationFileParser;
import com.rdlopes.fsm.parser.impl.ConfigurationFileParserImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(FileParserProperties.class)
public class StateMachineExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StateMachineExampleApplication.class, args);
    }

    @Bean
    public ConfigurationFileParser parser(FileParserProperties properties) {
        return new ConfigurationFileParserImpl(properties);
    }

    @Bean
    public CommandLineRunner runner(ConfigurationFileParser parser, FileParserProperties properties) {
        return args -> {
            parser.parse();
            Optional.ofNullable(parser.getLawn())
                    .ifPresent(lawn -> log.info("Successfully parsed lawn from file {} - lawn:{}", properties.getInputResource(), lawn));
        };
    }

}
