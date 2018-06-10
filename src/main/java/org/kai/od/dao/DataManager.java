package org.kai.od.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kai.od.io.SerializableData;

public class DataManager {

    private Map<Long, SerializableData> dataMap;

    private DataManager() {
        dataMap = new ConcurrentHashMap<>();
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

    private enum DataManagerHolder {
        TOP_CATEGORY(),
        CATEGORY(),
        MANUFACTOR(),
        OPTICAL_DEVICE();

        DataManager dataManager;

        DataManagerHolder() {
            this.dataManager = new DataManager();
        }
    }

}
