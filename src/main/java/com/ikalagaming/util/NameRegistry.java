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
        registeredNames = new HashMap<>();
        mapLock = new ReentrantLock();
    }

    /** Removes all names and mappings from the registry. */
    public void clear() {
        mapLock.lock();
        try {
            registeredNames.values().forEach(IntegerTree::clear);
            registeredNames.clear();
        } finally {
            mapLock.unlock();
        }
    }

    /**
     * Removes all mappings to the given base name in the name registry.
     *
     * @param baseName the string that IDs are registered for
     */
    public void clear(final String baseName) {
        mapLock.lock();
        try {
            IntegerTree tree = registeredNames.remove(baseName);
            if (tree != null) {
                tree.clear();
            }
        } finally {
            mapLock.unlock();
        }
    }

    /**
     * A full name split into its base name and ID.
     *
     * @param name The base name.
     * @param id The ID.
     */
    private record FullName(String name, int id) {}

    /**
     * Split a full name, formed as base name, dash, then ID.
     *
     * @param fullName The full name.
     * @return The parts, or null if the name doesn't have that form.
     */
    private static FullName parse(final String fullName) {
        if (fullName == null) {
            return null;
        }
        final int dashPos = fullName.lastIndexOf('-');
        if (dashPos <= 0) {
            // No dash, or no base name before it
            return null;
        }
        try {
            return new FullName(
                    fullName.substring(0, dashPos),
                    Integer.parseInt(fullName.substring(dashPos + 1)));
        } catch (NumberFormatException badNumber) {
            return null;
        }
    }

    /**
     * Returns true if the base name exists in the registry and has the given ID registered to
     * itself.
     *
     * @param fullName the entire name of the object, including base name, dash, and ID
     * @return true if that name and ID pair exists, otherwise false
     */
    public boolean contains(final String fullName) {
        final FullName parts = parse(fullName);
        if (parts == null) {
            return false;
        }
        mapLock.lock();
        try {
            final IntegerTree tree = registeredNames.get(parts.name());
            return tree != null && tree.contains(parts.id());
        } finally {
            mapLock.unlock();
        }
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
        mapLock.lock();
        try {
            final IntegerTree tree = registeredNames.get(baseName);
            return tree == null ? 0 : tree.getSmallestUnusedInt();
        } finally {
            mapLock.unlock();
        }
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
        final int addition;
        mapLock.lock();
        try {
            final IntegerTree tree = registeredNames.computeIfAbsent(baseName, k -> new IntegerTree());
            addition = tree.getSmallestUnusedInt();
            tree.insert(addition);
        } catch (DuplicateEntry e) {
            // The smallest unused ID can't already be in use while we hold the lock
            throw new IllegalStateException(e);
        } finally {
            mapLock.unlock();
        }
        return baseName + "-" + addition;
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
        final FullName parts = parse(fullName);
        if (parts == null) {
            return false;
        }
        mapLock.lock();
        try {
            final IntegerTree tree = registeredNames.get(parts.name());
            if (tree == null || !tree.contains(parts.id())) {
                return false;
            }
            tree.remove(parts.id());
            return true;
        } finally {
            mapLock.unlock();
        }
    }
}
