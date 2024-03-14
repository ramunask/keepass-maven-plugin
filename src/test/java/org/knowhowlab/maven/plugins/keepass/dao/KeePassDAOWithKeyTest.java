/*
 * Copyright (c) 2010-2016 Dmytro Pishchukhin (http://knowhowlab.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.knowhowlab.maven.plugins.keepass.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author dpishchukhin.
 */
public class KeePassDAOWithKeyTest {
    private File dbFile;
    private File dbOnlyWithKeyFile;
    private File keyFile;

    @BeforeEach
    public void setUp() {
        dbFile = new File("./src/test/resources/test-with-key.kdbx");
        dbOnlyWithKeyFile = new File("./src/test/resources/test-only-with-key.kdbx");
        keyFile = new File("./src/test/resources/keyfile.key");
    }

    @Test
    public void testOpen_invalidPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			new KeePassDAO(dbFile).open("testpass");
		});
    }

    @Test
    public void testOpen_invalidPassword2() {
		assertThrows(IllegalArgumentException.class, () -> {
			new KeePassDAO(dbOnlyWithKeyFile).open("testpass");
		});
    }

    @Test
    public void testOpen_invalidKey() {
		assertThrows(IllegalArgumentException.class, () -> {
			new KeePassDAO(dbFile).open(new File("wrong-key-file.key"));
		});
    }

    @Test
    public void testOpen() {
        assertNotNull(new KeePassDAO(dbFile).open("test123", keyFile));
    }

    @Test
    public void testOpen2() {
        assertNotNull(new KeePassDAO(dbOnlyWithKeyFile).open(keyFile));
    }
}