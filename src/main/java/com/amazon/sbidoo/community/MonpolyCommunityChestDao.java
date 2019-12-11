package com.amazon.sbidoo.community;

import com.amazon.sbidoo.card.Card;
import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.function.BiConsumer;

public class MonpolyCommunityChestDao implements CommunityChestDao {
    private final List<Card> communityChestCards;

    @Inject
    public MonpolyCommunityChestDao(@Named("ChanceCards") List<Card> communityChestCards) {
        this.communityChestCards = communityChestCards;
    }


    @Override
    public BiConsumer<Player, Banker> getActionFromCard(Card card) {
        final int randomCardIndex = RandomUtils.nextInt(0, communityChestCards.size());
        return this.communityChestCards.get(randomCardIndex).getPlayerConsumer();
    }
}
