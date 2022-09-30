package com.vietdp.vietdp.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO extends AbstractDTO{
    private String firstName;
    
    private String lastName;
    
    private String email;
    
    private String displayName;
    
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDisplayName() {
		return firstName + " " + lastName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
    
    
    
    
}
