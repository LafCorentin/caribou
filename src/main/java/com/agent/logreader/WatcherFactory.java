package com.agent.logreader;

import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;

import com.agent.ParamAgent;

public class WatcherFactory {

	public static MyWatcher addMyWatcher(ParamAgent param) throws Exception {
		MyWatcher watcher = new MyWatcher(param);
		Paths.get(param.getLogPath()).toAbsolutePath().getParent().register(watcher.getService(),
				StandardWatchEventKinds.ENTRY_MODIFY);
		if (watcher.connect()) {
			watcher.run();
			return watcher;
		} else
			return null;
	}
}
