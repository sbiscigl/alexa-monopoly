package com.amazon.sbidoo.game.status;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Banker {
    private int bankroll;
    private int freeParkingMoney;
}
