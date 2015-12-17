package com.example.li.eve_united.bean;

public class UserBean {
	private int id;
	private String user;
	private String nickname;
	private String pwd;
	private String number;
	private String sex;
	private String address;
	private String portrait;
	private String email;
	private String introduce;
	private String time;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", user=" + user + ", nickname=" + nickname
				+ ", pwd=" + pwd + ", number=" + number + ", sex=" + sex
				+ ", address=" + address + ", portrait=" + portrait
				+ ", email=" + email + ", introduce=" + introduce + ", time="
				+ time + "]";
	}

	public UserBean(int id, String user, String nickname, String pwd,
			String number, String sex, String address, String portrait,
			String email, String introduce, String time) {
		super();
		this.id = id;
		this.user = user;
		this.nickname = nickname;
		this.pwd = pwd;
		this.number = number;
		this.sex = sex;
		this.address = address;
		this.portrait = portrait;
		this.email = email;
		this.introduce = introduce;
		this.time = time;
	}
	public UserBean(){
		
	}
	
}
