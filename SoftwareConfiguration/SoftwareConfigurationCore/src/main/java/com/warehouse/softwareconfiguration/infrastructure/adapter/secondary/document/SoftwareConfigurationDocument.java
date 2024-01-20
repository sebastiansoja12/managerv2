package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document;

import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "softwareConfigurationDocument")
public class SoftwareConfigurationDocument {

	@Id
	private String id;

	private String category;

	private String name;

	private String value;

	public void updateDocument(SoftwareProperty softwareProperty) {
		this.category = softwareProperty.getCategory();
		this.name = softwareProperty.getName();
		this.value = softwareProperty.getValue();
	}
}
