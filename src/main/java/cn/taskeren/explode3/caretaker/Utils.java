/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker;

import lombok.val;
import lombok.var;

import java.io.*;

class Utils {

	/**
	 * Should be the same with BitConverter.ToString() method in C#.
	 *
	 * @param bits the data
	 * @return the string
	 */
	public static String convertBitToString(byte[] bits) {
		val array = new String[bits.length];
		for(int i = 0; i < bits.length; i++) {
			val unsigned = Byte.toUnsignedInt(bits[i]);
			val hex = String.format("%02X", unsigned);
			array[i] = hex;
		}
		return String.join("-", array);
	}

	public static void extractDll(File dest) throws IOException {
		try(val in = Utils.class.getResourceAsStream("/caretaker.dll")) {
			try(val out = new FileOutputStream(dest)) {
				if(in != null) {
					copy(in, out);
				}
			}
		}
	}

	public static boolean isWindows() {
		val osName = System.getProperty("os.name");
		return osName.contains("Windows");
	}

	private static void copy(InputStream i, OutputStream o) throws IOException {
		val buffer = new byte[8192];
		var bytes = i.read(buffer);
		while(bytes >= 0) {
			o.write(buffer, 0, bytes);
			bytes = i.read(buffer);
		}
	}

}
