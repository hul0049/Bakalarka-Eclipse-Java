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

import java.io.IOException;
import java.io.OutputStream;
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

	private SerialPort comm;
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
		if (comm != null && comm.isOpened()) {
			return;
		}
		try {
			comm = new SerialPort(comPort);
			comm.openPort();
			comm.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
			comm.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (SerialPortException e) {
			throw new IOException("ComPort connection error.", e); //$NON-NLS-1$
		}

	}

	/**
	 * 
	 * @throws SerialPortException
	 */
	public void close() throws IOException {
		try {
			comm.closePort();
		} catch (SerialPortException e) {
			throw new IOException("ComPort close error.", e); //$NON-NLS-1$
		}
	}

	public boolean isOpened() {
		return comm.isOpened();
	}

	/**
	 * 
	 * @param size
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	public byte[] read(int size) throws IOException {
		return read(size, DEFAULT_TIMEOUT);
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
		try {
			return comm.readBytes(size, timeout);
		} catch (SerialPortException e) {
			throw new IOException("ComPort read error.", e); //$NON-NLS-1$
		} catch (SerialPortTimeoutException e) {
		}
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	public byte[] read() throws IOException {
		try {
			comm.openPort();
			return comm.readBytes();
		} catch (SerialPortException e) {
			throw new IOException("Read error.", e); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	public void sendData(byte[] data) throws IOException {
		try {
			comm.writeBytes(data);
		} catch (SerialPortException e) {
			throw new IOException("Write error.", e); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 */
	public int availableBytes() throws IOException {
		try {
			return comm.getInputBufferBytesCount();
		} catch (SerialPortException e) {
			throw new IOException("Available bytes error.", e); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	public void sendData(byte data) throws IOException {
		try {
			comm.writeByte(data);
		} catch (SerialPortException e) {
			throw new IOException("Write bytes error.", e); //$NON-NLS-1$
		}
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
				byte[] receivedByte = read(1, timeout);
				if (receivedByte != null && receivedByte[0] == template[index]) {
					index++;
				} else {
					index = 0;
					if (System.currentTimeMillis() - startTime > timeout) {
					}
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
