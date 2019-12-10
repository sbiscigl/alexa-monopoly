package com.amazon.sbidoo.game.status;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Banker {
    private int bankroll;
    private int freeParkingMoney;
}
