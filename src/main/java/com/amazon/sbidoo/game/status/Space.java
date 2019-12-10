package com.amazon.sbidoo.game.status;

import lombok.Builder;
import lombok.Value;

import java.util.function.Consumer;

@Builder
@Value
public class Space {
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

    private SpaceType spaceType;
    private SpaceCategory spaceCategory;
    private int positionFromStart;
    private int price;
    private String propertyName;
    private Consumer<Player> spaceAction;
}
