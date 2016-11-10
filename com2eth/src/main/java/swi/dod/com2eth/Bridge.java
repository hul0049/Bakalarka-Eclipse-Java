package swi.dod.com2eth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class Bridge 
{
	private static final Logger logger = LogManager.getLogger(Bridge.class);
	
    //private static final int BUFFER_SIZE = 1024*1024;
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
	    		String hostAdress = null;
	    		try(final Socket soc = ss.accept();
	    		  final Rs485Line line = new Rs485Line(commPort);
	    		  final InputStream isSocket = soc.getInputStream();
	    		  final OutputStream osSocket = soc.getOutputStream();) {
	    			hostAdress = soc.getInetAddress().getCanonicalHostName();
	    			if(logger.isInfoEnabled()){
	    				logger.info(String.format("Accepted connection from host %s.", hostAdress));
	    			}
	    			line.connect();
	    			final InputStream isLine = line.getInputStream();
	    			final OutputStream osLine = line.getOutputStream();
		    		Thread t1 = new Thread(() -> copyDataBetweenStreams(isSocket, osLine, Tfc.CONTROL_LENGTH, null));
		    		Thread t2 = new Thread(() -> copyDataBetweenStreams(isLine, osSocket,Tfc.DATA_LENGTH + 1, Tfc.DATA_HEADER));
		    		t1.start();
		    		t2.start();
		    		joinThreadOrInterrupt(t1);
		    		joinThreadOrInterrupt(t2);
	    		}  catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    		if(logger.isInfoEnabled() && hostAdress != null){
    				logger.info(String.format("Connection with host %s was closed.", hostAdress));
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

	private void copyDataBetweenStreams( InputStream is, OutputStream os, int size, byte[] allignHeader) {
		byte []buffer = new byte[size];
		
		int len;
		try {
			while(!Thread.interrupted()){
				if(allignHeader != null) {
					if(!waitForHeader(is,os, allignHeader)) {
						break;
					}
				}
				len = is.read(buffer);
				if(len == -1) {
					break;
				}
 				os.write(buffer, 0, len);
				os.flush();
			}
		} catch (IOException e) {
			//only LOG
			e.printStackTrace();
		}
	}

	private boolean waitForHeader(InputStream is, OutputStream os, byte[] allignHeader) {
		TimeoutMemento memento = new TimeoutMemento(is, 200);
		try {
			int index = 0;
			byte[] b = new byte[1];
			while (index < allignHeader.length) {
				int receivedBytes = is.read(b);
				if (receivedBytes != 0 && b[0] == allignHeader[index]) {
					index++;
				} else {
					index = 0;
				}
				if(receivedBytes != 0) {
					os.write(b);
				}
			}
			return true;
		} catch (SocketTimeoutException e) {
			logger.error("Timeout.", e);
		} catch(IOException e) {
			logger.error( e);
		} finally {
			memento.restoreTimeout();
		}
		return false;
	}
    
}
