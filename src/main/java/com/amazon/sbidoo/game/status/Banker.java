package com.amazon.sbidoo.game.status;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder
@Data
public class Banker {
    private int bankroll;
    private int freeParkingMoney;
}
