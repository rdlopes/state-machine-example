package com.rdlopes.fsm.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Lawn {
    private final int width;

    private final int height;
}
