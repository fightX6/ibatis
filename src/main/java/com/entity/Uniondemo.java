package com.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the uniondemo database table.
 * 
 */
@Entity
@NamedQuery(name="Uniondemo.findAll", query="SELECT u FROM Uniondemo u")
public class Uniondemo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UniondemoPK id;

	public Uniondemo() {
	}

	public UniondemoPK getId() {
		return this.id;
	}

	public void setId(UniondemoPK id) {
		this.id = id;
	}

}