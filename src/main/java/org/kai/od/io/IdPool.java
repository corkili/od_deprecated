package org.kai.od.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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

    public void clear() {
        ids.clear();
    }

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public void setId(Long id) {
    }

    @Override
    public void readObject(InputStream input) throws CheckObjectException, IOException {
        byte[] bodyLenBytes = new byte[4];
        if (input.read(bodyLenBytes) != 4) {
            throw new IOException("body length field is destroyed");
        }
        int bodyLen = Ints.fromByteArray(bodyLenBytes);
        byte[] body = new byte[bodyLen];
        if (input.read(body) != bodyLen) {
            throw new IOException("body field is destroyed");
        }
        ByteBuf buf = Unpooled.wrappedBuffer(body);
        // read id
        this.nextId = new AtomicLong(buf.readLong());
        // read ids
        int size = buf.readInt();
        int pairCount = buf.readInt();
        this.ids = new TreeSet<>();
        for (int i = 0; i < pairCount; i++) {
            long start = buf.readLong();
            long end = buf.readLong();
            for (long id = start; id <= end; id += 1) {
                this.ids.add(id);
            }
        }
        if (this.ids.size() != size) {
            throw new IOException("object data is invalid" + this);
        }
        if (!checkObject()) {
            throw new CheckObjectException("object is invalid! " + this);
        }
    }

    @Override
    public void writeObject(OutputStream output) throws CheckObjectException, IOException {
        if (!checkObject()) {
            throw new CheckObjectException("object is invalid! " + this);
        }
        ByteBuf buf = Unpooled.buffer();
        // write nextId
        buf.writeLong(this.nextId.get());
        // write ids
        buf.writeInt(ids.size());
        Long[] tmp = (Long[]) ids.toArray();
        List<Long> pairs = new ArrayList<>();
        int n = tmp.length - 1;
        pairs.add(tmp[0]);
        for (int i = 0; i < n; i++) {
            if (tmp[i] + 1 != tmp[i + 1]) {
                pairs.add(tmp[i]);
                pairs.add(tmp[i + 1]);
            }
        }
        if (pairs.size() % 2 == 1) {
            pairs.add(tmp[n]);
        }
        int pairCount = pairs.size() / 2;
        buf.writeInt(pairCount);
        for (int i = 0; i < pairs.size(); i += 2) {
            buf.writeLong(pairs.get(i));
            buf.writeLong(pairs.get(i + 1));
        }
        // output
        int bodyLen = buf.readableBytes();
        byte[] body = new byte[bodyLen];
        buf.readBytes(body);
        output.write(Ints.toByteArray(bodyLen));
        output.write(body);
    }

    @Override
    public boolean checkObject() {
        if (nextId == null || ids == null) {
            return false;
        }
        if (existId(nextId.get())) {
            return false;
        }
        return true;
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
