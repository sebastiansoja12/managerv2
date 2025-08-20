package com.warehouse.commonassets.enumeration;

import jakarta.persistence.Embeddable;

@Embeddable
public enum Country {
    ALBANIA,
    ANDORRA,
    ARMENIA,
    AUSTRIA,
    AZERBAIJAN,
    BELARUS,
    BELGIUM,
    BOSNIA_AND_HERZEGOVINA,
    BULGARIA,
    CROATIA,
    CYPRUS,
    CZECH_REPUBLIC,
    DENMARK,
    ESTONIA,
    FINLAND,
    FRANCE,
    GEORGIA,
    GERMANY,
    GREECE,
    HUNGARY,
    ICELAND,
    IRELAND,
    ITALY,
    KAZAKHSTAN,
    KOSOVO,
    LATVIA,
    LIECHTENSTEIN,
    LITHUANIA,
    LUXEMBOURG,
    MALTA,
    MOLDOVA,
    MONACO,
    MONTENEGRO,
    NETHERLANDS,
    NORTH_MACEDONIA,
    NORWAY,
    POLAND,
    PORTUGAL,
    ROMANIA,
    RUSSIA,
    SAN_MARINO,
    SERBIA,
    SLOVAKIA,
    SLOVENIA,
    SPAIN,
    SWEDEN,
    SWITZERLAND,
    TURKEY,
    UKRAINE,
    UNITED_KINGDOM,
    VATICAN_CITY,
    UNDEFINED;

    public String getCountryName() {
        return this.name().replace("_", " ");
    }
}

