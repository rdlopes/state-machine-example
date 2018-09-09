package com.rdlopes.fsm.parser.fsm;

import com.rdlopes.fsm.domain.Mower;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;
import java.util.List;

public interface Context {

    State getState();

    Resource getInputResource();

    Charset getInputCharset();

    void addContentLine(String line);

    boolean hasMoreLines();

    String getNextLine();

    void setLawnDimension(int width, int height);

    void setMowerCoordinates(int x, int y, Mower.Orientation orientation);

    void setMowerInstructions(List<Mower.Instruction> instructions);
}
