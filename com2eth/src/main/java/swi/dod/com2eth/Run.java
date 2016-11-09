package swi.dod.com2eth;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Run {
	private static final Logger logger = LogManager.getLogger(Rs485Line.class);
	public static void main(String[] args) throws IOException {
		int portNumber = Integer.parseInt(args[0]);
		String commPort = args[1];
		if(logger.isErrorEnabled()) {
			logger.error( String.format("Accept connection on port %d and conecting to serial port %s", portNumber,commPort));
		}
		new Bridge(portNumber, commPort).doBridge();
	}

}
