package com.warehouse.redirect.domain.model;


import lombok.Data;

@Data
public class ConstantBodyMailMessage {

    private String body;

    private String token;

    private Long parcelId;

    public ConstantBodyMailMessage(String token) {
        this.body = "Dla Państwa paczki została wysłana prośba o przekierowanie" +
                        "\nAby to zrobić prosimy użyć kodu: " + token;
    }
}
