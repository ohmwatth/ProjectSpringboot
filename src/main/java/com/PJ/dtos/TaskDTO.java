package com.PJ.dtos;

public class TaskDTO {
	private String title;
    private String description;
    private Long subjectId;
    private String subjectName;
    private String subjectTitle;
  

    
    public TaskDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TaskDTO(String title, String description, Long subjectId, String subjectName, String subjectTitle) {
		super();
		this.title = title;
		this.description = description;
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.subjectTitle = subjectTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectTitle() {
		return subjectTitle;
	}
	public void setSubjectTitle(String subjectTitle) {
		this.subjectTitle = subjectTitle;
	}

	
	
}
    
