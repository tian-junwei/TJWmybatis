package com.tianjunwei.lazy.entity;

import java.io.Serializable;

public class User implements Serializable {
	
	
	private static final long serialVersionUID = -1611006178368335684L;
	private int id;
	private String names;
	private int age;
	private Teacher teacher;
	
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
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
