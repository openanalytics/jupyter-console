/*******************************************************************************
 * Copyright (c) 2016 Open Analytics NV and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package eu.openanalytics.jupyter.console.action;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsole;

import eu.openanalytics.jupyter.console.Activator;
import eu.openanalytics.jupyter.console.JupyterConsole;
import eu.openanalytics.jupyter.console.io.EventType;
import eu.openanalytics.jupyter.console.io.IEventListener;
import eu.openanalytics.jupyter.console.io.SessionEvent;

public class StopSessionAction extends Action {

	private static ImageDescriptor imgEnabled = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_STOP);
	private static ImageDescriptor imgDisabled = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_STOP_DISABLED);
	
	private JupyterConsole console;
	private IEventListener sessionListener;
	
	public StopSessionAction(IConsole console) {
		super();
		setImageDescriptor(imgEnabled);
		setToolTipText("Stop session");
		
		this.console = (JupyterConsole) console;
		sessionListener = new IEventListener() {
			@Override
			public void handle(SessionEvent event) {
				if (event.type == EventType.SessionStopped) {
					setImageDescriptor(imgDisabled);
					setEnabled(false);
				}
			}
		};
		this.console.getSession().addEventListener(sessionListener);
	}

	@Override
	public void run() {
		try {
			console.getSession().stop();
		} catch (IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to stop session", e));
		}
	}

	public void dispose() {
		console.getSession().removeEventListener(sessionListener);
		console = null;
	}
}
