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
                        .message("Bank error in your favor, collect 200 dollars")
                        .cardApplier((player, banker) -> {
                            player.setMoney(player.getMoney() + 200);
                            banker.setBankroll(banker.getBankroll() - 200);
                            return "Bank error in your favor, collect 200 dollars. Current balance is " + player.getMoney() + " dollars";
                        })
                        .build(),
                Card.builder()
                        .message("Hospital Fees, pay 100 dollars")
                        .cardApplier((player, banker) -> {
                            player.setMoney(player.getMoney() - 100);
                            banker.setBankroll(banker.getBankroll() + 100);
                            return "Hospital Fees, pay 100 dollars. Current balance is " + player.getMoney() + " dollars";
                        })
                        .build(),
                Card.builder()
                        .message("Collect 25 dollar consultancy fee")
                        .cardApplier((player, banker) -> {
                            player.setMoney(player.getMoney() - 25);
                            banker.setBankroll(banker.getBankroll() + 25);
                            return "Collect 25 dollar consultancy fee. Current balance is " + player.getMoney() + " dollars";
                        })
                        .build()
        );
    }
}
