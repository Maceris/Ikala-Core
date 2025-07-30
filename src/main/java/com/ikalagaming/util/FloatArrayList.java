package com.ikalagaming.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class FloatArrayList extends AbstractList<Float>
        implements List<Float>, RandomAccess, Cloneable, java.io.Serializable {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

    /** Shared instance to use for empty lists. */
    private static final float[] EMPTY_LIST = {};

    /** Shared instance to use for empty lists that have never had anything added to them. */
    private static final float[] DEFAULT_EMPTY_LIST = {};

    private transient float[] contents;
    private int size;

    /** Create an empty list with a capacity of 16. */
    public FloatArrayList() {
        this.contents = DEFAULT_EMPTY_LIST;
    }

    /**
     * Create a list with a specific initial capacity.
     *
     * @param initialCapacity The initial capacity, which must be >= 0.
     * @throws IllegalArgumentException if the initial capacity is negative
     */
    public FloatArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must not be negative");
        } else if (initialCapacity == 0) {
            this.contents = EMPTY_LIST;
        } else {
            this.contents = new float[initialCapacity];
        }
    }

    public FloatArrayList(@NonNull Collection<? extends Number> other) {
        Object[] array = other.toArray();
        this.size = array.length;

        if (size == 0) {
            this.contents = EMPTY_LIST;
        } else {
            this.contents = new float[array.length];
            for (int i = 0; i < array.length; ++i) {
                this.contents[i] = (float) array[i];
            }
        }
    }

    public FloatArrayList(@NonNull FloatArrayList other) {
        this.size = other.size;
        this.contents = new float[other.contents.length];
        System.arraycopy(other.contents, 0, this.contents, 0, other.contents.length);
    }

    public void trimToSize() {
        ++modCount;
        if (size < contents.length) {
            if (size == 0) {
                contents = EMPTY_LIST;
            } else {
                contents = Arrays.copyOf(contents, size);
            }
        }
    }

    private static int newLength(int oldLength, int minGrowth, int preferredGrowth) {
        final int preferredLength = oldLength + Math.max(minGrowth, preferredGrowth);
        if (preferredLength > 0 && preferredLength <= SOFT_MAX_ARRAY_LENGTH) {
            return preferredLength;
        } else {
            // Cold path is in a separate method
            return hugeLength(oldLength, minGrowth);
        }
    }

    private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) {
            // It overflowed
            throw new OutOfMemoryError(
                    String.format(
                            "Required array length %d + %d is too large", oldLength, minGrowth));
        }
        return Math.max(minLength, SOFT_MAX_ARRAY_LENGTH);
    }

    private float[] grow(int minCapacity) {
        int oldCapacity = contents.length;
        if (oldCapacity > 0 || contents != DEFAULT_EMPTY_LIST) {
            int newCapacity = newLength(oldCapacity, minCapacity - oldCapacity, oldCapacity >> 1);
            contents = Arrays.copyOf(contents, newCapacity);
        } else {
            contents = new float[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
        return contents;
    }

    private float[] grow() {
        return grow(size + 1);
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o) {
        return indexOfWithinRange(o, 0, size);
    }

    private int indexOfWithinRange(Object o, int start, int end) {
        float[] localContents = contents;
        if (o == null) {
            return -1;
        }
        for (int i = start; i < end; ++i) {
            if (o.equals(localContents[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOfWithinRange(o, 0, size);
    }

    private int lastIndexOfWithinRange(Object o, int start, int end) {
        float[] localContents = contents;
        if (o == null) {
            return -1;
        }
        for (int i = end - 1; i >= start; --i) {
            if (o.equals(localContents[i])) {
                return i;
            }
        }
        return -1;
    }

    public Object clone() { // NOSONAR
        // ArrayList and kin have this, we are trying to be as functionally close as possible
        try {
            FloatArrayList other = (FloatArrayList) super.clone();
            other.contents = Arrays.copyOf(contents, size);
            other.modCount = 0;
            return other;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public Object @NonNull [] toArray() {
        Float[] result = new Float[size];
        for (int i = 0; i < size; ++i) {
            // They need to be boxed.
            result[i] = contents[i]; // NOSONAR
        }
        return result;
    }

    /**
     * Unsupported.
     *
     * @throws UnsupportedOperationException when called.
     */
    @Override
    public <T> T @NonNull [] toArray(T @NonNull [] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Float get(int index) {
        Objects.checkIndex(index, size);
        return contents[index];
    }

    public float getFloat(int index) {
        Objects.checkIndex(index, size);
        return contents[index];
    }

    @Override
    public Float set(int index, Float element) {
        Objects.checkIndex(index, size);
        float oldValue = contents[index];
        contents[index] = element;
        return oldValue;
    }

    public float setFloat(int index, float element) {
        Objects.checkIndex(index, size);
        float oldValue = contents[index];
        contents[index] = element;
        return oldValue;
    }

    private void add(Float e, float[] data, int s) {
        if (s == data.length) {
            data = grow();
        }
        data[s] = e;
        size = s + 1;
    }

    @Override
    public boolean add(Float e) {
        ++modCount;
        add(e, contents, size);
        return true;
    }

    public boolean addFloat(float e) {
        ++modCount;
        add(e, contents, size);
        return true;
    }

    /**
     * Push a float onto the stack, equivalent to adding to the end of the list.
     *
     * @param e The float to add.
     * @return If we were successful.
     */
    public boolean push(float e) {
        ++modCount;
        add(e, contents, size);
        return true;
    }

    /**
     * Returns the top of the stack (last element in the list).
     *
     * @return The last element in the list.
     * @see #isEmpty()
     * @see #pop()
     * @throws EmptyStackException if the list is empty.
     */
    public float peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return contents[size - 1];
    }

    /**
     * Remove and return the top item of the stack (last element in the list).
     *
     * @return The last element of the list.
     * @see #isEmpty()
     * @see #peek()
     * @throws EmptyStackException if the list is empty.
     */
    public float pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        ++modCount;
        final int newSize = size - 1;
        float value = contents[newSize];
        size = newSize;
        return value;
    }

    @Override
    public void add(int index, Float element) {
        Objects.checkIndex(index, size - 1);
        ++modCount;
        final int localSize = size;
        float[] localContents = this.contents;

        if (localSize == localContents.length) {
            localContents = grow();
        }
        System.arraycopy(localContents, index, localContents, index + 1, localSize - index);
        localContents[index] = element;
        size = localSize + 1;
    }

    public void addFloat(int index, float element) {
        Objects.checkIndex(index, size - 1);
        ++modCount;
        final int localSize = size;
        float[] localContents = this.contents;
        if (localSize == localContents.length) {
            localContents = grow();
        }
        System.arraycopy(localContents, index, localContents, index + 1, localSize - index);
        localContents[index] = element;
        size = localSize + 1;
    }

    private void justRemove(float[] localContents, int index) {
        ++modCount;
        final int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(localContents, index + 1, localContents, index, newSize - index);
        }
        size = newSize;
    }

    @Override
    public Float remove(int index) {
        Objects.checkIndex(index, size);
        final float[] localContents = contents;
        final float oldValue = localContents[index];
        justRemove(localContents, index);
        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FloatArrayList otherList)) {
            return false;
        }

        final int localModCount = otherList.modCount;
        final int otherModCount = otherList.modCount;
        final int localSize = size;
        final int otherSize = otherList.size;

        boolean equal = localSize == otherSize;
        if (equal) {
            final float[] localContents = contents;
            final float[] otherContents = otherList.contents;
            if (localSize > localContents.length || localSize > otherContents.length) {
                throw new ConcurrentModificationException();
            }
            for (int i = 0; i < size; ++i) {
                if (localContents[i] != otherContents[i]) {
                    equal = false;
                    break;
                }
            }
        }
        checkForConcurrentModification(localModCount);
        otherList.checkForConcurrentModification(otherModCount);
        return equal;
    }

    private void checkForConcurrentModification(final int expectedModCount) {
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    @Override
    public int hashCode() {
        final int initialModCount = modCount;
        int hash = hashCodeOfSlice(0, size);
        checkForConcurrentModification(initialModCount);
        return hash;
    }

    private int hashCodeOfSlice(int start, int end) {
        final float[] localContents = contents;
        if (end > localContents.length) {
            throw new ConcurrentModificationException();
        }
        int hashCode = 1;
        for (int i = start; i < end; ++i) {
            hashCode = hashCode * 31 + Float.hashCode(localContents[i]);
        }
        return hashCode;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Float other)) {
            return false;
        }
        final float[] localContents = contents;
        final int localSize = size;
        final float otherCasted = other;

        for (int i = 0; i < localSize; ++i) {
            if (localContents[i] == otherCasted) {
                justRemove(localContents, i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        ++modCount;
        size = 0;
    }

    @Override
    public boolean addAll(Collection<? extends Float> c) {
        Object[] array = c.toArray();
        ++modCount;
        int newItemCount = array.length;
        if (newItemCount == 0) {
            return false;
        }
        float[] localContents = contents;
        final int localSize = size;
        if (newItemCount > localContents.length - localSize) {
            localContents = grow(localSize + newItemCount);
        }
        for (int i = 0; i < newItemCount; ++i) {
            // Java, why can't you be normal?
            localContents[localSize + i] = (Float) array[i];
        }
        size = localSize + 1;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Float> c) {
        Objects.checkIndex(index, size - 1);

        Object[] array = c.toArray();
        ++modCount;
        int newItemCount = array.length;
        if (newItemCount == 0) {
            return false;
        }
        float[] localContents = contents;
        final int localSize = size;
        if (newItemCount > localContents.length - localSize) {
            localContents = grow(localSize + newItemCount);
        }
        final int movedItemCount = localSize - index;
        if (movedItemCount > 0) {
            System.arraycopy(
                    localContents, index, localContents, index + newItemCount, movedItemCount);
        }
        for (int i = 0; i < newItemCount; ++i) {
            localContents[index + i] = (Float) array[i];
        }
        size = localSize + 1;
        return true;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return removeCollection(c, false, 0, size);
    }

    private boolean removeCollection(
            @NonNull Collection<?> c, final boolean invert, final int start, final int end) {
        final float[] localContents = contents;

        // Go ahead and skip over the initial list of items that we are going to keep
        int position;
        for (position = start; ; ++position) {
            if (position == end) {
                return false;
            }
            if (c.contains(localContents[position]) == invert) {
                break;
            }
        }
        int removed = position;
        ++position;
        try {
            float value;
            for (; position < end; position++) {
                value = localContents[position];
                if (c.contains(value) != invert) {
                    localContents[removed++] = value;
                }
            }
        } catch (Exception e) {
            System.arraycopy(localContents, position, localContents, removed, end - position);
            removed += end - position;
            throw e;
        } finally {
            modCount += end - removed;
            System.arraycopy(localContents, end, localContents, removed, size - end);
        }
        return true;
    }

    @Serial
    private void writeObject(ObjectOutputStream s) throws IOException {
        int expectedModCount = modCount;
        s.defaultWriteObject();

        s.writeInt(size); // To work like FloatArrayList does

        for (int i = 0; i < size; ++i) {
            s.writeFloat(contents[i]);
        }

        checkForConcurrentModification(expectedModCount);
    }

    @Serial
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        // Ignore size, like FloatArrayList would
        s.readInt();

        if (size > 0) {
            float[] newContents = new float[size];
            for (int i = 0; i < size; ++i) {
                newContents[i] = s.readFloat();
            }
            contents = newContents;
        } else if (size == 0) {
            contents = EMPTY_LIST;
        } else {
            throw new InvalidObjectException("Invalid size: " + size);
        }
    }

    @Override
    public @NonNull ListIterator<Float> listIterator(int index) {
        Objects.checkIndex(index, size - 1);
        return new ListItr(index);
    }

    @Override
    public @NonNull ListIterator<Float> listIterator() {
        return new ListItr(0);
    }

    @Override
    public @NonNull Iterator<Float> iterator() {
        return new Itr();
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    private class Itr implements Iterator<Float> {
        int cursor;
        int lastReturned = -1;
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size;
        }

        public Float next() {
            checkForConcurrentModification();
            int i = cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            }
            float[] localContents = contents;
            if (i >= localContents.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i + 1;
            lastReturned = i;
            return localContents[i];
        }

        @Override
        public void remove() {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            checkForConcurrentModification();
            try {
                FloatArrayList.this.remove(lastReturned);
                cursor = lastReturned;
                lastReturned = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(@NonNull Consumer<? super Float> action) {
            final int localSize = size;
            int i = cursor;
            if (i < localSize) {
                float[] localContents = contents;
                if (i >= localContents.length) {
                    throw new ConcurrentModificationException();
                }
                for (; i < size && modCount == expectedModCount; ++i) {
                    action.accept(localContents[i]);
                }
                cursor = i;
                lastReturned = i - 1;
                checkForConcurrentModification();
            }
        }

        void checkForConcurrentModification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class ListItr extends Itr implements ListIterator<Float> {
        ListItr(int index) {
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public Float previous() {
            super.checkForConcurrentModification();
            int i = cursor - 1;
            if (i < 0) {
                throw new NoSuchElementException();
            }
            float[] localContents = contents;
            if (i >= localContents.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i;
            lastReturned = i;
            return localContents[i];
        }

        public void set(Float e) {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            super.checkForConcurrentModification();
            try {
                FloatArrayList.this.setFloat(lastReturned, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(Float e) {
            super.checkForConcurrentModification();

            try {
                int i = cursor;
                FloatArrayList.this.addFloat(i, e);
                cursor = i + 1;
                lastReturned = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public @NonNull List<Float> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, fromIndex, toIndex);
    }

    private static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("from index must be >=0, but is " + fromIndex);
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex must be <= size, but is " + toIndex);
        }
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException(
                    "fromIndex (" + fromIndex + ") > toIndex (" + toIndex + ")");
        }
    }

    private static class SubList extends AbstractList<Float> implements RandomAccess {
        private final FloatArrayList root;
        private final SubList parent;
        private final int offset;
        private int size;

        public SubList(FloatArrayList root, int from, int to) {
            this.root = root;
            this.parent = null;
            this.offset = from;
            this.size = to - from;
            this.modCount = root.modCount;
        }

        private SubList(SubList parent, int from, int to) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + from;
            this.size = to - from;
            this.modCount = parent.modCount;
        }

        @Override
        public Float set(int index, Float element) {
            Objects.checkIndex(index, size);
            checkForConcurrentModification();
            float oldValue = root.contents[offset + index];
            root.contents[offset + index] = element;
            return oldValue;
        }

        @Override
        public Float get(int index) {
            Objects.checkIndex(index, size);
            checkForConcurrentModification();
            return root.contents[offset + index];
        }

        @Override
        public int size() {
            checkForConcurrentModification();
            return size;
        }

        @Override
        public void add(int index, Float element) {
            Objects.checkIndex(index, size);
            checkForConcurrentModification();
            root.add(offset + index, element);
            updateSizeAndModCount(1);
        }

        @Override
        public Float remove(int index) {
            Objects.checkIndex(index, size);
            checkForConcurrentModification();
            Float result = root.remove(offset + index);
            updateSizeAndModCount(-1);
            return result;
        }

        @Override
        protected void removeRange(int from, int to) {
            checkForConcurrentModification();
            root.removeRange(offset + from, offset + to);
            updateSizeAndModCount(from - to);
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends Float> c) {
            return addAll(this.size, c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends Float> c) {
            Objects.checkIndex(index, size - 1);
            final int toAdd = c.size();
            if (toAdd == 0) {
                return false;
            }
            checkForConcurrentModification();
            root.addAll(offset + index, c);
            updateSizeAndModCount(toAdd);
            return true;
        }

        @Override
        public void replaceAll(UnaryOperator<Float> operator) {
            root.replaceAllRange(operator, offset, offset + size);
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return removeCollection(c, false);
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return removeCollection(c, true);
        }

        private boolean removeCollection(Collection<?> c, boolean invert) {
            checkForConcurrentModification();
            int oldSize = root.size;
            boolean modified = root.removeCollection(c, invert, offset, offset + size);
            if (modified) {
                updateSizeAndModCount(root.size - oldSize);
            }
            return modified;
        }

        @Override
        public boolean removeIf(Predicate<? super Float> filter) {
            checkForConcurrentModification();
            int oldSize = root.size;
            boolean modified = root.removeIf(filter, offset, offset + size);
            if (modified) {
                updateSizeAndModCount(root.size - oldSize);
            }
            return modified;
        }

        @Override
        public Object @NonNull [] toArray() {
            checkForConcurrentModification();
            Float[] result = new Float[size];
            for (int i = 0; i < size; ++i) {
                result[i] = root.contents[offset + i];
            }
            return result;
        }

        @Override
        public <T> T @NonNull [] toArray(T @NonNull [] a) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof List<?> otherList)) {
                return false;
            }

            final int localSize = size;
            final int otherSize = otherList.size();

            boolean equal = localSize == otherSize;
            if (equal) {
                final float[] localContents = root.contents;
                if (localSize > localContents.length) {
                    throw new ConcurrentModificationException();
                }
                for (int i = 0; i < size; ++i) {
                    if (!Objects.equals(localContents[offset + i], otherList.get(i))) {
                        equal = false;
                        break;
                    }
                }
            }

            checkForConcurrentModification();
            return equal;
        }

        @Override
        public int hashCode() {
            int hash = root.hashCodeOfSlice(offset, offset + size);
            checkForConcurrentModification();
            return hash;
        }

        @Override
        public int indexOf(Object o) {
            int index = root.indexOfWithinRange(o, offset, offset + size);
            checkForConcurrentModification();
            if (index < 0) {
                return -1;
            }
            return index - offset;
        }

        @Override
        public int lastIndexOf(Object o) {
            int index = root.lastIndexOfWithinRange(o, offset, offset + size);
            checkForConcurrentModification();
            if (index < 0) {
                return -1;
            }
            return index - offset;
        }

        @Override
        public boolean contains(Object o) {
            return indexOf(o) >= 0;
        }

        @Override
        public @NonNull Iterator<Float> iterator() {
            return super.listIterator();
        }

        @Override
        public @NonNull ListIterator<Float> listIterator(int index) {
            Objects.checkIndex(index, size - 1);
            checkForConcurrentModification();

            return new ListIterator<>() {
                int cursor = index;
                int lastReturned = -1;
                int expectedModCount = modCount;

                public boolean hasNext() {
                    return cursor != size;
                }

                public Float next() {
                    checkForConcurrentModification();
                    int i = cursor;
                    if (i >= size) {
                        throw new NoSuchElementException();
                    }
                    float[] contents = root.contents;
                    if (offset + i >= contents.length) {
                        throw new ConcurrentModificationException();
                    }
                    cursor = i + 1;
                    lastReturned = i;
                    return contents[offset + i];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                public Float previous() {
                    checkForConcurrentModification();
                    int i = cursor - 1;
                    if (i < 0) {
                        throw new NoSuchElementException();
                    }
                    float[] contents = root.contents;
                    if (offset + i >= contents.length) {
                        throw new ConcurrentModificationException();
                    }
                    cursor = i;
                    lastReturned = i;
                    return contents[offset + i];
                }

                @Override
                public void forEachRemaining(@NonNull Consumer<? super Float> action) {
                    final int localSize = size;
                    int i = cursor;
                    if (i < localSize) {
                        final float[] localContents = root.contents;
                        if (offset + i >= localContents.length) {
                            throw new ConcurrentModificationException();
                        }
                        for (; i < localSize && root.modCount == expectedModCount; i++) {
                            action.accept(localContents[offset + i]);
                        }
                        cursor = i;
                        lastReturned = i - 1;
                        checkForConcurrentModification();
                    }
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastReturned < 0) {
                        throw new IllegalStateException();
                    }
                    checkForConcurrentModification();

                    try {
                        FloatArrayList.SubList.this.remove(lastReturned);
                        cursor = lastReturned;
                        lastReturned = -1;
                        expectedModCount = modCount;
                    } catch (IndexOutOfBoundsException e) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(Float e) {
                    if (lastReturned < 0) {
                        throw new IllegalStateException();
                    }
                    checkForConcurrentModification();

                    try {
                        root.set(offset + lastReturned, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(Float e) {
                    checkForConcurrentModification();

                    try {
                        int i = cursor;
                        FloatArrayList.SubList.this.add(i, e);
                        cursor = i + 1;
                        lastReturned = -1;
                        expectedModCount = modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }
            };
        }

        @Override
        public @NonNull List<Float> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new FloatArrayList.SubList(this, fromIndex, toIndex);
        }

        @Override
        public Spliterator<Float> spliterator() {
            checkForConcurrentModification();

            return new Spliterator<>() {
                private int index = offset;
                private int fence = -1;
                private int expectedModCount;

                private int getFence() {
                    int hi;
                    if ((hi = fence) < 0) {
                        expectedModCount = modCount;
                        hi = fence = offset + size;
                    }
                    return hi;
                }

                public Spliterator<Float> trySplit() {
                    int hi = getFence();
                    int lo = index;
                    int mid = (lo + hi) >>> 1;
                    if (lo >= mid) {
                        return null;
                    }
                    index = mid;
                    return root.new FloatArrayListSpliterator(lo, mid, expectedModCount);
                }

                public boolean tryAdvance(@NonNull Consumer<? super Float> action) {
                    int hi = getFence();
                    int i = index;
                    if (i < hi) {
                        index = i + 1;
                        float e = root.contents[i];
                        action.accept(e);
                        checkForConcurrentModification();
                        return true;
                    }
                    return false;
                }

                @Override
                public void forEachRemaining(@NonNull Consumer<? super Float> action) {
                    int i;
                    int hi;
                    int mc;
                    FloatArrayList localRoot = root;
                    float[] localContents = localRoot.contents;
                    if (localContents != null) {
                        hi = fence;
                        if (hi < 0) {
                            mc = modCount;
                            hi = offset + size;
                        } else {
                            mc = expectedModCount;
                        }
                        if ((i = index) >= 0 && (index = hi) <= localContents.length) {
                            for (; i < hi; ++i) {
                                float e = localContents[i];
                                action.accept(e);
                            }
                            if (localRoot.modCount == mc) {
                                return;
                            }
                        }
                    }
                    throw new ConcurrentModificationException();
                }

                public long estimateSize() {
                    return (long) getFence() - index;
                }

                public int characteristics() {
                    return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
                }
            };
        }

        private void updateSizeAndModCount(int sizeIncrease) {
            SubList current = this;
            do {
                current.size += sizeIncrease;
                current.modCount = root.modCount;
                current = current.parent;
            } while (current != null);
        }

        private void checkForConcurrentModification() {
            if (modCount != root.modCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @AllArgsConstructor
    final class FloatArrayListSpliterator implements Spliterator<Float> {
        private int index;
        private int fence;
        private int expectedModCount;

        private int getFence() {
            int hi = fence;
            if (hi < 0) {
                expectedModCount = modCount;
                hi = fence = size;
            }
            return hi;
        }

        public FloatArrayListSpliterator trySplit() {
            int hi = getFence();
            int lo = index;
            int mid = (lo + hi) >>> 1;
            if (lo >= mid) {
                return null;
            }
            index = mid;
            return new FloatArrayListSpliterator(lo, mid, expectedModCount);
        }

        public boolean tryAdvance(@NonNull Consumer<? super Float> action) {
            int hi = getFence();
            int i = index;
            if (i < hi) {
                index = i + 1;
                float e = contents[i];
                action.accept(e);
                checkForConcurrentModification(expectedModCount);
                return true;
            }
            return false;
        }

        @Override
        public void forEachRemaining(@NonNull Consumer<? super Float> action) {
            int i;
            int hi;
            int mc;
            float[] localContents = contents;

            if (localContents != null) {
                hi = fence;
                if (hi < 0) {
                    mc = modCount;
                    hi = size;
                } else {
                    mc = expectedModCount;
                }
                if ((i = index) >= 0 && (index = hi) <= localContents.length) {
                    for (; i < hi; ++i) {
                        float e = localContents[i];
                        action.accept(e);
                    }
                    if (modCount == mc) {
                        return;
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return (long) getFence() - index;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

    @Override
    public void forEach(Consumer<? super Float> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        final float[] localContents = contents;
        final int localSize = this.size;
        // No, "modCount == expectedModCount" might not always be true
        for (int i = 0; modCount == expectedModCount && i < localSize; i++) { // NOSONAR
            action.accept(localContents[i]);
        }
        checkForConcurrentModification(expectedModCount);
    }

    @Override
    public Spliterator<Float> spliterator() {
        return new FloatArrayListSpliterator(0, -1, 0);
    }

    private static long[] nBits(int n) {
        return new long[((n - 1) >> 6) + 1];
    }

    private static void setBit(long[] bits, int i) {
        bits[i >> 6] |= 1L << i;
    }

    private static boolean isClear(long[] bits, int i) {
        return (bits[i >> 6] & (1L << i)) == 0;
    }

    boolean removeIf(@NonNull Predicate<? super Float> filter, int i, final int end) {
        int expectedModCount = modCount;
        final float[] localContents = contents;

        // Go ahead and skip over the initial list of items that we are going to keep
        for (; i < end && !filter.test(localContents[i]); ++i) { // NOSONAR
            // Skip
        }
        if (i < end) {
            final int beginning = i;
            final long[] toDelete = nBits(end - beginning);
            toDelete[0] = 1L;
            for (i = beginning + 1; i < end; i++) {
                if (filter.test(localContents[i])) {
                    setBit(toDelete, i - beginning);
                }
            }
            checkForConcurrentModification(expectedModCount);
            modCount++;
            int w = beginning;
            for (i = beginning; i < end; i++) {
                if (isClear(toDelete, i - beginning)) {
                    localContents[w++] = localContents[i];
                }
            }
            System.arraycopy(localContents, end, localContents, w, size - end);
            size -= end - w;
            return true;
        } else {
            checkForConcurrentModification(expectedModCount);
            return false;
        }
    }

    @Override
    public void replaceAll(UnaryOperator<Float> operator) {
        replaceAllRange(operator, 0, size);
    }

    private void replaceAllRange(@NonNull UnaryOperator<Float> operator, int i, int end) {
        final int expectedModCount = modCount;
        final float[] localContents = contents;
        for (; modCount == expectedModCount && i < end; i++) { // NOSONAR
            localContents[i] = operator.apply(localContents[i]);
        }
        checkForConcurrentModification(expectedModCount);
    }

    @Override
    public void sort(Comparator<? super Float> c) {
        final int expectedModCount = modCount;
        if (c == null) {
            Arrays.sort(contents);
        } else {
            // Seriously we need better Arrays or Stream APIs for this
            Float[] results =
                    IntStream.range(0, size)
                            .mapToObj(i -> contents[i])
                            .sorted(c)
                            .toArray(Float[]::new);
            for (int i = 0; i < size; ++i) {
                // We need to unbox, because we can't sort plain floats with a comparator for some
                // reason
                contents[i] = results[i]; // NOSONAR
            }
        }

        checkForConcurrentModification(expectedModCount);
        modCount++;
    }
}
