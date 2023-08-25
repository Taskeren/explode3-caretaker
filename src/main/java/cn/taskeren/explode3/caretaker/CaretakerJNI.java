/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reversed.
 */

package cn.taskeren.explode3.caretaker;

/**
 * Internal class for JNI linkage.
 * You should call these functions from {@link Caretaker}.
 *
 * @see Caretaker
 * @author Taskeren
 */
public class CaretakerJNI {

    static {
        System.loadLibrary("caretaker");
    }

    public static native byte[] encrypt(byte[] data);

    public static native byte[] decrypt(byte[] data);

}
