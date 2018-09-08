package com.rdlopes.fsm.parser;

import com.rdlopes.fsm.FileParserProperties;
import com.rdlopes.fsm.domain.Lawn;
import com.rdlopes.fsm.domain.Mower;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.readAllLines;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Data
public class ConfigurationFileParser implements Context {

    private final List<String> lines = new ArrayList<>();

    private Lawn lawn = null;

    private Mower.MowerBuilder mowerBuilder = null;

    private List<Mower> mowers = new ArrayList<>();

    private FileParserProperties properties;

    public ConfigurationFileParser(FileParserProperties properties) {
        this.properties = properties;
        this.state = State.States.READING_LAWN_DIMENSIONS;
    }

    @Getter
    @Setter
    @NonNull
    private State state;

    @Override
    public boolean hasContent() {
        return !lines.isEmpty() && hasText(lines.get(0));
    }

    @Override
    public String nextContent() {
        return lines.remove(0);
    }

    @Override
    public boolean setLawn(int width, int height) {
        if (this.lawn != null) {
            return false;
        }

        this.lawn = Lawn.builder()
                        .width(width)
                        .height(height)
                        .build();

        return true;
    }

    @Override
    public boolean setMowerCoordinates(int x, int y, String orientation) {
        if (mowerBuilder != null) {
            return false;
        }

        mowerBuilder = Mower.builder()
                            .x(x)
                            .y(y)
                            .orientation(Mower.Orientation.valueOf(orientation));
        return true;
    }

    @Override
    public boolean setMowerInstructions(List<String> instructions) {
        if (mowerBuilder == null) {
            return false;
        }

        mowerBuilder.instructions(instructions.stream()
                                              .map(Mower.Instruction::valueOf)
                                              .collect(toList()));
        mowers.add(mowerBuilder.build());
        mowerBuilder = null;
        return true;
    }

    public void parse() throws IOException {
        Path inputPath = properties.getInput().getFile().toPath();
        this.lines.addAll(readAllLines(inputPath));

        while (getState().process(this)) {
            log.debug("continuing parsing for context:{}", this);
        }

        if (getState() == State.States.END) {
            log.info("SUCCESS - Parsed lawn:{}", lawn);
            mowers.forEach(mower -> log.info("SUCCESS - Parsed mower:{}", mower));

        } else {
            log.error("FAILURE - Stopped at state:{}", getState());
        }
    }

}
