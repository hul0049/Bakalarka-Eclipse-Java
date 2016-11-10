package swi.dod.com2eth;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

public class Run {
	private static final Logger logger = LogManager.getLogger(Rs485Line.class);
	public static void main(String[] args) throws IOException {
//		String xml = System.getProperty("log4j.properties");
//		if(xml != null) {
//			PropertyConfigurator.configure(xml);
//		}
		int portNumber = Integer.parseInt(args[0]);
		String commPort = args[1];
		if(logger.isInfoEnabled()) {
			logger.info( String.format("Accept connection on port  %d and conecting to serial port %s", portNumber,commPort));
		}
		new Bridge(portNumber, commPort).doBridge();
	}

	
}
