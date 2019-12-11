package com.amazon.sbidoo.chance;

import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;

import java.util.function.BiFunction;

public interface ChanceDao {
    BiFunction<Player, Banker, String> getActionFromCard();
}
