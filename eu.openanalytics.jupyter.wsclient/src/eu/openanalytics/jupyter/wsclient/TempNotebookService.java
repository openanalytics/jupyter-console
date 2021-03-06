/*******************************************************************************
 * Copyright (c) 2016 Open Analytics NV and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package eu.openanalytics.jupyter.wsclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eu.openanalytics.jupyter.wsclient.util.HTTPUtil;
import eu.openanalytics.jupyter.wsclient.util.JSONUtil;

public class TempNotebookService {

	private static final String SPAWN_SERVER_URL = "api/spawn";
	
	private Map<String, String> activeNotebooks;
	
	public TempNotebookService() {
		activeNotebooks = new HashMap<>();
	}
	
	public String spawn(String baseUrl) throws IOException {
		String url = HTTPUtil.concat(baseUrl, SPAWN_SERVER_URL);
		String res = HTTPUtil.post(url, null, 200);
		Map<String, Object> resMap = JSONUtil.toMap(res);
		if (resMap.containsKey("status") && "full".equals(resMap.get("status"))) {
			throw new IOException("Failed to spawn notebook: the server appears to be temporarily out of notebooks");
		}
		String notebookUrl = resMap.get("url").toString();
		if (!notebookUrl.startsWith("http")) notebookUrl = HTTPUtil.concat(baseUrl, notebookUrl);
		activeNotebooks.put(baseUrl, notebookUrl);
		return notebookUrl;
	}
	
	public String getNotebook(String baseUrl) throws IOException {
		String url = activeNotebooks.get(baseUrl);
		if (url == null) spawn(baseUrl);
		return activeNotebooks.get(baseUrl);
	}
}
