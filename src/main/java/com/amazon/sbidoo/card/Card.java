package com.amazon.sbidoo.card;

import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;
import lombok.Builder;
import lombok.Value;

import java.util.function.BiFunction;

@Builder
@Value
public class Card {
    String message;
    BiFunction<Player, Banker, String> cardApplier;
}
