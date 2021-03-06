/*******************************************************************************
 * Copyright (c) 2016 Open Analytics NV and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package eu.openanalytics.jupyter.wsclient.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Channel {
	IOPub("iopub"),
	Shell("shell"),
	Control("control"),
	Stdin("stdin");

	private String value;

	private Channel(String value) {
		this.value = value;
	}

	@JsonValue
	@Override
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static Channel fromValue(String value) {
		for (Channel c: values()) {
			if (c.value.equals(value)) return c;
		}
		throw new IllegalArgumentException(value);
	}
}
