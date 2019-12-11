package com.amazon.sbidoo.chance;

import com.amazon.sbidoo.card.Card;
import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;

import java.util.function.BiConsumer;

public interface ChanceDao {
    BiConsumer<Player, Banker> getActionFromCard(Card card);
}
