package com.rdlopes.fsm;

import com.rdlopes.fsm.parser.ConfigurationFileParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(FileParserProperties.class)
public class StateMachineExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StateMachineExampleApplication.class, args);
    }

    @Bean
    public ConfigurationFileParser parser(FileParserProperties properties) {
        return new ConfigurationFileParser(properties);
    }

    @Bean
    public CommandLineRunner runner(ConfigurationFileParser parser) {
        return args -> parser.parse();
    }

}
