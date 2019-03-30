package com.pier.bean;

import java.io.Serializable;

public class User implements Serializable {

	private int id;
	private String username;
	private String password;
	private double account;

	public User() {
		super();
	}

	public User(Integer id, String username, String password, double account) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.account = account;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "User [account=" + account + ", id=" + id + ", password="
				+ password + ", username=" + username + "]";
	}
}