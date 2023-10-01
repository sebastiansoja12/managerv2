package com.warehouse.redirect.domain.model;


import lombok.Data;

@Data
public class ConstantBodyMailMessage {

    String body;

    String token;

    Long parcelId;

    public ConstantBodyMailMessage(String token) {
        this.body = "Dla Państwa paczki została wysłana prośba o przekierowanie" +
                        "\nAby to zrobić prosimy użyć kodu: " + token;
    }
}
