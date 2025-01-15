package com.alura.forumhub.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class Content {
	private Boolean active = true;
	private Boolean edited = false;
	
	@Column(name="last_edited")
	private LocalDateTime lastEdited = null;
	
	public void deactivate() {
		this.active = false;
	}
	
	public void activate() {
		this.active = true;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setEdit() {
		this.edited = true;
		this.lastEdited = LocalDateTime.now();
	}
}