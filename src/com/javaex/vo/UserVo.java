package com.javaex.vo;

public class UserVo {

	private int no;
	private String id;
	private String pw;
	private String name;
	private String gender;

	public UserVo() {
		super();
	}

	

	public UserVo(int no, String pw, String name, String gender) {
		super();
		this.no = no;
		this.pw = pw;
		this.name = name;
		this.gender = gender;
	}



	public UserVo(String id, String pw, String name, String gender) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.gender = gender;
	}



	public UserVo(int no, String id, String pw, String name, String gender) {
		super();
		this.no = no;
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.gender = gender;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "UserVo [no=" + no + ", id=" + id + ", pw=" + pw + ", name=" + name + ", gender=" + gender + "]";
	}

}