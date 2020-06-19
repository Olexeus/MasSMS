package com.example.massms.models;

import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;
import java.util.Arrays;

class CustomContactNaming implements FieldNamingStrategy {
    @Override
    public String translateName(Field field) {
        // https://stackoverflow.com/questions/38731050/gson-deserializejson-field-name-different-from-java-object-field-name
        // TODO: create a Strategy Pattern?
        // TODO: add all possible variations
        String[] possibleFirst = new String[]{"first", "first name", "firstname"};
        String[] possibleLast = new String[]{"last", "surname", "last name", "lastname"};
        String[] possibleName = new String[]{"name", "username", "person"};
        String[] possiblePhone = new String[]{"phone", "number"};
        // TODO: maybe use Hash_Set.contains(Object element)
        boolean containsFirst = Arrays.asList(possibleFirst).contains(field.getName().toLowerCase());
        boolean containsLast = Arrays.asList(possibleLast).contains(field.getName().toLowerCase());
        boolean containsName = Arrays.asList(possibleName).contains(field.getName().toLowerCase());
        boolean containsPhone = Arrays.asList(possiblePhone).contains(field.getName().toLowerCase());

        if (containsFirst) {
            return "first";
        }
        else if (containsLast) {
            return "last";
        }
        else if (containsName) {
            return "name";
        }
        else if (containsPhone) {
            return "phone";
        }

        return field.getName();
    }
}