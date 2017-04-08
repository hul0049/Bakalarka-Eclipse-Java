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
public class CommunicationWrapperLinuxUsb extends CommunicationWrapper {

	private static final Logger logger = LogManager.getLogger(CommunicationWrapperLinuxUsb.class);
	private String comPort = "com29"; //$NON-NLS-1$

	private InputStream socketInput;
	private OutputStream socketOutput;
	private byte[] oneByteBuffer = new byte[1];

	/**
	 * 
	 * @param comPort
	 */
	public CommunicationWrapperLinuxUsb(Tfc tfc, String comPort) {
		super(tfc);
		this.comPort = comPort;
	}

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	@Override
	public void connect() throws IOException {
		socketInput = new FileInputStream(comPort);
		socketOutput = new FileOutputStream(comPort);
	}

	/**
	 * 
	 * @throws SerialPortException
	 */
	@Override
	public void close() throws IOException {
		socketInput.close();
		socketOutput.close();
	}

	@Override
	public boolean isOpened() {
		try {
			socketInput.available();
			socketOutput.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param size
	 * @param timeout
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	@Override
	public int read(byte[] buffer, int offset, int size, int timeout) throws IOException {
		return socketInput.read(buffer, offset, size);
	}

	@Override
	public byte readByte(int timeout) throws IOException {
		if(socketInput.read(oneByteBuffer, 0, 1) == 1 ){
			return oneByteBuffer[0];
		}
		throw new IOException("Read somethin else then one byte.");
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	@Override
	public void sendData(byte[] data) throws IOException {
		socketOutput.write(data);
	}
	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 */
	@Override
	public int availableBytes() throws IOException {
		return socketInput.available();
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	@Override
	public void sendData(byte data) throws IOException {
		socketOutput.write(data);
	}

	public static void main(String[] args) {
		CommunicationWrapperLinuxUsb line = new CommunicationWrapperLinuxUsb(new Tfc(), "com30");
		line.startRead();
	}
}
