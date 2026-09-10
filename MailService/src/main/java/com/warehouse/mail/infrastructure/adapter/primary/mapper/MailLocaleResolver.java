package com.warehouse.mail.infrastructure.adapter.primary.mapper;

import java.util.Locale;
import java.util.Map;

public class MailLocaleResolver {

    private final Map<String, Locale> countryLocales;
    private final Locale defaultLocale;
    private final Locale unmappedCountryLocale;

    public MailLocaleResolver(final Map<String, Locale> countryLocales,
                              final Locale defaultLocale,
                              final Locale unmappedCountryLocale) {
        this.countryLocales = Map.copyOf(countryLocales);
        this.defaultLocale = defaultLocale;
        this.unmappedCountryLocale = unmappedCountryLocale;
    }

    public Locale forCountry(final String countryCode) {
        if (countryCode == null || countryCode.isBlank()) {
            return this.defaultLocale;
        }
        return this.countryLocales.getOrDefault(countryCode.trim().toUpperCase(Locale.ROOT), this.unmappedCountryLocale);
    }

    public Locale defaultLocale() {
        return this.defaultLocale;
    }
}
