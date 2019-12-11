package com.amazon.sbidoo.chance;

import com.amazon.sbidoo.card.Card;
import com.amazon.sbidoo.game.status.Banker;
import com.amazon.sbidoo.game.status.Player;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.function.BiConsumer;

public class MonopolyChanceDao implements ChanceDao {
    private final List<Card> chanceCards;

    @Inject
    public MonopolyChanceDao(@Named("ChanceCards") List<Card> chanceCards) {
        this.chanceCards = chanceCards;
    }


    @Override
    public BiConsumer<Player, Banker> getActionFromCard(Card card) {
        final int randomCardIndex = RandomUtils.nextInt(0, chanceCards.size());
        return this.chanceCards.get(randomCardIndex).getPlayerConsumer();
    }
}
