package com.amazon.sbidoo.game.status;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder
@Value
public class Board {
    private final Map<Integer, Space> spaceMap;
}
