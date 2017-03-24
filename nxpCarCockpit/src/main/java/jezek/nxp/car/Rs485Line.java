/*
 * Copyright 2015 David Jezek
 * 
 * This file is part of JMTP.
 * 
 * JTMP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or any later version.
 * 
 * JMTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU LesserGeneral Public 
 * License along with JMTP. If not, see <http://www.gnu.org/licenses/>.
 */

package jezek.nxp.car;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import javax.sound.sampled.Line;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * @author David Ježek
 *
 */
public class Rs485Line implements Runnable{

	private static final Logger logger = LogManager.getLogger(Rs485Line.class);
	private String comPort = "com29"; //$NON-NLS-1$
	
	public static final int DEFAULT_TIMEOUT = 3000;
	public static final int FREE_LINE_TIMEOUT = 10;

	public static final byte[] DATA_HEADER = new byte[] { (byte) 0x02, (byte) 0x19, (byte) 0x01, (byte) 0x01 };

//	private SerialPort comm;
//	private Socket socket;
	private InputStream socketInput;
	private OutputStream socketOutput;
	private Random random = new Random();
	private boolean end = false;
	private Thread readingThread;
	private Tfc tfc = new Tfc();
	private boolean record = false;
	private OutputStream recordStream;
	/**
	 * 
	 * @param comPort
	 */
	public Rs485Line(String comPort) {
		super();
		this.comPort = comPort;
	}

	
	public String getComPort() {
		return comPort;
	}


	public void setComPort(String comPort) {
		this.comPort = comPort;
	}


	public void connect() throws IOException {
//		 socket = new Socket(InetAddress.getByName(comPort), 40460);
//		 socketInput  = socket.getInputStream();
//		 socketOutput = socket.getOutputStream();
		 socketInput  = new FileInputStream(comPort);
		 socketOutput = new FileOutputStream(comPort);
//		if (comm != null && comm.isOpened()) {
//			return;
//		}
//		try {
//			comm = new SerialPort(comPort);
//			comm.openPort();
//			comm.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
//			comm.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
//					SerialPort.PARITY_NONE);
//		} catch (SerialPortException e) {
//			throw new IOException("ComPort connection error.", e); //$NON-NLS-1$
//		}

	}

	/**
	 * 
	 * @throws SerialPortException
	 */
	public void close() throws IOException {
//		socket.close();
		socketInput.close();
		socketOutput.close();
//		try {
//			comm.closePort();
//		} catch (SerialPortException e) {
//			throw new IOException("ComPort close error.", e); //$NON-NLS-1$
//		}
	}

	public boolean isOpened() {
//		return !socket.isClosed();
		return false;
//		return comm.isOpened();
	}

