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
                        .playerConsumer((player, banker) -> player.setMoney(player.getMoney() + 150))
                        .build(),
                Card.builder()
                        .message("Go back three spaces")
                        .playerConsumer((player, banker) -> {
                            int spacesBack  = player.getPositionFromStart() - 3;
                            if (spacesBack >= 0) {
                                player.setPositionFromStart(spacesBack);
                            } else {
                                player.setPositionFromStart(39 - Math.abs(spacesBack));
                            }
                        })
                        .build(),
                Card.builder()
                        .message("Speeding fine, pay 15 dollars")
                        .playerConsumer((player, banker) -> player.setMoney(player.getMoney() - 15))
                        .build(),
                Card.builder()
                        .message("Collect 200 dollars and for to start")
                        .playerConsumer((player, banker) -> {
                            player.setPositionFromStart(0);
                            player.setMoney(player.getMoney() + 200);
                        })
                        .build()
        );
    }
}
