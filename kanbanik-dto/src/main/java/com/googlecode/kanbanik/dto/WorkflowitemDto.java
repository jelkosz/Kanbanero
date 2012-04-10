package com.googlecode.kanbanik.dto;

import java.util.ArrayList;
import java.util.List;

public class WorkflowitemDto implements KanbanikDto {

	private static final long serialVersionUID = 1343045359919670502L;

	private String name;
	
	private String id;
	
	private int wipLimit;
	
	private List<WorkflowitemDto> children = new ArrayList<WorkflowitemDto>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getWipLimit() {
		return wipLimit;
	}

	public void setWipLimit(int wipLimit) {
		this.wipLimit = wipLimit;
	}

	public List<WorkflowitemDto> getChildren() {
		return children;
	}
	
	public void addChild(WorkflowitemDto child) {
		children.add(child);
	}
	
}
