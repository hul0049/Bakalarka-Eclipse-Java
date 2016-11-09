package swi.dod.com2eth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Bridge 
{
    private static final int BUFFER_SIZE = 1024*1024;
	private int portNumber;
    private String commPort;
	public Bridge(int portNumber, String commPort) {
		super();
		this.portNumber = portNumber;
		this.commPort = commPort;
	}
    
    public void doBridge() throws IOException {
    	try(ServerSocket ss = new ServerSocket(portNumber, 1)) {
	    	while(true) {
	    		try(final Socket soc = ss.accept();
	    		  final Rs485Line line = new Rs485Line(commPort);
	    		  final InputStream isSocket = soc.getInputStream();
	    		  final OutputStream osSocket = soc.getOutputStream();) {
	    			line.connect();
	    			final InputStream isLine = line.getInputStream();
	    			final OutputStream osLine = line.getOutputStream();
		    		Thread t1 = new Thread(() -> copyDataBetweenStreams(isSocket, osLine));
		    		Thread t2 = new Thread(() -> copyDataBetweenStreams(isLine, osSocket));
		    		t1.start();
		    		t2.start();
		    		joinThreadOrInterrupt(t1);
		    		joinThreadOrInterrupt(t2);
	    		}  catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
    	}
    }

	private void joinThreadOrInterrupt(Thread t1) {
		try {
			t1.join();
		} catch (InterruptedException e) {
			t1.interrupt();
		}
	}

	private void copyDataBetweenStreams( InputStream is, OutputStream os) {
		byte []buffer = new byte[BUFFER_SIZE];
		boolean stop = false;
		int len;
		try {
			while(!Thread.interrupted() && !stop && -1 != (len = is.read(buffer))){
				os.write(buffer, 0, len);
			}
		} catch (IOException e) {
			//only LOG
			e.printStackTrace();
		}
	}
    
}
