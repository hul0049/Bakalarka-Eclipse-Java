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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author David Jezek
 *
 */
public class BinaryUtils {

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static int toInt(byte[] data){
		return toInt(data, 0);
	}

	/**
	 * 
	 * @param data
	 * @param offSet
	 * @return
	 */
	public static int toInt(byte[] data, int offSet){
		ByteBuffer buffer = ByteBuffer.wrap(data, offSet, Integer.BYTES);
		buffer.order(ByteOrder.BIG_ENDIAN);
		return buffer.getInt();
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] toBytes(int i){
		ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES);  
		buf.putInt(i);
		return buf.array();
	}

	/**
	 * 
	 * @param l
	 * @return
	 */
	public static byte[] toBytes(long l){
		ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);  
		buf.putLong(l);
		return buf.array();
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] toBytes(short s){
		ByteBuffer buf = ByteBuffer.allocate(Short.BYTES);  
		buf.putShort(s);
		return buf.array();
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static short toShort(byte[] data){
		return toShort(data, 0);
	}

	
	/**
	 * 
	 * @param data
	 * @param offSet
	 * @return
	 */
	public static short toShort(byte[] data, int offSet){
		ByteBuffer buffer = ByteBuffer.wrap(data, offSet, Short.BYTES);
		return buffer.getShort();
	}
	
	/**
	 * 
	 * @param source
	 * @param soureStart
	 * @param sourceLength
	 * @param dest
	 * @param destStart
	 * @return delku kopirovanzch dat
	 */
	public static int copyInto(byte[] source, int soureStart, int sourceLength, byte[] dest, int destStart){
		for(int i=0; i<sourceLength; i++){
			dest[destStart+i] = source[soureStart+i];
		}
		return sourceLength;
	}
	
	/**
	 * 
	 * @param source
	 * @param dest
	 * @param destStart
	 * @return
	 */
	public static int copyInto(byte[] source, byte[] dest, int destStart){
		return copyInto(source, 0, source.length, dest, destStart);
	}
	
	/**
	 * 
	 * @param source
	 * @param dest
	 * @param destStart
	 * @return
	 */
	public static int copyInto(byte source, byte[] dest, int destStart){
		dest[destStart] = source;
		return 1;
	}

	/**
	 * 
	 * @param source
	 * @param srcStart
	 * @param length
	 * @return new array
	 */
	public static byte[] copyInto(byte[] source, int srcStart, int length){
		byte[] newArray = new byte[length];
		copyInto(source, srcStart, length, newArray, 0);
		return newArray;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static byte smallIntToByte(int value){
		return toBytes(value)[3];
	}

	public static int byteToInt(byte value){
		return toInt(new byte[] {0, 0, 0, value});
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static int toUnsigned(short value){
		int retValue = 0;
		for(int i=0; i<Short.BYTES; i++){
			retValue += ((value >> (8*i)) & 0xFF) << (8*i);
		}
		return retValue;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static long toUnsigned(int value){
		long retValue = 0;
		for(int i=0; i<Integer.BYTES; i++){
			int b = (value >> (8*i)) & 0xFF;
			long d = ((long)b) << (8*i);
			retValue += d;
		}
		return retValue;
	}
}
