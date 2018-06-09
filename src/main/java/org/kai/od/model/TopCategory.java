package org.kai.od.model;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.kai.od.dao.IdGenerator;
import org.kai.od.io.CheckObjectException;
import org.kai.od.io.SerializableData;

public class TopCategory implements SerializableData {

    private Long id;

    private String name;

    private List<Long> subCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Long> subCategories) {
        this.subCategories = subCategories;
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
        this.id = buf.readLong();
        // read name
        int nameLen = buf.readInt();
        byte[] nameBytes = new byte[nameLen];
        buf.readBytes(nameBytes);
        this.name = new String(nameBytes, CharsetUtil.UTF_8);
        // read sub categories
        int size = buf.readInt();
        this.subCategories = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            subCategories.add(buf.readLong());
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
        // write id
        buf.writeLong(this.id);
        // write name
        byte[] nameBytes = this.name.getBytes(CharsetUtil.UTF_8);
        buf.writeInt(nameBytes.length);
        buf.writeBytes(nameBytes);
        // write sub categories
        buf.writeInt(subCategories.size());
        subCategories.forEach(buf::writeLong);
        int bodyLen = buf.readableBytes();
        byte[] body = new byte[bodyLen];
        // output
        buf.readBytes(body);
        output.write(Ints.toByteArray(bodyLen));
        output.write(body);
    }

    @Override
    public boolean checkObject() {
        if (this.id == null || this.id >= IdGenerator.getIdGenerator().currentTopCategoryId()) {
            return false;
        }
        if (StringUtil.isNullOrEmpty(name)) {
            return false;
        }
        for (Long key : subCategories) {
            if (key == null || key >= IdGenerator.getIdGenerator().currentTopCategoryId()) {
                return false;
            }
        }
        return true;
    }
}
