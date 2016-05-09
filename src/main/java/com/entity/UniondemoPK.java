package com.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the uniondemo database table.
 * 
 */
@Embeddable
public class UniondemoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String name;

	private String value;

	private String type;

	public UniondemoPK() {
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UniondemoPK)) {
			return false;
		}
		UniondemoPK castOther = (UniondemoPK)other;
		return 
			this.name.equals(castOther.name)
			&& this.value.equals(castOther.value)
			&& this.type.equals(castOther.type);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.name.hashCode();
		hash = hash * prime + this.value.hashCode();
		hash = hash * prime + this.type.hashCode();
		
		return hash;
	}
}