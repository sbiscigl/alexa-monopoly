package com.amazon.sbidoo.community;

import com.amazon.sbidoo.card.Card;
import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.function.BiFunction;

public class MonopolyCommunityChestDao implements CommunityChestDao {
    private final List<Card> communityChestCards;

    @Inject
    public MonopolyCommunityChestDao() {
        this.communityChestCards = CommunityChestCardBuilder.buildCommunityChanceCards();
    }


    @Override
    public BiFunction<Player, Banker, String> getActionFromCard() {
        final int randomCardIndex = RandomUtils.nextInt(0, communityChestCards.size());
        return this.communityChestCards.get(randomCardIndex).getCardApplier();
    }
}
