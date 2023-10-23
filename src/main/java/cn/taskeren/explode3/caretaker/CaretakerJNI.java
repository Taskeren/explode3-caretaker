/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker;

/**
 * Internal class for JNI linkage.
 * You should call these functions from {@link Caretaker}.
 *
 * @author Taskeren
 * @see Caretaker
 */
class CaretakerJNI {

	static {
		System.loadLibrary("caretaker");
	}

	public static native byte[] encrypt(byte[] data);

	public static native byte[] decrypt(byte[] data);

	public static native byte[] sign(byte[] data);

}
