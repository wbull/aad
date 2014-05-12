package com.wtb.fragment.tasks;

public class Task {
	
	//private variables
	private int id;
	private String taskDetails;
	
	public Task() {}
	
	public Task(String taskDetails) {
		super();
		this.taskDetails = taskDetails;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskDetails() {
		return taskDetails;
	}

	public void setTaskDetails(String taskDetails) {
		this.taskDetails = taskDetails;
	}

	//Added this for use by the ArrayAdapter in ListView
	@Override
	public String toString(){
		return taskDetails.toString();
	}

}
