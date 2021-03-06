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
public abstract class CommunicationWrapper implements Runnable{

	private static final Logger logger = LogManager.getLogger(CommunicationWrapper.class);
	
	public static final int DEFAULT_TIMEOUT = 3000;
	public static final int FREE_LINE_TIMEOUT = 10;


	private boolean end = false;
	private Thread readingThread;
	protected DataTransformer dataTransformer;
	protected boolean record = false;
	protected OutputStream recordStream;

	public CommunicationWrapper(DataTransformer dataTransformer) {
		super();
		this.dataTransformer = dataTransformer;
	}

	public abstract void connect() throws IOException;

	public abstract void close() throws IOException;
	
	public abstract boolean isOpened();

	/**
	 * 
	 * @param size
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */

	protected abstract int read(byte[] buffer , int offset, int size, int timeout) throws IOException;

	protected byte[] read(int size) throws IOException {
		byte[] data = new byte[size];
		int totalReaded = 0;
		while(totalReaded < size){
			int readed = read(data, totalReaded, size - totalReaded, DEFAULT_TIMEOUT);
			if(readed < 0){
				break;
			}
			totalReaded += readed;
		}
		if(totalReaded >= 0 && totalReaded != size){
			data = Arrays.copyOf(data, totalReaded);
		}
		return data;
	}

	/**
	 * 
	 * @param size
	 * @param timeout
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	protected byte[] read(int size, int timeout) throws IOException {
		byte[] data = new byte[size];
		int readed = read(data, 0, size, timeout);
		if(readed >= 0 && readed != size){
			data = Arrays.copyOf(data, readed);
		}
		return data;
	}

	protected abstract byte readByte(int timeout) throws IOException;

	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	protected byte[] read() throws IOException {
		int size = availableBytes();
		byte[] data = new byte[size];
		int readed = read(data, 0, size, DEFAULT_TIMEOUT);
		if(readed >= 0 && readed != size){
			data = Arrays.copyOf(data, readed);
		}
		return data;
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	protected abstract void sendData(byte[] data) throws IOException;

	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 */
	protected abstract int availableBytes() throws IOException;

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	protected abstract void sendData(byte data) throws IOException;
	
	/**
	 * 
	 * @param template
	 * @param timeout
	 * @return
	 * @throws IOException
	 * @throws LineIsBusyException
	 */
	protected boolean waitForTemplate(byte[] template, int timeout) throws IOException {
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
				if(dataTransformer.isControlToSend()){
					byte[] control = dataTransformer.getControlToSend();
					sendData(control);
				}
				if(dataTransformer.isSettingToSend()){
					byte[] setting= dataTransformer.getSettingToSend();
					for (int i = 0; i < setting.length; i++) {
						System.out.print(String.format("%x ", setting[i]));
					}
					System.out.println();
					sendData(setting);
				}
				if(dataTransformer.waitForTamplate()){
					waitForTemplate(dataTransformer.getTempalte(), 200);
				}
				byte[] data = read(dataTransformer.dataCount()); 
				if(dataTransformer.checkTail(data)){
					logger.warn("No ETX on end of packet.");
					continue;
				}
				dataTransformer.processData(data);
				if(record){
					recordStream.write(dataTransformer.getTempalte());
					recordStream.write(data);
				}
			}
			close();
		} catch (IOException e) {
			logger.error("Reading error", e);
		}
	}
}
