package org.kai.od.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

import org.kai.od.dao.IdPool;
import org.kai.od.io.CheckObjectException;
import org.kai.od.io.SerializableData;

public class OpticalDevice implements SerializableData {

    private Long id;

    private String name;

    private String type;

    private Long topCategory;

    private Long category;

    private Double price;

    private String characteristics;

    private String describe;

    private List<Long> representativeManufactors;

    public OpticalDevice() {

    }

    public OpticalDevice(Long id, String name, String type, Long topCategory, Long category,
                         Double price, String characteristics, String describe,
                         List<Long> representativeManufactors) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.topCategory = topCategory;
        this.category = category;
        this.price = price;
        this.characteristics = characteristics;
        this.describe = describe;
        this.representativeManufactors = representativeManufactors;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(Long topCategory) {
        this.topCategory = topCategory;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<Long> getRepresentativeManufactors() {
        return representativeManufactors;
    }

    public void setRepresentativeManufactors(List<Long> representativeManufactors) {
        this.representativeManufactors = representativeManufactors;
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
        this.name = readString(buf);
        // read type
        this.type = readString(buf);
        // read top category
        this.category = buf.readLong();
        // read category
        this.category = buf.readLong();
        // read price
        this.price = buf.readDouble();
        // read characteristics
        this.characteristics = readString(buf);
        // read describe
        this.describe = readString(buf);
        // read representative manufactors
        int size = buf.readInt();
        this.representativeManufactors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.representativeManufactors.add(buf.readLong());
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
        writeString(buf, this.name);
        // write type
        writeString(buf, this.type);
        // write top category
        buf.writeLong(this.topCategory);
        // write category
        buf.writeLong(this.category);
        // write price
        buf.writeDouble(this.price);
        // write characteristics
        writeString(buf, this.characteristics);
        // write describe
        writeString(buf, this.describe);
        // write representative manufactors
        buf.writeInt(representativeManufactors.size());
        representativeManufactors.forEach(buf::writeLong);
        // output
        int bodyLen = buf.readableBytes();
        byte[] body = new byte[bodyLen];
        buf.readBytes(body);
        output.write(Ints.toByteArray(bodyLen));
        output.write(body);
    }

    @Override
    public boolean checkObject() {
        if (this.id == null || IdPool.opticalDevice().existId(this.id)) {
            return false;
        }
        if (StringUtil.isNullOrEmpty(this.name)) {
            return false;
        }
        if (StringUtil.isNullOrEmpty(this.type)) {
            return false;
        }
        if (topCategory == null || IdPool.topCategoryIdPool().existId(topCategory)) {
            return false;
        }
        if (category == null || IdPool.categoryIdPool().existId(category)) {
            return false;
        }
        if (price == null) {
            return false;
        }
        if (StringUtil.isNullOrEmpty(characteristics)) {
            return false;
        }
        if (StringUtil.isNullOrEmpty(describe)) {
            return false;
        }
        for (Long k : representativeManufactors) {
            if (k == null || IdPool.manufactorIdPool().existId(k)) {
                return false;
            }
        }
        return true;
    }
    
    private void writeString(ByteBuf buf, String str) {
        byte[] bytes = str.getBytes(CharsetUtil.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }
    
    private String readString(ByteBuf buf) {
        int len = buf.readInt();
        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        return new String(bytes, CharsetUtil.UTF_8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpticalDevice that = (OpticalDevice) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(topCategory, that.topCategory) &&
                Objects.equals(category, that.category) &&
                Objects.equals(price, that.price) &&
                Objects.equals(characteristics, that.characteristics) &&
                Objects.equals(describe, that.describe) &&
                Objects.equals(representativeManufactors, that.representativeManufactors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, topCategory, category, price, characteristics, describe, representativeManufactors);
    }

    @Override
    public String toString() {
        return "OpticalDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", topCategory=" + topCategory +
                ", category=" + category +
                ", price=" + price +
                ", characteristics='" + characteristics + '\'' +
                ", describe='" + describe + '\'' +
                ", representativeManufactors=" + representativeManufactors +
                '}';
    }
}
