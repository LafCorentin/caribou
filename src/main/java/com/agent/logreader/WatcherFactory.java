package com.agent.logreader;

import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;

import com.agent.ParamAgent;

public class WatcherFactory {
	private Path logPath;
	private static final String regexDebutLog = "(\\d\\d:\\d\\d:\\d\\d[^\n]*\n)*\\d\\d:\\d\\d:\\d\\d[^\n]*Inbound Message";
	private static final String regexFinLog = "(.*?-{10,}){4}";
	private static final String regexFiltre = ".";

	public WatcherFactory(Path logPath) {
		this.logPath = logPath;
	}

	public WatcherFactory addMyWatcher(String regexDefault, String outputPath) throws Exception {
		ParamAgent paramDefault = new ParamAgent(regexDebutLog, regexFinLog, regexFiltre,5000,30000,100)  ;
		MyWatcher watcher = new MyWatcher(logPath, paramDefault, outputPath);
		logPath.toAbsolutePath().getParent().register(watcher.getService(), StandardWatchEventKinds.ENTRY_MODIFY);
		watcher.run();
		return this;
	}

}
