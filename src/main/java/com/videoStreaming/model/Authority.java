package com.videoStreaming.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "authority", schema = "public")
public class Authority implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
    @Size(max = 50)
    @Column(length = 50)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Authority [name=" + name + "]";
	}
}
