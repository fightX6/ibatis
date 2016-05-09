package com.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the demo database table.
 * 
 */
@Entity
@NamedQuery(name="Demo.findAll", query="SELECT d FROM Demo d")
public class Demo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String name;

	private String value;

	public Demo() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

}