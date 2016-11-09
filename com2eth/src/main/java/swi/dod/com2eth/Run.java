package swi.dod.com2eth;

import java.io.IOException;

public class Run {

	public static void main(String[] args) throws IOException {
		int portNumber = Integer.parseInt(args[0]);
		String commPort = args[1];
		new Bridge(portNumber, commPort).doBridge();
	}

}
