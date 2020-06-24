package com.example.massms.models;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Person implements JsonDeserializer<Person> {
    // Possible: @SerializedName(value="first", alternate={"firstname", "first name"})
    @SerializedName("first")
    private String firstName;

    @SerializedName("last")
    private String lastName;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private Double phone;

    public Person() {
        firstName = null;
        lastName = null;
        name = null;
        phone = null;
    }

    // non-default constructors
    public Person(String newName, Double newPhone) {
        firstName = null;
        lastName = null;
        name = newName;
        phone = newPhone;
    }

    public Person(String newFirst, String newLast, Double newPhone) {
        firstName = newFirst;
        lastName = newLast;
        name = firstName + " " + lastName;
        phone = newPhone;
    }

    // get info functions
    public String getName() {
        // We only use one name. So, if it is null we use first and last names.
        // It depends how user defined cotact list
        if (this.name == null)
            this.name = this.firstName + " " + this.lastName;
        return this.name;
    }

    public Double getPhone() {
        return this.phone;
    }

    // TODO: comparator

    @Override
    public String toString() {
        String contactString = String.format(Locale.US, "%nName: %s%nPhone: %.0f%n"
                , this.getName()
                , this.getPhone());

        return contactString;
    }

    @Override
    public Person deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // TODO: create a Strategy Pattern?
        // TODO: add all possible variations
        // possible values for field names
        String[] possibleFirst = new String[]{"first", "first name", "firstname", "First Name"};
        String[] possibleLast = new String[]{"last", "surname", "last name", "lastname", "Last Name"};
        String[] possibleName = new String[]{"name", "username", "person"};
        String[] possiblePhone = new String[]{"phone", "number", "phone number"};
        // TODO: maybe use Hash_Set.contains(Object element)


        final JsonObject convertedGson = json.getAsJsonObject();
        final Person newPerson = new Person();

        // get the key values
        Set<Map.Entry<String, JsonElement>> personObjects =  convertedGson.entrySet();

        for (Map.Entry<String, JsonElement> entry : personObjects) {
            String key = entry.getKey();
            JsonElement personInfo  = entry.getValue();

            // check if key values coinside with any possible values
            boolean containsFirst = Arrays.asList(possibleFirst).contains(key.toLowerCase());
            boolean containsLast = Arrays.asList(possibleLast).contains(key.toLowerCase());
            boolean containsName = Arrays.asList(possibleName).contains(key.toLowerCase());
            boolean containsPhone = Arrays.asList(possiblePhone).contains(key.toLowerCase());

            // assign to corresponding variable
            if (containsFirst) {
                newPerson.firstName = personInfo.getAsString();
            }
            else if (containsLast) {
                newPerson.lastName = personInfo.getAsString();
            }
            else if (containsName) {
                newPerson.name = personInfo.getAsString();
            }
            else if (containsPhone) {
                // TODO: maybe there is a better condition
                if (personInfo.getAsString().isEmpty()) {
                    return null;
                }
                newPerson.phone = personInfo.getAsDouble();
            }
        }

        return newPerson;
    }
}
