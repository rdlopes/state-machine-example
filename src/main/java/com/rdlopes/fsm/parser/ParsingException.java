package com.rdlopes.fsm.parser;

import lombok.Getter;

public class ParsingException extends Exception {

    @Getter
    private final transient ConfigurationFileParser configurationFileParser;

    public ParsingException(ConfigurationFileParser configurationFileParser, Throwable cause) {
        super(cause);
        this.configurationFileParser = configurationFileParser;
    }

    public ParsingException(ConfigurationFileParser configurationFileParser, String message) {
        super(message);
        this.configurationFileParser = configurationFileParser;
    }
}
