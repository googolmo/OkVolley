package im.amomo.volley.sample.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-09
 * Time: 14:14
 */
public class PersonalDesDeserializer implements JsonDeserializer<PersonalDes> {
    @Override
    public PersonalDes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final PersonalDes personalDes = new PersonalDes();
        final JsonObject jsonObject = json.getAsJsonObject();
        personalDes.setLoc_id(jsonObject.get("loc_id").getAsString());
        personalDes.setName(jsonObject.get("name").getAsString());
        personalDes.setCreated(jsonObject.get("created").getAsString());
        personalDes.setIs_banned(jsonObject.get("is_banned").getAsBoolean());
        personalDes.setIs_suicide(jsonObject.get("is_suicide").getAsBoolean());
        personalDes.setLoc_name(jsonObject.get("loc_name").getAsString());
        personalDes.setAvatar(jsonObject.get("avatar").getAsString());
        personalDes.setSignature(jsonObject.get("signature").getAsString());
        personalDes.setUid(jsonObject.get("uid").getAsString());
        personalDes.setAlt(jsonObject.get("alt").getAsString());
        personalDes.setDesc(jsonObject.get("desc").getAsString());
        personalDes.setType(jsonObject.get("type").getAsString());
        personalDes.setId(jsonObject.get("id").getAsString());
        personalDes.setLarge_avatar(jsonObject.get("large_avatar").getAsString());
        return personalDes;
    }
}
