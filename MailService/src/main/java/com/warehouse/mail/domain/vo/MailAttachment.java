package com.warehouse.mail.domain.vo;

public record MailAttachment(String fileName, String contentType, byte[] content) {
}
