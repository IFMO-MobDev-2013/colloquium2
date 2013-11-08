package com.itmo.gosugdr.cdo;

public class Task {
	long id;
	long subjectId;
	String label;
	double score;
	
	public Task(long id, String label, double amount, int subjectId) {
		super();
		this.id = id;
		this.label = label;
		this.score = amount;
		this.subjectId = this.subjectId;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public Task(String label, double amount, int subjectId) {
		super();
		this.label = label;
		this.score = amount;
		this.subjectId = subjectId;
	}
	
	
	public Task() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getAmount() {
		return score;
	}
	public void setAmount(double amount) {
		this.score = amount;
	}
	
}
