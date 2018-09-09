package com.rdlopes.fsm.parser.impl;

import com.rdlopes.fsm.FileParserProperties;
import com.rdlopes.fsm.domain.Lawn;
import com.rdlopes.fsm.domain.Mower;
import com.rdlopes.fsm.parser.ConfigurationFileParser;
import com.rdlopes.fsm.parser.ParsingException;
import com.rdlopes.fsm.parser.fsm.Context;
import com.rdlopes.fsm.parser.fsm.State;
import com.rdlopes.fsm.parser.fsm.StateProcessingException;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Data
public class ConfigurationFileParserImpl implements ConfigurationFileParser, Context {

    @Getter
    private final Resource inputResource;

    @Getter
    private final Charset inputCharset;

    @Getter
    @NonNull
    private State state;

    @NonNull
    private Queue<String> content = new LinkedList<>();

    private Lawn.LawnBuilder lawnBuilder = null;

    private Mower.MowerBuilder mowerBuilder = null;

    public ConfigurationFileParserImpl(FileParserProperties properties) {
        this.state = States.READING_FILE;
        this.inputResource = properties.getInputResource();
        this.inputCharset = properties.getInputCharset();
    }

    @Override
    public void parse() throws ParsingException {
        while (state.canProcess(this)) {
            try {
                this.state = state.process(this);

            } catch (StateProcessingException e) {
                throw new ParsingException(this, e);
            }
        }

        if (!isInTerminalState()) {
            throw new ParsingException(this, MessageFormat.format("Parser is not in a terminal state, state:{0}", getState()));
        }
    }

    @Override
    public Lawn getLawn() {
        return Optional.ofNullable(lawnBuilder)
                       .map(Lawn.LawnBuilder::build)
                       .orElse(null);
    }

    private boolean isInTerminalState() {
        return getState().equals(States.END);
    }

    @Override
    public void addContentLine(String line) {
        content.offer(line);
    }

    @Override
    public boolean hasMoreLines() {
        return !content.isEmpty() && hasText(content.peek());
    }

    @Override
    public String getNextLine() {
        return content.poll();
    }

    @Override
    public void setLawnDimension(int width, int height) {
        lawnBuilder = Lawn.builder()
                          .width(width)
                          .height(height);
    }

    @Override
    public void setMowerCoordinates(int x, int y, Mower.Orientation orientation) {
        mowerBuilder = Mower.builder()
                            .x(x)
                            .y(y)
                            .orientation(orientation);
    }

    @Override
    public void setMowerInstructions(List<Mower.Instruction> instructions) {
        mowerBuilder.instructions(instructions);
        lawnBuilder.mower(mowerBuilder.build());
    }
}
