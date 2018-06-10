package org.kai.od.core;

import org.kai.od.io.DataManager;
import org.kai.od.io.IdPool;
import org.kai.od.io.SerializableData;
import org.kai.od.model.Category;
import org.kai.od.model.Manufactor;
import org.kai.od.model.OpticalDevice;
import org.kai.od.model.TopCategory;

public class QueryEngine {

    private DataManager topCategoryManager;

    private DataManager categoryManager;

    private DataManager manufactorManager;

    private DataManager opticalDeviceManager;

    private QueryEngine() {
        topCategoryManager = DataManager.topCategoryManager();
        categoryManager = DataManager.categoryManager();
        manufactorManager = DataManager.manufactorManager();
        opticalDeviceManager = DataManager.opticalDeviceManager();
    }

    public static QueryEngine getInstance() {
        return QueryEngineHolder.SINGLETON.queryEngine;
    }

    public QueryResult createTopCategory(Query query) {
        String name = query.getString("name");
        if (name == null || "".equals(name.trim())) {
            return new QueryResult(false, "name is null");
        }
        name = name.trim();
        for (SerializableData data : topCategoryManager.getAllData()) {
            if (name.equals(((TopCategory) data).getName())) {
                return new QueryResult(false, "name {" + name + "} already exist");
            }
        }
        Long id = IdPool.topCategoryIdPool().nextId();
        TopCategory topCategory = new TopCategory();
        topCategory.setId(id);
        topCategory.setName(name);
        String res = topCategoryManager.addData(topCategory);
        if (res != null) {
            return new QueryResult(false, res);
        } else {
            IdPool.topCategoryIdPool().addId(id);
            return new QueryResult(true, topCategory.toString(), topCategory);
        }
    }

    public QueryResult createCategory(Query query) {
        String name = query.getString("name");
        if (name == null || "".equals(name.trim())) {
            return new QueryResult(false, "name is null");
        }
        name = name.trim();
        Long parentCategory = query.getLong("topCategory");
        if (parentCategory == null
                || !IdPool.topCategoryIdPool().existId(parentCategory)
                || topCategoryManager.get(parentCategory) == null) {
            return new QueryResult(false, "topCategory is invalid");
        }
        Long id = IdPool.categoryIdPool().nextId();
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        String res = categoryManager.addData(category);
        if (res != null) {
            return new QueryResult(false, res);
        } else {
            TopCategory topCategory = (TopCategory) topCategoryManager.get(id);
            topCategory.getSubCategories().add(id);
            IdPool.categoryIdPool().addId(id);
            return new QueryResult(true, category.toString(), category);
        }
    }

    public QueryResult createManufactor(Query query) {
        String name = query.getString("name");
        if (name == null || "".equals(name.trim())) {
            return new QueryResult(false, "name is null");
        }
        name = name.trim();
        Long id = IdPool.manufactorIdPool().nextId();
        Manufactor manufactor = new Manufactor();
        manufactor.setId(id);
        manufactor.setName(name);
        String res = manufactorManager.addData(manufactor);
        if (res != null) {
            return new QueryResult(false, res);
        } else {
            IdPool.topCategoryIdPool().addId(id);
            return new QueryResult(true, manufactor.toString(), manufactor);
        }
    }

    public QueryResult createOpticalDevice(Query query) {
        String name = query.getString("name");
        if (name == null || "".equals(name.trim())) {
            return new QueryResult(false, "name is null");
        }
        name = name.trim();

        String type = query.getString("type");
        if (type == null || "".equals(type.trim())) {
            return new QueryResult(false, "type is null");
        }
        type = type.trim();

        String characteristics = query.getString("characteristics");
        if (characteristics == null || "".equals(characteristics.trim())) {
            return new QueryResult(false, "characteristics is null");
        }
        characteristics = characteristics.trim();

        String describe = query.getString("describe");
        if (describe == null || "".equals(describe.trim())) {
            return new QueryResult(false, "describe is null");
        }
        describe = describe.trim();

        Long topCategory = query.getLong("topCategory");
        if (topCategory == null
                || !IdPool.topCategoryIdPool().existId(topCategory)
                || topCategoryManager.get(topCategory) == null) {
            return new QueryResult(false, "topCategory is invalid");
        }

        Long category = query.getLong("topCategory");
        if (category == null
                || !IdPool.topCategoryIdPool().existId(category)
                || categoryManager.get(category) == null
                || !((TopCategory) topCategoryManager.get(topCategory)).getSubCategories().contains(category)) {
            return new QueryResult(false, "category is invalid");
        }
        
        Long representativeManufactor = query.getLong("representativeManufactor");
        if (representativeManufactor == null
                || !IdPool.manufactorIdPool().existId(representativeManufactor)
                || manufactorManager.get(representativeManufactor) == null) {
            return new QueryResult(false, "representativeManufactor is invalid");
        }
        
        Double price = query.getDouble("price");
        if (price == null) {
            price = 0.0;
        }

        Long id = IdPool.opticalDevice().nextId();
        OpticalDevice opticalDevice = new OpticalDevice(id, name, type, topCategory, category,
                price, characteristics, describe, representativeManufactor);
        String res = opticalDeviceManager.addData(opticalDevice);
        if (res != null) {
            return new QueryResult(false, res);
        } else {
            IdPool.opticalDevice().addId(id);
            return new QueryResult(true, opticalDevice.toString(), opticalDevice);
        }
    }

