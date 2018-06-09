package org.kai.od.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import org.kai.od.io.CheckObjectException;
import org.kai.od.io.SerializableData;

public class IdPool implements SerializableData {

    private AtomicLong nextId;

    private Set<Long> ids;

    public static IdPool topCategoryIdPool() {
        return IdPoolHolder.TOP_CATEGORY.idPool;
    }

    public static IdPool categoryIdPool() {
        return IdPoolHolder.CATEGORY.idPool;
    }

    public static IdPool manufactorIdPool() {
        return IdPoolHolder.MANUFACTOR.idPool;
    }

    public static IdPool opticalDevice() {
        return IdPoolHolder.OPTICAL_DEVICE.idPool;
    }

    private IdPool() {
        nextId = new AtomicLong(0L);
        ids = new TreeSet<>();
    }

    public long nextId() {
        return nextId.getAndIncrement();
    }

    public void addId(long id) {
        ids.add(id);
    }

    public void removeId(long id) {
        ids.remove(id);
    }

    public boolean existId(long id) {
        return ids.contains(id);
    }

    @Override
    public void readObject(InputStream input) throws CheckObjectException {

    }

    @Override
    public void writeObject(OutputStream output) throws CheckObjectException {

    }

    @Override
    public boolean checkObject() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdPool idPool = (IdPool) o;
        return Objects.equals(nextId, idPool.nextId) &&
                Objects.equals(ids, idPool.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextId, ids);
    }

    @Override
    public String toString() {
        return "IdPool{" +
                "nextId=" + nextId +
                ", ids=" + ids +
                '}';
    }

    private enum IdPoolHolder {
        TOP_CATEGORY,
        CATEGORY,
        MANUFACTOR,
        OPTICAL_DEVICE;

        private IdPool idPool;

        IdPoolHolder() {
            this.idPool = new IdPool();
        }

    }
}
