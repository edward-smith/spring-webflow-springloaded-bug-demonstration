package io.tednology;

import java.io.Serializable;

public class Form implements Serializable {

	NotSerializable notSerializable;

	public Form() {	}

	public NotSerializable getNotSerializable() {
		return notSerializable;
	}

	public void setNotSerializable(NotSerializable notSerializable) {
		this.notSerializable = notSerializable;
	}

	private static final long serialVersionUID = -3431439855814174496L;
}
