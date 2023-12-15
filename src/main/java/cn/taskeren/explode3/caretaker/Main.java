/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker;

import lombok.val;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

class Main {

	/**
	 * Caretaker CLI main entry.
	 * <hr>
	 * <p>
	 * {@code caretaker <mode> <input>}
	 * <p>
	 * where mode can be "enc", "dec", or "sign".
	 *
	 * <ul>
	 * <li>"enc" is used to encrypt the normal file, which you should pass the path at the 2nd argument.</li>
	 * <li>"dec" is used to decrypt the encrypted file, which you should pass the path at the 2nd argument.</li>
	 * <li>"sign" is used to sign the request data, mostly used in the GraphQL requests,
	 * which you should pass the data string at the 2nd argument.</li>
	 * </ul>
	 *
	 * @param args the arguments
	 * @throws Exception if any error occurs
	 */
	public static void main(String[] args) throws Exception {
		prepareEnvironment();

		val mode = getOrNull(args, 0);
		val input = getOrNull(args, 1);

		if(mode == null) {
			System.err.println("Invalid Mode: null");
			return;
		}
		if(input == null) {
			System.err.println("Invalid Input: null");
			return;
		}

		Path inputPath = new File(input).toPath();
		switch(mode) {
			case "enc": {
				val bytes = Files.readAllBytes(inputPath);
				val encrypted = Caretaker.encrypt(bytes);
				Files.write(new File(input + ".rnx").toPath(), encrypted);
				break;
			}
			case "dec": {
				val bytes = Files.readAllBytes(inputPath);
				val decrypted = Caretaker.decrypt(bytes);
				val output = input.endsWith(".rnx") ? input.substring(0, input.length() - 4) : input + ".dec";
				Files.write(new File(output).toPath(), decrypted);
				break;
			}
			case "sign":
				System.out.println(Caretaker.signData(input));
				break;
			default:
				System.err.println("Invalid Mode: " + mode);
				break;
		}
	}

	private static void prepareEnvironment() {
		try {
			Caretaker.prepareDynamicLibrary();
		} catch(Exception e) {
			System.err.println("Failed to prepare caretaker.dll");
			throw new RuntimeException(e);
		}
	}

	// util
	private static String getOrNull(String[] args, int index) {
		return (index >= 0 && index < args.length) ? args[index] : null;
	}

}
