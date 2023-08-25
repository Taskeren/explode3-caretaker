/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reversed.
 */

package cn.taskeren.explode3.caretaker;

/**
 * <b>Encrypt and Decrypt Helper</b> for Dynamite Assets from Tuner Games which are encrypted.
 * <p>
 * The assets are encrypted using <i>AES192/CBC/Pkcs7</i>, and KEY and IV are not public.
 * I'm not authorized to create this tool, nor to publish. So use this tool at your own risk.
 *
 * @author Taskeren
 * @see Caretaker#encrypt(byte[])
 * @see Caretaker#decrypt(byte[])
 */
public class Caretaker {

    public static byte[] encrypt(byte[] decrypted) {
        return CaretakerJNI.encrypt(decrypted);
    }

    public static byte[] decrypt(byte[] encrypted) {
        return CaretakerJNI.decrypt(encrypted);
    }

}