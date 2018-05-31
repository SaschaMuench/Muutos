package de.sonnmatt.muutos.DTOs;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DoubleString implements Serializable, IsSerializable {
	private static final long serialVersionUID = -2075149902320524170L;
	private String data1;
	private String data2;
	
	public DoubleString() {
	}

	public DoubleString(String data1, String data2) {
		super();
		this.data1 = data1;
		this.data2 = data2;
	}

	public String get1() {
		return data1;
	}

	public void set1(String data1) {
		this.data1 = data1;
	}

	public String get2() {
		return data2;
	}

	public void set2(String data2) {
		this.data2 = data2;
	}

}