package org.kai.od.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kai.od.io.SerializableData;

public class DataManager {

    private final Map<Long, SerializableData> dataMap;
    private final IdPool idPool;

    private DataManager(IdPool idPool) {
        dataMap = new ConcurrentHashMap<>();
        this.idPool = idPool;
    }

    public static DataManager topCategoryManager() {
        return DataManagerHolder.TOP_CATEGORY.dataManager;
    }

    public static DataManager categoryManager() {
        return DataManagerHolder.CATEGORY.dataManager;
    }

    public static DataManager manufactorManager() {
        return DataManagerHolder.MANUFACTOR.dataManager;
    }

    public static DataManager opticalDeviceManager() {
        return DataManagerHolder.OPTICAL_DEVICE.dataManager;
    }

    public synchronized void resetIdPool() {
        synchronized (dataMap) {
            synchronized (idPool) {
                idPool.clear();
                dataMap.keySet().forEach(idPool::addId);
            }
        }
    }

    public Collection<SerializableData> getAllData() {
        return dataMap.values();
    }

    public String addData(SerializableData data) {
        if (data != null) {
            if (data.getId() != null) {
                if (!dataMap.containsKey(data.getId())) {
                    dataMap.put(data.getId(), data);
                    return null;
                } else {
                    return "data " + data.getId() + " already exist";
                }
            } else {
                return "ID is null";
            }
        } else {
            return "data is null";
        }
    }

    public void addAll(Collection<SerializableData> data) {
        data.forEach(this::addData);
    }

    public String updateData(SerializableData data) {
        if (data != null) {
            if (data.getId() != null) {
                if (dataMap.containsKey(data.getId())) {
                    dataMap.put(data.getId(), data);
                    return null;
                } else {
                    return "data " + data.getId() + " not exist";
                }
            } else {
                return "ID is null";
            }
        } else {
            return "data is null";
        }
    }

    public String removeData(SerializableData data) {
        if (data != null) {
            if (data.getId() != null) {
                if (dataMap.containsKey(data.getId())) {
                    dataMap.remove(data.getId());
                    return null;
                } else {
                    return "data " + data.getId() + "not exist";
                }
            } else {
                return "ID is null";
            }
        } else {
            return "data is null";
        }
    }

    public void removeAll(Collection<SerializableData> data) {
        data.forEach(this::removeData);
    }

    private enum DataManagerHolder {
        TOP_CATEGORY(IdPool.topCategoryIdPool()),
        CATEGORY(IdPool.categoryIdPool()),
        MANUFACTOR(IdPool.manufactorIdPool()),
        OPTICAL_DEVICE(IdPool.opticalDevice());

        DataManager dataManager;

        DataManagerHolder(IdPool idPool) {
            this.dataManager = new DataManager(idPool);
        }
    }

}
