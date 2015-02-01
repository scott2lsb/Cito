package com.app.tomore.beans;
import java.io.Serializable;

public class UserModel implements Serializable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getMemberID() {
		return memberID;
	}
	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTotalThread() {
		return totalThread;
	}
	public void setTotalThread(String totalThread) {
		this.totalThread = totalThread;
	}
	
	public String getFollowingNum() {
		return followingNum;
	}
	public void setFollowingNum(String followingNum) {
		this.followingNum = followingNum;
	}

	public String getFollowedNum() {
		return followedNum;
	}
	public void setFollowedNum(String followedNum) {
		this.followedNum = followedNum;
	}

	private String memberID;
	 private String school;
	 private String major;
	 private String address;
	 private String city;
	 private String province;
	 private String postalCode;
	 private String profileImage;
	private String accountName;
	private String status;
	private String email;
	private String phone;
	private String role;
	private String gender;
	private String totalThread;
	private String followingNum;
	private String followedNum;
	private String image;
	private String followed;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getFollowed() {
		return followed;
	}
	public void setFollowed(String followed) {
		this.followed = followed;
	}
	
    	 
}

