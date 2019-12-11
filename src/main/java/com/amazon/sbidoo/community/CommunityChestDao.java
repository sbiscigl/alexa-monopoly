package com.amazon.sbidoo.community;

import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;

import java.util.function.BiFunction;

public interface CommunityChestDao {
    BiFunction<Player, Banker, String> getActionFromCard();
}
