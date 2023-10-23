/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker;

import java.io.FileOutputStream;
import java.io.IOException;

class Utils {

	/**
	 * Should be the same with BitConverter.ToString() method in C#.
	 *
	 * @param bits the data
	 * @return the string
	 */
	public static String convertBitToString(byte[] bits) {
		var array = new String[bits.length];
		for(int i = 0; i < bits.length; i++) {
			var unsigned = Byte.toUnsignedInt(bits[i]);
			var hex = "%02X".formatted(unsigned);
			array[i] = hex;
		}
		return String.join("-", array);
	}

	public static void extractDll() throws IOException {
		try(var in = Utils.class.getResourceAsStream("/caretaker.dll")) {
			try(var out = new FileOutputStream("caretaker.dll")) {
				if(in != null) {
					in.transferTo(out);
				}
			}
		}
	}

}
