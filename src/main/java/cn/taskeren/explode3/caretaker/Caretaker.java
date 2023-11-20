/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker;

import java.io.File;

/**
 * <b>Encrypt and Decrypt Helper</b> for Dynamite Assets from Tuner Games which are encrypted.
 * <p>
 * The assets are encrypted using <i>AES192/CBC/Pkcs7</i>, and KEY and IV are not public.
 * I'm not authorized to create this tool, nor to publish. So use this tool at your own risk.
 *
 * @author Taskeren
 * @see Caretaker#encrypt(byte[])
 * @see Caretaker#decrypt(byte[])
 * @see Caretaker#signData(byte[])
 * @see Caretaker#signData(String)
 */
public class Caretaker {

	/**
	 * Encrypt the rnx data.
	 *
	 * @param decrypted the bytes to encrypt
	 * @return the encrypted bytes
	 */
	public static byte[] encrypt(byte[] decrypted) {
		return CaretakerJNI.encrypt(decrypted);
	}

	/**
	 * Decrypt the encrypted rnx data.
	 *
	 * @param encrypted the encrypted bytes
	 * @return the decrypted bytes
	 */
	public static byte[] decrypt(byte[] encrypted) {
		return CaretakerJNI.decrypt(encrypted);
	}

	/**
	 * Used to sign the data with tuner's key in header 'X-VERIFY' of the http requests.
	 *
	 * @param data the bytes of the body data
	 * @return the signed data
	 */
	public static byte[] signData(byte[] data) {
		return CaretakerJNI.sign(data);
	}

	/**
	 * Used to sign the data with tuner's key in header 'X-VERIFY' of the http requests.
	 *
	 * @param data the body data
	 * @return the signed data
	 */
	public static String signData(String data) {
		return Utils.convertBitToString(signData(data.getBytes()));
	}

	/**
	 * Extract the caretaker.dll in the jar to the path.
	 *
	 * @throws Exception when any error occurs
	 */
	public static void prepareDynamicLibrary() throws Exception {
		/*
		 * For now, no linux dynamic library (.so) build of Caretaker is provided.
		 * If you need one, you can open an issue, and I maybe provide it as a feature.
		 */
		if(!Utils.isWindows()) {
			System.err.println("Unsupported Platform!");
			return;
		}

		File destination = new File("caretaker.dll");
		if(!destination.exists()) {
			Utils.extractDll(destination);
		}
	}

}
