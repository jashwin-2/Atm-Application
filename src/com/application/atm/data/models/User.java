package com.application.atm.data.models;


public class User {
	private String name;
	private int age;
	private long mobileNumber;
	private String address;

	public User(String name, int age, String address , long mobileNumber) {
		this.name = name;
		this.age = age;
		this.address = address;
		this.setMobileNumber(mobileNumber);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
