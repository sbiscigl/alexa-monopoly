package com.amazon.sbidoo.game.status;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Consumer;

public class Board {
    private final Map<Integer, Space> spaceMap;

    public enum SpaceType {
        Property,
        Chance,
        CommunityChest,
        Jail,
        Start,
        GoToJail,
        FreeParking,
        RailRoad,
        Utilities
    }

    public enum SpaceCategory {
        Brown,
        LightBlue,
        Pink,
        Orange,
        Red,
        Yellow,
        Green,
        Blue,
        Utilities,
        RailRoad
    }

    public class Space {
        private SpaceType spaceType;
        private SpaceCategory spaceCategory;
        private int positionFromStart;
        private int price;
        private String propertyName;
        private Consumer<Player> spaceAction;
    }

    public Board() {
        this.spaceMap = ImmutableMap.of();
    }
}
