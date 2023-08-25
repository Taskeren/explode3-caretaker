/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reversed.
 */

package cn.taskeren.explode3.caretaker;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws Exception {
        var mode = getOrNull(args, 0);
        var input = getOrNull(args, 1);

        if (mode == null) {
            System.out.println("Invalid Mode: null");
            return;
        }
        if (input == null) {
            System.out.println("Invalid Input: null");
            return;
        }

        Path inputPath = Path.of(input);
        if ("enc".equals(mode)) {
            var bytes = Files.readAllBytes(inputPath);
            var encrypted = Caretaker.encrypt(bytes);
            Files.write(Path.of(input + ".rnx"), encrypted);
        } else if ("dec".equals(mode)) {
            var bytes = Files.readAllBytes(inputPath);
            var decrypted = Caretaker.decrypt(bytes);
            var output = input.endsWith(".rnx") ? input.substring(0, input.length() - 4) : input + ".dec";
            Files.write(Path.of(output), decrypted);
        } else {
            System.out.println("Invalid Mode: " + mode);
        }
    }

    // util
    private static String getOrNull(String[] args, int index) {
        return (index >= 0 && index < args.length) ? args[index] : null;
    }


}
