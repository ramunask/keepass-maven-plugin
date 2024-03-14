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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author dpishchukhin.
 */
public class KeePassDAOTest {
	private File dbFile;

	@BeforeEach
	public void setUp() throws Exception {
		dbFile = new File("./src/test/resources/testdb.kdbx");
	}

	@Test
	public void testKeePassDAO_null() {
		assertThrows(IllegalArgumentException.class, () -> {
			new KeePassDAO(null);
		});
	}

	@Test
	public void testKeePassDAO_unknownFile() {
		assertThrows(IllegalArgumentException.class, () -> {
			new KeePassDAO(new File("/aaa/bbb/aaa"));
		});

	}

	@Test
	public void testOpen_null() {
		assertThrows(IllegalArgumentException.class, () -> {
			new KeePassDAO(dbFile).open((String) null);
		});
	}

    @Test
    public void testOpen_invalidPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			new KeePassDAO(dbFile).open("fakepass");
		});
    }

    @Test
    public void testOpen() {
    	assertNotNull(new KeePassDAO(dbFile).open("testpass"));
    }

    // find entry by UUID
    @Test
    public void testFindEntryByUUID() {
        UUID entryUuid = UUID.fromString("878bc61b-9a16-259c-4765-64d1b82945f3");

        KeePassEntry entry = new KeePassDAO(dbFile)
                .open("testpass")
                .getEntry(entryUuid);
        
        assertNotNull(entry);
        assertEquals(entry.getUuid(), entryUuid);
        assertEquals(entry.getTitle(), "Deployment");
        assertEquals(entry.getUsername(), "test-deploy");
        assertEquals(entry.getPassword(), "testtest");
    }

    @Test
    public void testFindEntryByUUID_invalidValue() {
		assertThrows(IllegalArgumentException.class, () -> {
	        new KeePassDAO(dbFile)
	                .open("testpass")
	                .getEntry(UUID.fromString("01234567-0123-0123-0123-012345678990"));
		});
    }

    // find entries by title
    @Test
    public void testFindEntryByTitle() {
        List<KeePassEntry> entries = new KeePassDAO(dbFile)
                .open("testpass")
                .getEntriesByTitle("Deployment");

        assertNotNull(entries);
        assertEquals(entries.size(), 3);
    }

    @Test
    public void testFindEntryByTitle_invalidValue() {
		assertThrows(IllegalArgumentException.class, () -> {
	        new KeePassDAO(dbFile)
	                .open("testpass")
	                .getEntriesByTitle("Deployment123");
		});
    }

    // find entries by regex
    @Test
    public void testFindEntryByTitleRegex() {
        List<KeePassEntry> entries = new KeePassDAO(dbFile)
                .open("testpass")
                .getEntriesByTitleRegex("Dep[l|o]{2}yme.*");

        assertNotNull(entries);
        assertEquals(entries.size(), 3);
    }

    @Test
    public void testFindEntryByTitleRegex_invalidValue() {
		assertThrows(IllegalArgumentException.class, () -> {
	        new KeePassDAO(dbFile)
	                .open("testpass")
	                .getEntriesByTitleRegex("Deployme[0-9]{2}");
		});
    }

    // find group by UUID
    @Test
    public void testFindGroupByUUID() {
        UUID groupUuid = UUID.fromString("8b7e6300-b873-d32b-8c20-811b6de5f2ac");

        KeePassGroup group = new KeePassDAO(dbFile)
                .open("testpass")
                .getGroup(groupUuid);

        assertNotNull(group);
        assertEquals(group.getUuid(), groupUuid);
        assertEquals(group.getName(), "development");
        assertNotNull(group.getEntries());
        assertEquals(group.getEntries().size(), 1);
    }

    @Test
    public void testFindGroupByUUID_invalidValue() {
		assertThrows(IllegalArgumentException.class, () -> {
	        new KeePassDAO(dbFile)
	                .open("testpass")
	                .getGroup(UUID.fromString("01234567-0123-0123-0123-012345678990"));
		});
    }

    // find group by path
    @Test
    public void testFindGroupByPath() {
        UUID groupUuid = UUID.fromString("8b7e6300-b873-d32b-8c20-811b6de5f2ac");

        List<KeePassGroup> groups = new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByPath("/Root/server/development");

        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        assertEquals(groups.get(0).getUuid(), groupUuid);
        assertEquals(groups.get(0).getName(), "development");
        assertNotNull(groups.get(0).getEntries());
        assertEquals(groups.get(0).getEntries().size(), 1);
    }

    @Test
    public void testFindGroupByPath_invalidValue() {
		assertThrows(IllegalArgumentException.class, () -> {
	        new KeePassDAO(dbFile)
	                .open("testpass")
	                .getGroupsByPath("/Root/server/staging");
		});
    }

    // find groups by name
    @Test
    public void testFindGroupByName() {
        List<KeePassGroup> groups = new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByName("test");

        assertNotNull(groups);
        assertEquals(groups.size(), 2);
    }

    @Test
    public void testFindGroupByName_invalidValue() {
		assertThrows(IllegalArgumentException.class, () -> {
	        new KeePassDAO(dbFile)
	                .open("testpass")
	                .getGroupsByName("test123");
		});
    }

    // find groups by name regex
    @Test
    public void testFindGroupByNameRegex() {
        List<KeePassGroup> groups = new KeePassDAO(dbFile)
                .open("testpass")
                .getGroupsByNameRegex("[t|e|s]{3}t");

        assertNotNull(groups);
        assertEquals(groups.size(), 2);
    }

    @Test
    public void testFindGroupByNameRegex_invalidValue() {
		assertThrows(IllegalArgumentException.class, () -> {
	        new KeePassDAO(dbFile)
	                .open("testpass")
	                .getGroupsByNameRegex("[t|e|s]{3}[0-9]?");
		});
    }

    // find groups by name regex
    @Test
    public void testFindGroupByPath_and_EntryProperties() {
        KeePassDAO dao = new KeePassDAO(dbFile)
                .open("testpass");

        List<KeePassGroup> groups = dao
                .getGroupsByPath("/Root/server/test");

        assertNotNull(groups);
        assertEquals(groups.size(), 1);

        List<KeePassEntry> entries = dao.getEntriesByTitle(groups.get(0), "Deployment");
        assertNotNull(entries);
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getPropertyByName("check").getValue(), "true");
    }
}