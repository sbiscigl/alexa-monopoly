package com.amazon.sbidoo.game.status;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder
@Value
public class Property {
    public class SpaceInfo {
        private Space.SpaceType spaceType;
        private Space.SpaceCategory spaceCategory;
        private String spaceName;

        public SpaceInfo(Space.SpaceType spaceType, Space.SpaceCategory spaceCategory, String spaceName) {
            this.spaceType = spaceType;
            this.spaceCategory = spaceCategory;
            this.spaceName = spaceName;
        }
    }

    public class OwnerInfo {
        private Player.PieceType owner;
        private int houses;
        private int hotels;
    }

    private final Map<SpaceInfo, OwnerInfo> propertyMap;
}