    public QueryResult deleteOpticalDevice(Query query) {
        Long id = query.getLong("id");
        if (id == null) {
            return new QueryResult(false, "id is null");
        }
        OpticalDevice opticalDevice = (OpticalDevice) opticalDeviceManager.get(id);
        if (opticalDevice == null || !IdPool.opticalDevice().existId(id)) {
            return new QueryResult(false, "optical device - " + id + " not exist");
        }
        Category category = (Category) categoryManager.get(opticalDevice.getCategory());
        if (category == null) {
            return new QueryResult(false, "parent category - " + opticalDevice.getCategory() + " not exist");
        }
        category.getOpticalDevices().remove(id);
        IdPool.opticalDevice().removeId(id);
        String res = opticalDeviceManager.removeData(opticalDevice);
        if (res != null) {
            return new QueryResult(false, res);
        } else {
            return new QueryResult(true, opticalDevice.toString(), opticalDevice);
        }
    }

    public QueryResult deleteCategory(Query query) {
        Long id = query.getLong("id");
        if (id == null) {
            return new QueryResult(false, "id is null");
        }
        Category category = (Category) categoryManager.get(id);
        if (category == null || !IdPool.categoryIdPool().existId(id)) {
            return new QueryResult(false, "category - " + id + " not exist");
        }
        if (!category.getOpticalDevices().isEmpty()) {
            return new QueryResult(false, "This category has optical device, so cannot delete it");
        }
        IdPool.categoryIdPool().removeId(id);
        String res = categoryManager.removeData(category);
        if (res != null) {
            return new QueryResult(false, res);
        } else {
            return new QueryResult(true, category.toString(), category);
        }
    }

    public QueryResult deleteManufactor(Query query) {
        Long id = query.getLong("id");
        if (id == null) {
            return new QueryResult(false, "id is null");
        }
        Manufactor manufactor = (Manufactor) manufactorManager.get(id);
        if (manufactor == null || !IdPool.manufactorIdPool().existId(id)) {
            return new QueryResult(false, "manufactor - " + id + " not exist");
        }
        for (SerializableData data : opticalDeviceManager.getAllData()) {
            OpticalDevice opticalDevice = (OpticalDevice) data;
            if (opticalDevice.getRepresentativeManufactor().equals(manufactor.getId())) {
                return new QueryResult(false, "This manufactor has optical device, so cannot delete it");
            }
        }
        IdPool.manufactorIdPool().removeId(id);
        String res = manufactorManager.removeData(manufactor);
        if (res != null) {
            return new QueryResult(false, res);
        } else {
            return new QueryResult(true, manufactor.toString(), manufactor);
        }
    }

    public QueryResult deleteTopCategory(Query query) {
        Long id = query.getLong("id");
        if (id == null) {
            return new QueryResult(false, "id is null");
        }
        TopCategory topCategory = (TopCategory) topCategoryManager.get(id);
        if (topCategory == null || !IdPool.topCategoryIdPool().existId(id)) {
            return new QueryResult(false, "topCategory - " + id + " not exist");
        }
        if (!topCategory.getSubCategories().isEmpty()) {
            return new QueryResult(false, "This top category has category, so cannot delete it");
        }
        IdPool.topCategoryIdPool().removeId(id);
        String res = topCategoryManager.removeData(topCategory);
        if (res != null) {
            return new QueryResult(false, res);
        } else {
            return new QueryResult(true, topCategory.toString(), topCategory);
        }
    }

    private enum QueryEngineHolder {
        SINGLETON;

        QueryEngine queryEngine;

        QueryEngineHolder() {
            queryEngine = new QueryEngine();
        }
    }
}
