package swi.dod.com2eth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * Hello world!
 *
 */
public class Bridge2 {
	public static final int LISTENSERVER_PORT = 40460;

	private static final Logger logger = LogManager.getLogger(Bridge2.class);

	private Socket socketFromClient;
	private Thread listenThread;
	private Thread com2ethThread;
	private Thread eth2comThread;
	private String comPort;
	private SerialPort port;

	public Bridge2(String comPort) throws SerialPortException {
		super();
		this.comPort = comPort;
		port = new SerialPort(comPort);
		System.out.println(port.openPort());
		port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
		port.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);

		System.out.println(Arrays.toString(port.readBytes()));
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(SerialPortList.getPortNames()));
		if (args.length < 1) {
			System.out.println("Arg comm port needed.");
		} else {
			try {
				new Bridge2(args[0]).listen();
			} catch (SerialPortException e) {
				logger.error("Error", e);
			}
		}
	}

	/**
	 * listen
	 */
	public void listen() {
		try (ServerSocket serverSocket = new ServerSocket(LISTENSERVER_PORT)) {
			while (!Thread.currentThread().isInterrupted()) {
				Socket newClient = serverSocket.accept();
				if (socketFromClient == null) {
					socketFromClient = newClient;
					logger.info(String.format("New client from %s", socketFromClient.getRemoteSocketAddress()));
					com2ethThread = new Thread(new Runnable() {
						@Override
						public void run() {
							com2eth();
						}
					});
					com2ethThread.start();
					eth2comThread = new Thread(new Runnable() {
						@Override
						public void run() {
							eth2com();
						}
					});
					eth2comThread.start();
				} else {
					logger.info(String.format("Too many client from %s", socketFromClient.getRemoteSocketAddress()));
					newClient.close();
				}
			}
		} catch (IOException e) {
			logger.error("Main thread exception", e);
		}
		if (com2ethThread != null) {
			com2ethThread.interrupt();
		}
		if (eth2comThread != null) {
			eth2comThread.interrupt();
		}
	}

	/**
	 * com2eth
	 */
	public void com2eth() {
		try {
			logger.trace("Start reading data from USB.");
			OutputStream out = socketFromClient.getOutputStream();
			while (!Thread.currentThread().isInterrupted()) {
//				logger.trace("Reading...");
				byte[] data = port.readBytes();
//				logger.trace("Readed from USB " + (data != null?data.length:"null"));
				if(data != null){
					out.write(data);
				}
			}
		} catch (IOException | SerialPortException e) {
			logger.error("Com2Eth error:", e);
		}
		closeSocket();
	}

	private synchronized void closeSocket(){
		if(socketFromClient != null){
			try {
				socketFromClient.close();
			} catch (IOException e) {
				logger.error("Error close socket.", e);
			}
			socketFromClient = null;
		}
	}
	/**
	 * eth2com
	 */
	public void eth2com() {
		try {
			InputStream in = socketFromClient.getInputStream();
			while (!Thread.currentThread().isInterrupted()) {
				int data = in.read();
				port.writeByte((byte) data);
			}
		} catch (IOException | SerialPortException e) {
			logger.error("ETh2Com error:", e);
		}
		closeSocket();
	}

}
