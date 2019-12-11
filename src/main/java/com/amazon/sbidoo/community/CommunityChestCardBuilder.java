package com.amazon.sbidoo.community;

import com.amazon.sbidoo.card.Card;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class CommunityChestCardBuilder {
    private CommunityChestCardBuilder() {
    }

    public static List<Card> buildCommunityChanceCards() {
        return ImmutableList.of(
                Card.builder()
                        .message("Advance to go collect 200 dollars")
                        .playerConsumer((player, banker) -> {
                            player.setPositionFromStart(0);
                            player.setMoney(player.getMoney() + 200);
                            banker.setBankroll(banker.getBankroll() - 200);
                        })
                        .build(),
                Card.builder()
                        .message("Bank error in your favor, collect 200 dollars")
                        .playerConsumer((player, banker) -> {
                            player.setMoney(player.getMoney() + 200);
                            banker.setBankroll(banker.getBankroll() - 200);
                        })
                        .build(),
                Card.builder()
                        .message("Hospital Fees, pay 100 dollars")
                        .playerConsumer((player, banker) -> {
                            player.setMoney(player.getMoney() - 100);
                            banker.setBankroll(banker.getBankroll() + 100);
                        })
                        .build(),
                Card.builder()
                        .message("Collect 25 dollar consultancy fee")
                        .playerConsumer((player, banker) -> {
                            player.setMoney(player.getMoney() - 25);
                            banker.setBankroll(banker.getBankroll() + 25);
                        })
                        .build()
        );
    }
}
