package com.rdlopes.fsm.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Mower {
    private final int x;

    private final int y;

    private final Orientation orientation;

    private final List<Instruction> instructions;

    public enum Orientation {N, E, W, S}

    public enum Instruction {L, R, F}
}
