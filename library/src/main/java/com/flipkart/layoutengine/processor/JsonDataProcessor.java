package com.flipkart.layoutengine.processor;

import com.google.gson.JsonElement;

/**
 *
 */
public abstract class JsonDataProcessor<E> extends AttributeProcessor<E> {
    @Override
    abstract public void handle(String key, JsonElement value, E view);
}
