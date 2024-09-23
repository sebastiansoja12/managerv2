package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class DepartmentCode {

	private final String departmentCode;

	public DepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final DepartmentCode that = (DepartmentCode) o;
		return Objects.equals(departmentCode, that.departmentCode);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(departmentCode);
	}
}
