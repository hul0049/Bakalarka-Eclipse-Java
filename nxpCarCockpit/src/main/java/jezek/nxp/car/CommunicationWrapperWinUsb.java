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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * @author David Ježek
 *
 */
public class CommunicationWrapperWinUsb extends CommunicationWrapper{

	private static final Logger logger = LogManager.getLogger(CommunicationWrapperWinUsb.class);
	private String comPort = "com29"; //$NON-NLS-1$
	
	private SerialPort comm;

	/**
	 * 
	 * @param comPort
	 */
	public CommunicationWrapperWinUsb(Tfc tfc, String comPort) {
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
	@Override
	public void close() throws IOException {
		try {
			comm.closePort();
		} catch (SerialPortException e) {
			throw new IOException("ComPort close error.", e); //$NON-NLS-1$
		}
	}

	@Override
	public boolean isOpened() {
		return comm.isOpened();
	}

	@Override
	public int read(byte[] buffer, int offset, int size, int timeout) throws IOException{
		try {
			byte[] data = comm.readBytes(size, timeout);
			BinaryUtils.copyInto(data, buffer, offset);
			return data.length;
		} catch (SerialPortException e) {
			throw new IOException("ComPort read error.", e); //$NON-NLS-1$
		} catch (SerialPortTimeoutException e) {
			throw new IOException("timeout exceded.", e);
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
	public byte[] read(int size, int timeout) throws IOException {
		try {
			return comm.readBytes(size, timeout);
		} catch (SerialPortException e) {
			throw new IOException("ComPort read error.", e); //$NON-NLS-1$
		} catch (SerialPortTimeoutException e) {
			throw new IOException("timeout exceded.", e);
		}
	}

	@Override
	public byte readByte(int timeout) throws IOException {
		try {
			byte[] data = comm.readBytes(1, timeout);
			return data[0];
		} catch (SerialPortException e) {
			throw new IOException("ComPort read error.", e); //$NON-NLS-1$
		} catch (SerialPortTimeoutException e) {
			throw new IOException("timeout exceded.", e);
		}
	}

	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void sendData(byte data) throws IOException {
		try {
			comm.writeByte(data);
		} catch (SerialPortException e) {
			throw new IOException("Write bytes error.", e); //$NON-NLS-1$
		}
	}


	public static void main(String[] args) {
		CommunicationWrapperWinUsb line = new CommunicationWrapperWinUsb(new Tfc(), "com30");
		line.startRead();
	}
}
