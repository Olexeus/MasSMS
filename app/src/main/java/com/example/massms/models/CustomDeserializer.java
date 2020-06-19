package com.example.massms.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomDeserializer implements JsonDeserializer<Group> {

    @Override
    public Group deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        // https://stackoverflow.com/questions/23472175/gson-deserializing-json-with-random-class-names
        final JsonObject convertedGson = json.getAsJsonObject();
        final Group newGroup = new Group();

        // Check for group name and delete it
        if (convertedGson.has("groupName")) {
            final String groupName = convertedGson.get("groupName").getAsString();
            convertedGson.remove("groupName");
            newGroup.addName(groupName);
        }
        if (convertedGson.has("fileName")) {
            // final String fileName = convertedGson.get("fileName").getAsString();
            convertedGson.remove("fileName");
        }

        // get the key with contacts
        Set<Map.Entry<String, JsonElement>> groupObjects =  convertedGson.entrySet();
        // get Persons as JsonArray
        JsonArray jsonContacts = convertedGson.get(groupObjects.iterator().next().getKey()).getAsJsonArray();

        // Convert jsonContacts into Person objects
        Gson gson = new GsonBuilder().setFieldNamingStrategy(new CustomContactNaming()).create();
        List<Person> objectContacts = new ArrayList<>();
        for (JsonElement entry : jsonContacts) {
            JsonElement jsonElement  = entry;
            Person contact = gson.fromJson(jsonElement, Person.class);
            objectContacts.add(contact);
        }

        // add converted data
        newGroup.addContacts(objectContacts);

        return newGroup;
    }
}