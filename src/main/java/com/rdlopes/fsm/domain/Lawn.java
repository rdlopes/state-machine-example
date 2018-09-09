package com.rdlopes.fsm.domain;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class Lawn {
    private final int width;

    private final int height;

    @Singular
    private final Set<Mower> mowers;
}
