/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker;

import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.val;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;

@Log
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
        val inputPathString = getOrNull(args, 1);

        if(mode == null) {
            System.err.println("Invalid Mode: null");
            System.err.println("Acceptable values: [enc, dec, sign]");
            return;
        }
        if(inputPathString == null) {
            System.err.println("Invalid Input: null");
            return;
        }

        if(mode.equals("sign")) {
            try {
                System.out.println(Caretaker.signData(inputPathString));
            } catch(Exception e) {
                log.log(Level.SEVERE, "Exception occurred while signing data", e);
            }
            return;
        }

        val path = new File(inputPathString).toPath();
        if(!Files.exists(path)) {
            System.err.println("Input path doesn't exist: " + path);
            return;
        }
        if(Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                @NonNull
                public FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) {
                    handleOperation(mode, file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            handleOperation(mode, path);
        }
    }

    /**
     * Run the selected operation to the target path.
     *
     * @param mode   the mode, acceptable inputs are {@code enc} and {@code dec}
     * @param target the target path
     */
    private static void handleOperation(String mode, Path target) {
        try {
            switch(mode) {
                case "enc": {
                    val bytes = Files.readAllBytes(target);
                    val encrypted = Caretaker.encrypt(bytes);
                    Files.write(target.getParent().resolve(target.getFileName() + ".rnx"), encrypted);
                    break;
                }
                case "dec": {
                    val bytes = Files.readAllBytes(target);
                    val decrypted = Caretaker.decrypt(bytes);
                    val targetFilename = target.getFileName().toString();
                    val output = targetFilename.endsWith(".rnx") ? targetFilename.substring(0, targetFilename.length() - 4) : targetFilename + ".dec";
                    Files.write(Paths.get(output), decrypted);
                    break;
                }
                default:
                    System.err.println("Invalid Mode: " + mode);
                    break;
            }
        } catch(Exception e) {
            log.log(Level.SEVERE, "Exception occurred while operating with: " + target, e);
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
