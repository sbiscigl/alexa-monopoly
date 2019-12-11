package com.amazon.sbidoo.game.status.serializer;

import com.amazon.sbidoo.game.status.SpaceInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SpaceInfoSerializer implements JsonSerializer<SpaceInfo> {

    @Override
    public JsonElement serialize(SpaceInfo spaceInfo, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("spaceCategory", spaceInfo.getSpaceCategory().name());
        object.addProperty("spaceName", spaceInfo.getSpaceName());
        object.addProperty("spaceType", spaceInfo.getSpaceType().name());

        return object;

    }
}
