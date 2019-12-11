package com.amazon.sbidoo.chance;

import com.amazon.sbidoo.card.Card;
import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.function.BiFunction;

public class MonopolyChanceDao implements ChanceDao {
    private final List<Card> chanceCards;

    @Inject
    public MonopolyChanceDao() {
        this.chanceCards = ChanceCardBuilder.buildChanceCards();
    }


    @Override
    public BiFunction<Player, Banker, String> getActionFromCard() {
        final int randomCardIndex = RandomUtils.nextInt(0, chanceCards.size());
        return this.chanceCards.get(randomCardIndex).getCardApplier();
    }
}
