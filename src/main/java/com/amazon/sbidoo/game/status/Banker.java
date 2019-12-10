package com.amazon.sbidoo.game.status;

public class Banker {
    private final int bankroll;
    private final int freeParkingMoney;

    public Banker(final int bankroll,
                  final int freeParkingMoney) {
        this.bankroll = bankroll;
        this.freeParkingMoney = freeParkingMoney;
    }
}
