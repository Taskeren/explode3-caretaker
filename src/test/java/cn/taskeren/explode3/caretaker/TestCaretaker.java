/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCaretaker {

    @Test
    public void testIsWindows() {
        Assertions.assertEquals(SystemUtils.IS_OS_WINDOWS, Utils.isWindows());
    }

}
