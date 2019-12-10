package com.amazon.sbidoo.game.status;

import java.util.Map;

public class Property {
    public class SpaceInfo {
        private Board.SpaceType spaceType;
        private Board.SpaceCategory spaceCategory;
    }

    public class OwnerInfo {
        private Player.PieceType owner;
        private int houses;
        private int hotels;
    }

    private final Map<SpaceInfo, OwnerInfo> propertyMap;

    public Property(final Map<SpaceInfo, OwnerInfo> propertyMap) {
        this.propertyMap = propertyMap;
    }
}
