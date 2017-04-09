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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPortException;

/**
 * @author David Ježek
 *
 */
public class CommunicationWrapperTcp extends CommunicationWrapper{

	private static final Logger logger = LogManager.getLogger(CommunicationWrapperTcp.class);
	private String address = "otfeia406a.vsb.cz"; //$NON-NLS-1$
	private int port = 40460;
	
	private Socket socket;
	private InputStream socketInput;
	private OutputStream socketOutput;

	/**
	 * 
	 * @param comPort
	 */
	public CommunicationWrapperTcp(DataTransformer tfc, String address) {
		super(tfc);
		String[] parts = address.split(":");
		int port = 40460;
		if(parts.length > 1){
			try {
				this.port = Integer.parseInt(parts[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		init(parts[0], port);
	}

	public CommunicationWrapperTcp(Tfc tfc, String address, int port) {
		super(tfc);
		init(address, port);
	}

	private void init(String address, int port){
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void connect() throws IOException {
		 socket = new Socket(InetAddress.getByName(address), port);
		 socketInput  = socket.getInputStream();
		 socketOutput = socket.getOutputStream();
	}

	/**
	 * 
	 * @throws SerialPortException
	 */
	public void close() throws IOException {
		socket.close();
	}

	public boolean isOpened() {
		return !socket.isClosed();
	}

	@Override
	protected int read(byte[] buffer, int offset, int size, int timeout) throws IOException{
		socket.setSoTimeout(timeout);
		return socketInput.read(buffer, offset, size);
	}
	
	@Override
	protected byte readByte(int timeout) throws IOException {
		socket.setSoTimeout(timeout);
		return (byte)socketInput.read();
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	@Override
	protected void sendData(byte[] data) throws IOException {
		socketOutput.write(data);
	}

	/**
	 * 
	 * @return
	 * @throws SerialPortException
	 */
	@Override
	protected int availableBytes() throws IOException {
		return socketInput.available();
	}

	/**
	 * 
	 * @param data
	 * @throws LineIsBusyException
	 * @throws SerialPortException
	 */
	@Override
	protected void sendData(byte data) throws IOException {
		socketOutput.write(data);
	}

	public static void main(String[] args) {
		CommunicationWrapperTcp line = new CommunicationWrapperTcp(new Tfc(), "otfeia406a.vsb.cz", 40460);
		line.startRead();
	}
}
