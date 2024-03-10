package com.ikalagaming.util;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Contains a list of Strings. Each string has a list of integers that represent ID numbers for that
 * string, all of which are unique. These numbers start at zero and increase. When names are
 * unregistered (that is, an index value is now no longer in use), it becomes available again for
 * other matching strings to use. The smallest unused integer will always be used next.
 *
 * <p>This allows for sets of objects which require unique names, such as Entities in a game or UI
 * elements that may be created at runtime but need to be referenced by their unique identifier.
 *
 * @author Ches Burks
 */
public class NameRegistry {
    private HashMap<String, IntegerTree> registeredNames;
    private ReentrantLock mapLock;

    /** Create a new, empty, name registry. */
    public NameRegistry() {
        this.registeredNames = new HashMap<>();
        this.mapLock = new ReentrantLock();
    }

    /** Removes all names and mappings from the registry. */
    public void clear() {
        this.mapLock.lock();
        try {
            for (String name : this.registeredNames.keySet()) {
                this.clear(name);
            }
        } finally {
            this.mapLock.unlock();
        }
    }

    /**
     * Removes all mappings to the given base name in the name registry.
     *
     * @param baseName the string that IDs are registered for
     */
    public void clear(final String baseName) {
        this.mapLock.lock();
        try {
            if (this.registeredNames.containsKey(baseName)) {
                this.registeredNames.get(baseName).clear();
                this.registeredNames.remove(baseName);
            }
        } finally {
            this.mapLock.unlock();
        }
    }

    /**
     * Returns true if the base name exists in the registry and has the given ID registered to
     * itself. If the name is
     *
     * @param fullName the entire name of the object, including base name, dash, and ID
     * @return true if that name and ID pair exists, otherwise false
     */
    public boolean contains(final String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return false; // invalid name
        }
        // when objects are deleted, unregister their id
        final int dashPos = fullName.lastIndexOf("-");
        if (dashPos == -1) {
            return false; // invalid name
        }
        int id = Integer.MIN_VALUE;
        try {
            id = Integer.parseInt(fullName.substring(dashPos));
        } catch (NumberFormatException badNumber) {
            return false;
        }
        String name = "";
        try {
            name = fullName.substring(0, dashPos);
        } catch (IndexOutOfBoundsException badName) {
            return false;
        }
        if (name.isEmpty()) {
            return false;
        }

        boolean doesContain = false;
        this.mapLock.lock();
        try {
            doesContain = this.registeredNames.get(name).contains(id);
        } finally {
            this.mapLock.unlock();
        }
        return doesContain;
    }

    /**
     * Returns the ID that will next be registered for a base name. This will be 0 of that name has
     * never been registered. Otherwise it will be the number as defined by {@link
     * IntegerTree#getSmallestUnusedInt()}.
     *
     * <p><b>Note: This is not guaranteed to be the next ID.</b> It is possible this ID might not
     * match the one created when registering in some cases, such as another object with the same
     * name being registered between the method calls.
     *
     * @param baseName the string that IDs are registered for
     * @return the next ID that will be registered for the given name
     */
    public int getNextID(final String baseName) {
        boolean doesContain = false;
        this.mapLock.lock();
        try {
            doesContain = this.registeredNames.containsKey(baseName);
        } finally {
            this.mapLock.unlock();
        }
        if (!doesContain) {
            return 0;
        }
        int smallest = 0;

        this.mapLock.lock();
        try {
            smallest = this.registeredNames.get(baseName).getSmallestUnusedInt();
        } finally {
            this.mapLock.unlock();
        }
        return smallest;
    }

    /**
     * Registers a name in the registry. The base name is the string that name collisions are
     * checked against. The function registers the next available name that can be created using the
     * base name, then returns the newly created name. The generated name is the base name, followed
     * immediately by a single dash ('-'), followed immediately by a unique integer ID for that base
     * name.
     *
     * <p>For example, "House" might be the name to register. The first time house is registered,
     * "House-0" is registered/returned. The next time register is called for "House", while
     * "House-0" has not been unregisterd, "House-1" will be returned. Say "House-0" were now
     * unregistered, but "House-1" remained. The next call using "House" as the base name will
     * register "House-0" instead of "House-3", because 0 is now free. If register is then called
     * using "Car", "Car-0" will be returned as all strings use their own pool of ID's.
     *
     * @param baseName The base string to create and register an ID for
     * @return the newly created unique name for the object
     */
    public String registerName(final String baseName) {
        StringBuilder ret = new StringBuilder(baseName);
        IntegerTree tree;
        int addition;

        boolean doesContain = false;

        this.mapLock.lock();
        try {
            doesContain = this.registeredNames.containsKey(baseName);
        } finally {
            this.mapLock.unlock();
        }
        if (doesContain) {
            this.mapLock.lock();
            try {
                tree = this.registeredNames.get(baseName);
                addition = tree.getSmallestUnusedInt();
            } finally {
                this.mapLock.unlock();
            }
        } else {
            tree = new IntegerTree();
            addition = 0;
            this.mapLock.lock();
            try {
                this.registeredNames.put(baseName, tree);

            } finally {
                this.mapLock.unlock();
            }
        }

        this.mapLock.lock();
        try {
            tree.insert(addition);
        } catch (DuplicateEntry e) {
            e.printStackTrace();
        } finally {
            this.mapLock.unlock();
        }

        ret.append("-").append(addition);
        return ret.toString();
    }

    /**
     * Unregisters a name from the registry. The name should follow the format that matches what is
     * created by {@link NameRegistry#registerName(String)} . For reference that is a string that is
     * formed as follows: <code>Base name (String)+'-'+ID (Integer)</code>. If the name is not
     * registered, or has an invalid format for any reason, returns false. Returns true if the name
     * was unregistered correctly.
     *
     * @param fullName the entire name of the object, including base name, dash, and ID
     * @return true if the name was unregistered, otherwise false
     */
    public boolean unregisterName(final String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return false; // invalid name
        }
        // when objects are deleted, unregister their id
        final int dashPos = fullName.lastIndexOf("-");
        if (dashPos == -1) {
            return false; // invalid name
        }
        int id = Integer.MIN_VALUE;
        try {
            id = Integer.parseInt(fullName.substring(dashPos));
        } catch (NumberFormatException badNumber) {
            return false;
        }
        String name = "";
        try {
            name = fullName.substring(0, dashPos);
        } catch (IndexOutOfBoundsException badName) {
            return false;
        }
        if (name.isEmpty()) {
            return false;
        }

        this.mapLock.lock();
        try {
            IntegerTree nameTree;
            nameTree = this.registeredNames.get(name);
            if (nameTree != null) {
                nameTree.remove(id);
            }
        } finally {
            this.mapLock.unlock();
        }

        return true;
    }
}
