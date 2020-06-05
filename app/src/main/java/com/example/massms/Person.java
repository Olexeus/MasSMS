package com.example.massms;


import com.google.gson.annotations.SerializedName;

public class Person {
    // TODO: put an array of possible values
    @SerializedName(value="First Name", alternate={"first", "First"})
    private String firstName;

    @SerializedName(value="Last Name", alternate={"last", "Last"})
    private String lastName;

    @SerializedName(value="name", alternate={"Name", "Person"})
    private String name;

    @SerializedName(value="phone")
    private Float phone;

    public Person() {
        firstName = null;
        lastName = null;
        name = null;
        phone = null;
    }

    // TODO: non-default constructor
    // TODO: get info functions

    @Override
    public String toString() {
        // We only use one name. So, if it is null we use first and last names.
        // It depends how user defined cotact list
        if (this.name == null)
            this.name = this.firstName + " " + this.lastName;
        String contactString = String.format("%nName: %s%nPhone: %.0f%n"
                , this.name
                , this.phone);

        return contactString;
    }
}
