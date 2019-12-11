package com.amazon.sbidoo.community;

import com.amazon.sbidoo.card.Card;
import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;

import java.util.function.BiConsumer;

public interface CommunityChestDao {
    BiConsumer<Player, Banker> getActionFromCard(Card card);
}
