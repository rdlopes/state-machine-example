package com.rdlopes.fsm.parser;

import java.util.List;

public interface Context {
    String nextContent();

    boolean hasContent();

    State getState();

    void setState(State state);

    boolean setLawn(int width, int height);

    boolean setMowerCoordinates(int x, int y, String orientationCode);

    boolean setMowerInstructions(List<String> instructions);
}
