package com.amazon.sbidoo.game.status;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class GameStatus {
    private int version;
    private Set<Player> player;
    private Banker banker;
    private Property property;
    private Board board;
}
