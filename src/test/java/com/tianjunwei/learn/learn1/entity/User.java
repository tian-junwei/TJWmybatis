package com.tianjunwei.learn.learn1.entity;

import java.io.Serializable;

public class User implements Serializable {
	
	
	private static final long serialVersionUID = -1611006178368335684L;
	private int id;
	private String names;
	private int age;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String name) {
		this.names = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

}
