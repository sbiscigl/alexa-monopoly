package com.amazon.sbidoo.chance;

import com.amazon.sbidoo.card.Card;
import com.google.common.collect.ImmutableList;

import java.util.List;

final public class ChanceCardBuilder {
    private ChanceCardBuilder() {
    }

    public static List<Card> buildChanceCards() {
        return ImmutableList.of(
                Card.builder()
                        .message("Your Building Loan matures collect 150 dollars")
                        .cardApplier((player, banker) -> {
                            player.setMoney(player.getMoney() + 150);
                            return "Your Building Loan matures collect 150 dollars. Current balance is " + player.getMoney() + " dollars";
                        })
                        .build(),
                Card.builder()
                        .message("Speeding fine, pay 15 dollars")
                        .cardApplier((player, banker) -> {
                            player.setMoney(player.getMoney() - 15);
                            return "Speeding fine, pay 15 dollars. Current balance is " + player.getMoney() + " dollars";
                        })
                        .build()
        );
    }
}