	/**
	 * 
	 * @param size
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	public byte[] read(int size) throws IOException {
		byte[] data = new byte[size];
		int totalReaded = 0;
//		socket.setSoTimeout(0);
		while(totalReaded < size){
			int readed = socketInput.read(data, totalReaded, size - totalReaded);
			if(readed < 0){
				break;
			}
			totalReaded += readed;
		}
		if(totalReaded >= 0 && totalReaded != size){
			data = Arrays.copyOf(data, totalReaded);
		}
		return data;
//		return read(size, DEFAULT_TIMEOUT);
	}

	/**
	 * 
	 * @param size
	 * @param timeout
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	public byte[] read(int size, int timeout) throws IOException {
		byte[] data = new byte[size];
//		socket.setSoTimeout(timeout);
		int readed = socketInput.read(data, 0, size);
		if(readed >= 0 && readed != size){
			data = Arrays.copyOf(data, readed);
		}
		return data;
//		try {
//			return comm.readBytes(size, timeout);
//		} catch (SerialPortException e) {
//			throw new IOException("ComPort read error.", e); //$NON-NLS-1$
//		} catch (SerialPortTimeoutException e) {
//		}
//		return null;
	}

	public byte readByte(int timeout) throws IOException {
//		socket.setSoTimeout(timeout);
		return (byte)socketInput.read();
//		try {
//			return comm.readBytes(size, timeout);
//		} catch (SerialPortException e) {
//			throw new IOException("ComPort read error.", e); //$NON-NLS-1$
//		} catch (SerialPortTimeoutException e) {
//		}
//		return null;
	}

	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	public byte[] read() throws IOException {
		int size = socketInput.available();
		byte[] data = new byte[size];
		int readed = socketInput.read(data, 0, size);
		if(readed >= 0 && readed != size){
			data = Arrays.copyOf(data, readed);
		}
		return data;
//		try {
//			comm.openPort();
//			return comm.readBytes();
//		} catch (SerialPortException e) {
//			throw new IOException("Read error.", e); //$NON-NLS-1$
//		}
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	public void sendData(byte[] data) throws IOException {
		socketOutput.write(data);
//		try {
//			comm.writeBytes(data);
//		} catch (SerialPortException e) {
//			throw new IOException("Write error.", e); //$NON-NLS-1$
//		}
	}

	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 */
	public int availableBytes() throws IOException {
		return socketInput.available();
//		try {
//			return comm.getInputBufferBytesCount();
//		} catch (SerialPortException e) {
//			throw new IOException("Available bytes error.", e); //$NON-NLS-1$
//		}
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	public void sendData(byte data) throws IOException {
		socketOutput.write(data);
//		try {
//			comm.writeByte(data);
//		} catch (SerialPortException e) {
//			throw new IOException("Write bytes error.", e); //$NON-NLS-1$
//		}
	}

	/**
	 * 
	 * @param template
	 * @param timeout
	 * @return
	 * @throws IOException
	 * @throws LineIsBusyException
	 */
	public boolean waitForTemplate(byte[] template, int timeout) throws IOException {
		try {
			int index = 0;
			long startTime = System.currentTimeMillis();
			while (index < template.length) {
				try {
					byte receivedByte = readByte(timeout);
					if (receivedByte == template[index]) {
						index++;
					} else {
						index = 0;
						if (System.currentTimeMillis() - startTime > timeout) {
						}
					}
				} catch (SocketTimeoutException e) {
				}
			}
			return true;
		} catch (SocketTimeoutException e) {
			logger.error("Timeout.", e);
		}
		return false;
	}

	public void startRecording(OutputStream recordStream){
		if(!record && this.recordStream == null){
			this.recordStream = recordStream;
			record = true;
		}
	}
	public void stopRecording(){
		record = false;
		try {
			if(recordStream != null){
				this.recordStream.flush();
				this.recordStream.close();
			}
		} catch (IOException e) {
			logger.error("Close record stream erro.", e);
		}
		this.recordStream = null;
	}

	public void startRead() {
		readingThread = new Thread(this);
		readingThread.start();

	}
	
	@Override
	public void run(){
		try {
			connect();
			while (!end && !Thread.currentThread().isInterrupted()) {
				byte[] control = tfc.getControl();
				sendData(control);
				if(tfc.isSendSetting()){
					byte[] setting= tfc.getSetting();
					for (int i = 0; i < setting.length; i++) {
						System.out.print(String.format("%x ", setting[i]));
					}
					System.out.println();
					sendData(setting);
				}
				waitForTemplate(DATA_HEADER, 200);
				byte[] data = read(277);
				if(data[276] != Tfc.ETX){
					logger.warn("No ETX on end of packet.");
					continue;
				}
				tfc.setData(data);
				if(record){
					recordStream.write(Tfc.DATA_HEADER);
					recordStream.write(data);
				}
			}
			close();
		} catch (IOException e) {
			logger.error("Reading error", e);
		}
	}

	public static void main(String[] args) {
		Rs485Line line = new Rs485Line("com30");
		line.startRead();
	}

	public Tfc getTfc() {
		return tfc;
	}
}
