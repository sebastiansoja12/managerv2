package com.warehouse.palletmanagement.domain.vo;

public record PalletDocumentFile(String fileName, byte[] content) {

    public PalletDocumentFile {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name is required");
        }
        content = content == null ? new byte[0] : content;
    }
}
