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

        public void setOwner(Player.PieceType owner){
            this.owner = owner;
        }

        public Player.PieceType getOwner(){
            return this.owner;
        }

        public void setHouses(int houses) {
            this.houses = houses;
        }

        public void setHotels(int hotels) {
            this.hotels = hotels;
        }
    }

    private final Map<SpaceInfo, OwnerInfo> propertyMap;
}
