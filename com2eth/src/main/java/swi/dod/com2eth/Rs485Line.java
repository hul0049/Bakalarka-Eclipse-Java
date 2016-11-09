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

package swi.dod.com2eth;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * @author David Ježek
 *
 */
@SuppressWarnings("unused")
public class Rs485Line implements Closeable{

	
	private static final Logger logger = LogManager.getLogger(Rs485Line.class);
	private String comPort = "com29"; //$NON-NLS-1$
	public static final int DEFAULT_TIMEOUT = 3000;
	public static final int FREE_LINE_TIMEOUT = 10;

	public static final byte[] DATA_HEADER = new byte[] { (byte) 0x02, (byte) 0x19, (byte) 0x01, (byte) 0x01 };

	private SerialPort comm;
	
	private int timeout = DEFAULT_TIMEOUT;
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
	
	public int getTimeout() {
		return timeout;
	}


	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public InputStream getInputStream() {
		return new P_InputStream();
	}
	
	public OutputStream getOutputStream() {
		return new P_OutputStream();
	}

	/**
	 * 
	 * @param size
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	private byte[] read(int size) throws IOException {
		return read(size, timeout);
	}

	/**
	 * 
	 * @param size
	 * @param timeout
	 * @return
	 * @throws SerialPortException
	 * @throws SerialPortTimeoutException
	 */
	private byte[] read(int size, int timeout) throws IOException {
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
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	private void sendData(byte[] data) throws IOException {
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
	private int availableBytes() throws IOException {
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
	private void sendData(byte data) throws IOException {
		try {
			comm.writeByte(data);
		} catch (SerialPortException e) {
			throw new IOException("Write bytes error.", e); //$NON-NLS-1$
		}
	}

	private class P_OutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			Rs485Line.this.sendData((byte)(b & 0xff));
		}
		
		public void write(byte b[], int off, int len) throws IOException {
	        if (b == null) {
	            throw new NullPointerException();
	        } else if ((off < 0) || (off > b.length) || (len < 0) ||
	                   ((off + len) > b.length) || ((off + len) < 0)) {
	            throw new IndexOutOfBoundsException();
	        } else if (len == 0) {
	            return;
	        }
	        byte[] buffer;
	        if(len != b.length || off!=0) {
	        	buffer = new byte[len];
	        	System.arraycopy(b, off, buffer, 0, len);
	        } else {
	        	buffer = b;
	        }
	        Rs485Line.this.sendData(buffer);
	    }
	}
	
	private class P_InputStream extends InputStream {

		@Override
		public int read() throws IOException {
			byte []result = new byte[1];
			int number = read(result);
			if(number == 0) {
				throw new IllegalStateException();
			} else if(number == -1) {
				return -1;
			} else {
				return result[0];
			}
		}
		
		@Override
		public int read(byte[] b, int off, int len ) throws IOException {
			if (b == null) {
	            throw new NullPointerException();
	        } else if (off < 0 || len < 0 || len > b.length - off) {
	            throw new IndexOutOfBoundsException();
	        } else if (len == 0) {
	            return 0;
	        }
			byte[] result = Rs485Line.this.read(len);
			System.arraycopy(result, 0, b, off, result.length);
			return result.length;
		}
		
		@Override
		public int available() throws IOException {
	        return availableBytes();
	    }
		
		
	}
	

}
