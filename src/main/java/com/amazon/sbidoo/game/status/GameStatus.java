package com.amazon.sbidoo.game.status;

public class GameStatus {
    private int version;
    private String mesaage;

    public GameStatus(final int version,
                      final String mesaage) {
        this.version = version;
        this.mesaage = mesaage;
    }

    public int getVersion() {
        return version;
    }

    public String getMesaage() {
        return mesaage;
    }
}
