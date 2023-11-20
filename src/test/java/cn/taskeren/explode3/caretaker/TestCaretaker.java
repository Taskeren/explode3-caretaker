/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestCaretaker {

    private byte[] read(String path) throws IOException {
        var p = Path.of(path);
        if (Files.exists(p)) {
            return Files.readAllBytes(p);
        } else {
            try (var is = getClass().getClassLoader().getResourceAsStream(path)) {
                if (is == null)
                    throw new RuntimeException("Unable to find file in both local folder and in-jar resource! %s".formatted(path));

                // read bytes from input stream
                var buffer = new ByteArrayOutputStream();

                int n;
                byte[] data = new byte[16384];
                while ((n = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, n);
                }

                return buffer.toByteArray();
            }
        }
    }

    @Test
    public void testEncrypt() throws Exception {
        var dec = read("test.xml");
        var enc = read("test.xml.rnx");

        Assertions.assertArrayEquals(enc, Caretaker.encrypt(dec));
    }

    @Test
    public void testDecrypt() throws Exception {
        var dec = read("test.xml");
        var enc = read("test.xml.rnx");

        Assertions.assertArrayEquals(dec, Caretaker.decrypt(enc));
    }

    @Test
    public void testIsWindows() {
        Assertions.assertEquals(SystemUtils.IS_OS_WINDOWS, Utils.isWindows());
    }

}
