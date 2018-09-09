package com.rdlopes.fsm.parser;

import com.rdlopes.fsm.domain.Lawn;

public interface ConfigurationFileParser {
    void parse() throws ParsingException;

    Lawn getLawn();
}
