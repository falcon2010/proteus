package com.flipkart.layoutengine;

import android.support.annotation.Nullable;

import com.flipkart.layoutengine.exceptions.InvalidDataPathException;
import com.flipkart.layoutengine.exceptions.JsonNullException;
import com.flipkart.layoutengine.exceptions.NoSuchDataPathException;
import com.flipkart.layoutengine.toolbox.ProteusConstants;
import com.flipkart.layoutengine.toolbox.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Aditya Sharat
 */
public class DataContext {

    private static Logger logger = LoggerFactory.getLogger(DataContext.class);
    private final boolean isClone;
    private JsonObject data;
    @Nullable
    private JsonObject reverseScope;
    @Nullable
    private JsonObject scope;
    private int index;

    public DataContext() {
        this.data = new JsonObject();
        this.scope = new JsonObject();
        this.reverseScope = new JsonObject();
        this.index = -1;
        this.isClone = false;
    }

    public DataContext(DataContext dataContext) {
        this.data = dataContext.getData();
        this.scope = dataContext.getScope();
        this.reverseScope = dataContext.getReverseScope();
        this.index = dataContext.getIndex();
        this.isClone = true;
    }

    public static DataContext updateDataContext(DataContext dataContext, JsonObject data, JsonObject scope) {
        JsonObject reverseScope = new JsonObject();
        JsonObject newData = new JsonObject();

        if (data == null) {
            data = new JsonObject();
        }

        for (Map.Entry<String, JsonElement> entry : scope.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            JsonElement element;
            try {
                element = Utils.readJson(value, data, dataContext.getIndex());
            } catch (JsonNullException | NoSuchDataPathException | InvalidDataPathException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("#getNewDataContext could not find: '" + value + "' for '" + key + "'. ERROR: " + e.getMessage());
                }
                element = new JsonObject();
            }

            newData.add(key, element);
            String unAliasedValue = value.replace(ProteusConstants.INDEX, String.valueOf(dataContext.getIndex()));
            reverseScope.add(unAliasedValue, new JsonPrimitive(key));
        }

        Utils.addElements(newData, data, false);

        if (dataContext.getData() == null) {
            dataContext.setData(new JsonObject());
        } else {
            dataContext.setData(newData);
        }
        dataContext.setScope(scope);
        dataContext.setReverseScope(reverseScope);
        return dataContext;
    }

    public static String getAliasedDataPath(String dataPath, JsonObject reverseScope, boolean isBindingPath) {
        String[] segments;
        if (isBindingPath) {
            segments = dataPath.split(ProteusConstants.DATA_PATH_DELIMITER);
        } else {
            segments = dataPath.split(ProteusConstants.DATA_PATH_SIMPLE_DELIMITER);
        }

        if (reverseScope == null) {
            return dataPath;
        }
        String alias = Utils.getPropertyAsString(reverseScope, segments[0]);
        if (alias == null) {
            return dataPath;
        }

        return dataPath.replaceFirst(Pattern.quote(segments[0]), alias);
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    @Nullable
    public JsonObject getScope() {
        return scope;
    }

    public void setScope(@Nullable JsonObject scope) {
        this.scope = scope;
    }

    @Nullable
    public JsonObject getReverseScope() {
        return reverseScope;
    }

    public void setReverseScope(@Nullable JsonObject reverseScope) {
        this.reverseScope = reverseScope;
    }

    public boolean isClone() {
        return isClone;
    }

    @Nullable
    public JsonElement get(String dataPath) {
        String aliasedDataPath = getAliasedDataPath(dataPath, reverseScope, true);
        try {
            return Utils.readJson(aliasedDataPath, data, index);
        } catch (JsonNullException e) {
            return JsonNull.INSTANCE;
        } catch (NoSuchDataPathException | InvalidDataPathException e) {
            return null;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DataContext createChildDataContext(JsonObject scope, int childIndex) {
        return updateDataContext(new DataContext(), data, scope);
    }

    public void updateDataContext(JsonObject data) {
        updateDataContext(this, data, scope);
    }
}
