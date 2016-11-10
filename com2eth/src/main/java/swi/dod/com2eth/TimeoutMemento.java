package swi.dod.com2eth;

import java.io.InputStream;

public class TimeoutMemento {
	private int oldTimeout;
	private ITimeoutProvider provider;
	public TimeoutMemento(InputStream is, int timeout) {
		super();
		if (is instanceof ITimeoutProvider) {
			provider = (ITimeoutProvider) is;
			oldTimeout = provider.getTimeout();
			provider.setTimeout(timeout);
		}
	}
	
	
	public void restoreTimeout() {
		if(provider != null) {
			provider.setTimeout(oldTimeout);
		}
	}
	
}
