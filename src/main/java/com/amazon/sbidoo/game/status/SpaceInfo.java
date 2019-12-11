package com.amazon.sbidoo.game.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@Value
@AllArgsConstructor
@EqualsAndHashCode
public class SpaceInfo {
    private Space.SpaceType spaceType;
    private Space.SpaceCategory spaceCategory;
    private String spaceName;
}
