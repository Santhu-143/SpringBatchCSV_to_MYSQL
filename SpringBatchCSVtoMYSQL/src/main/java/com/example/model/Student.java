package com.example.model;

import lombok.Data;

@Data
public class Student {

	private Integer id;
	private String name;
	private Integer mat;
	private Integer phy;
	private Integer che;
	private double per;
	private String status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMat() {
		return mat;
	}
	public void setMat(Integer mat) {
		this.mat = mat;
	}
	public Integer getPhy() {
		return phy;
	}
	public void setPhy(Integer phy) {
		this.phy = phy;
	}
	public Integer getChe() {
		return che;
	}
	public void setChe(Integer che) {
		this.che = che;
	}
	public double getPer() {
		return per;
	}
	public void setPer(double per) {
		this.per = per;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
