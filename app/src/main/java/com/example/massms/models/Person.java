package com.example.massms.models;


import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class Person {
    // Possible: @SerializedName(value="first", alternate={"firstname", "first name"})
    @SerializedName("first")
    private String firstName;

    @SerializedName("last")
    private String lastName;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private Float phone;

    public Person() {
        firstName = null;
        lastName = null;
        name = null;
        phone = null;
    }

    // non-default constructors
    public Person(String newName, Float newPhone) {
        firstName = null;
        lastName = null;
        name = newName;
        phone = newPhone;
    }

    public Person(String newFirst, String newLast, Float newPhone) {
        firstName = newFirst;
        lastName = newLast;
        name = firstName + " " + lastName;
        phone = newPhone;
    }

    // get info functions
    public String getName() {
        if (this.name == null)
            this.name = this.firstName + " " + this.lastName;
        return this.name;
    }

    public Float getPhone() {
        return this.phone;
    }

    // TODO: comparator

    @Override
    public String toString() {
        // We only use one name. So, if it is null we use first and last names.
        // It depends how user defined cotact list
        if (this.name == null)
            this.name = this.firstName + " " + this.lastName;
        String contactString = String.format(Locale.US, "%nName: %s%nPhone: %.0f%n"
                , this.name
                , this.phone);

        return contactString;
    }
}
