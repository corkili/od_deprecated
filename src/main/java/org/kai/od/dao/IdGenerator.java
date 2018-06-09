package org.kai.od.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

import org.kai.od.io.CheckObjectException;
import org.kai.od.io.SerializableData;

public class IdGenerator implements SerializableData {

    private AtomicLong nextTopCategoryId;

    private AtomicLong nextCategoryId;

    private AtomicLong nextOpticalDeviceId;

    private AtomicLong nextManufactorId;

    public static IdGenerator getIdGenerator() {
        return IdGeneratorHolder.INSTANCE.idGenerator;
    }

    private IdGenerator() {
        nextTopCategoryId = new AtomicLong(0L);
        nextCategoryId = new AtomicLong(0L);
        nextOpticalDeviceId = new AtomicLong(0L);
        nextManufactorId = new AtomicLong(0L);
    }

    public Long getNextTopCategoryId() {
        return nextTopCategoryId.getAndIncrement();
    }

    public Long getNextCategoryId() {
        return nextCategoryId.getAndIncrement();
    }

    public Long getNextOpticalDeviceId() {
        return nextOpticalDeviceId.getAndIncrement();
    }

    public Long getNextManufactorId() {
        return nextManufactorId.getAndIncrement();
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

    private enum IdGeneratorHolder {
        INSTANCE;

        private IdGenerator idGenerator;

        IdGeneratorHolder() {
            this.idGenerator = new IdGenerator();
        }

    }
}
