package com.warehouse.mail.domain.service;

import com.warehouse.mail.domain.exception.MailException;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.model.MailTemplate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailTemplateRenderer {

    private static final Pattern SECTION = Pattern.compile("\\{\\{#([\\w.-]+)}}(.*?)\\{\\{/\\1}}", Pattern.DOTALL);
    private static final Pattern VARIABLE = Pattern.compile("\\{\\{([\\w.-]+)}}");

    public MailMessage render(final MailTemplate template, final MailRequest request) {
        return new MailMessage(request.recipient(),
                render(template, template.subject(), request.variables()).replaceAll("\\s+", " ").trim(),
                render(template, template.body(), request.variables()).trim());
    }

    private static String render(final MailTemplate template, final String text, final Map<String, String> variables) {
        final Matcher sections = SECTION.matcher(text);
        final StringBuilder withSections = new StringBuilder();
        while (sections.find()) {
            final String value = variables.get(sections.group(1));
            sections.appendReplacement(withSections, Matcher.quoteReplacement(
                    value == null || value.isBlank() ? "" : sections.group(2)));
        }
        sections.appendTail(withSections);
        final Matcher placeholders = VARIABLE.matcher(withSections);
        final StringBuilder rendered = new StringBuilder();
        while (placeholders.find()) {
            final String name = placeholders.group(1);
            if (!variables.containsKey(name)) {
                throw new MailException("Mail template " + template.key() + " requires variable " + name);
            }
            final String value = variables.get(name);
            placeholders.appendReplacement(rendered, Matcher.quoteReplacement(value == null ? "" : value));
        }
        placeholders.appendTail(rendered);
        return rendered.toString().replaceAll("\\n{3,}", "\n\n");
    }
}
