/**
 * © Copyright IBM Corporation 2016.
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.ibm.appscan.jenkins.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import jenkins.model.Jenkins;

import com.ibm.appscan.plugin.core.auth.IAuthenticationProvider;
import com.ibm.appscan.plugin.core.logging.IProgress;
import com.ibm.appscan.plugin.core.scan.IScan;
import com.ibm.appscan.plugin.core.scan.IScanFactory;

public final class ScanFactory {

	private static final ServiceLoader<IScanFactory> LOADER = ServiceLoader.load(IScanFactory.class,
			Jenkins.getInstance().getPluginManager().uberClassLoader);
	
	public static List<String> getScanTypes() {
		ArrayList<String> types = new ArrayList<String>();
		for(IScanFactory factory : LOADER)
			types.add(factory.getType());		
		return types;
	}
	
	public static IScan getScan(String type, Map<String, String> properties, IProgress progress, IAuthenticationProvider authProvider) {
		for(IScanFactory factory : LOADER) {
			if(factory.getType().equalsIgnoreCase(type)) {
				return factory.create(properties, progress, authProvider);
			}
		}
		return null;
	}
}