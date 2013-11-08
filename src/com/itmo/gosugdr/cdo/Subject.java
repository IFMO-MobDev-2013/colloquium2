package com.itmo.gosugdr.cdo;

public class Subject {
	private long id;
	private String label;
	private int total;
	public Subject(long id, String label) {
		super();
		this.id = id;
		this.label = label;
	}
	public Subject() {
		super();
	}
	public long getId() {
		return id;
	}
	public Subject(String label) {
		super();
		this.label = label;
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
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	
}
