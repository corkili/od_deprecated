package org.kai.od.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.io.FileUtils;

import org.kai.od.model.Category;
import org.kai.od.model.Manufactor;
import org.kai.od.model.OpticalDevice;
import org.kai.od.model.TopCategory;

public class FileDao {

    private FileDao(){}

    public static FileDao getInstance() {
        return FileDaoHolder.SINGLETON.fileDao;
    }

    public void store() {
        DataManager topCategoryManager = DataManager.topCategoryManager();
        DataManager categoryManager = DataManager.categoryManager();
        DataManager manufactorManager = DataManager.manufactorManager();
        DataManager opticalDeviceManager = DataManager.opticalDeviceManager();

        topCategoryManager.resetIdPool();
        categoryManager.resetIdPool();
        manufactorManager.resetIdPool();
        opticalDeviceManager.resetIdPool();

        // 一致性校验
        final List<SerializableData> removedData = new ArrayList<>();
        final Collection<SerializableData> topCategories = topCategoryManager.getAllData();
        topCategories.forEach(data -> {
            if (data instanceof TopCategory) {
                TopCategory topCategory = (TopCategory) data;
                final List<Long> removedId = new ArrayList<>();
                topCategory.getSubCategories().forEach(id -> {
                    if (!IdPool.categoryIdPool().existId(id)) {
                        removedId.add(id);
                    }
                });
                topCategory.getSubCategories().removeAll(removedId);
            } else {
                removedData.add(data);
            }
        });
        topCategoryManager.removeAll(removedData);

        removedData.clear();
        final Collection<SerializableData> categories = categoryManager.getAllData();
        categories.forEach(data -> {
            if (data instanceof Category) {
                Category category = (Category) data;
                final List<Long> removedId = new ArrayList<>();
                category.getOpticalDevices().forEach(id -> {
                    if (!IdPool.opticalDevice().existId(id)) {
                        removedId.add(id);
                    }
                });
                category.getOpticalDevices().removeAll(removedId);
            } else {
                removedData.add(data);
            }
        });
        categoryManager.removeAll(removedData);

        removedData.clear();
        final Collection<SerializableData> opticalDevices = opticalDeviceManager.getAllData();
        opticalDevices.forEach(data -> {
            if (data instanceof OpticalDevice) {
                OpticalDevice opticalDevice = (OpticalDevice) data;
                if (!IdPool.topCategoryIdPool().existId(opticalDevice.getTopCategory())
                        || !IdPool.categoryIdPool().existId(opticalDevice.getCategory())) {
                    removedData.add(data);
                } else {
                    final List<Long> removedId = new ArrayList<>();
                    opticalDevice.getRepresentativeManufactors().forEach(id -> {
                        if (!IdPool.manufactorIdPool().existId(id)) {
                            removedId.add(id);
                        }
                    });
                    opticalDevice.getRepresentativeManufactors().removeAll(removedId);
                }
            } else {
                removedData.add(data);
            }
        });
        opticalDeviceManager.removeAll(removedData);

        store(topCategoryManager.getAllData(), "tc.dat");
        store(categoryManager.getAllData(), "cg.dat");
        store(manufactorManager.getAllData(), "mf.dat");
        store(opticalDeviceManager.getAllData(), "od.dat");

        Collection<SerializableData> idPools = new ArrayList<>(Arrays.asList(IdPool.topCategoryIdPool(),
                IdPool.categoryIdPool(), IdPool.manufactorIdPool(), IdPool.opticalDevice()));
        store(idPools, "id.dat");
    }

    public void load() {
        DataManager topCategoryManager = DataManager.topCategoryManager();
        DataManager categoryManager = DataManager.categoryManager();
        DataManager manufactorManager = DataManager.manufactorManager();
        DataManager opticalDeviceManager = DataManager.opticalDeviceManager();
        load("id.dat");
        topCategoryManager.addAll(load("tc.dat"));
        categoryManager.addAll(load("cg.dat"));
        manufactorManager.addAll(load("mf.dat"));
        opticalDeviceManager.addAll(load("od.dat"));
    }

    private void store(Collection<SerializableData> allData, String filename) {
        File file = new File(filename);
        file.deleteOnExit();
        try {
            if (!file.createNewFile()) {
                System.err.println("cannot create file - " + filename);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuf buf = Unpooled.buffer();
        int size = 0;
        for (SerializableData data : allData) {
            OdDataOutputStream outputStream = new OdDataOutputStream();
            try {
                data.writeObject(outputStream);
                buf.writeBytes(outputStream.getData());
                size++;
            } catch (CheckObjectException | IOException e) {
                System.err.println(filename + " - " + e.getMessage());
            }
        }
        byte[] body = new byte[buf.readableBytes()];
        buf.readBytes(body);
        try {
            FileUtils.writeByteArrayToFile(file, Bytes.concat(Ints.toByteArray(size), body));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Collection<SerializableData> load(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("file not found");
            return new ArrayList<>();
        }
        byte[] fileData = null;
        try {
            fileData = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileData == null || fileData.length == 0) {
            System.err.println("cannot read data from file - " + filename);
            return new ArrayList<>();
        }
        ByteBuf buf = Unpooled.wrappedBuffer(fileData);
        int size = buf.readInt();
        Collection<SerializableData> allData;
        if (filename.contains("id.dat")) {
           allData  = new ArrayList<>(Arrays.asList(IdPool.topCategoryIdPool(),
                    IdPool.categoryIdPool(), IdPool.manufactorIdPool(), IdPool.opticalDevice()));
        } else {
            allData = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (filename.contains("tc.dat")) {
                    allData.add(new TopCategory());
                } else if (filename.contains("cg.dat")) {
                    allData.add(new Category());
                } else if (filename.contains("mf.dat")) {
                    allData.add(new Manufactor());
                } else if (filename.contains("od.dat")) {
                    allData.add(new OpticalDevice());
                }
            }
        }
        byte[] body = new byte[buf.readableBytes()];
        buf.readBytes(body);
        OdDataInputStream inputStream = new OdDataInputStream(body);
        allData.forEach(data -> {
            try {
                data.readObject(inputStream);
            } catch (CheckObjectException | IOException e) {
                System.err.println(e.getMessage());
            }
        });
        return allData;
    }

    private enum FileDaoHolder{
        SINGLETON;

        FileDao fileDao;

        FileDaoHolder(){
            fileDao = new FileDao();
        }
    }
}
