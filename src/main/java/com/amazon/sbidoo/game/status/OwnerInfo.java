package com.amazon.sbidoo.game.status;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@AllArgsConstructor
public class OwnerInfo {
    private Player.PieceType owner;
    private int houses;
    private int hotels;
}
