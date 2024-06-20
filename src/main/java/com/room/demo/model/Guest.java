package com.room.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Guest {

    private double willingnessToPay;

    @JsonCreator
    public Guest (double willingnessToPay) {
        this.willingnessToPay = willingnessToPay;
    }

    @JsonValue
    // This annotation tells Jackson to consider the return value of this method as the value during serialization
    public double getWillingnessToPay () {
        return willingnessToPay;
    }

}