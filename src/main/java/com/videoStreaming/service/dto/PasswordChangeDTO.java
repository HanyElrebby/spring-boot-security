package com.videoStreaming.service.dto;

public class PasswordChangeDTO {
	private String currentPassword;
	private String newPassword;
	
	public PasswordChangeDTO(String currentPassword, String newPassword) {
		super();
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}
	
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
