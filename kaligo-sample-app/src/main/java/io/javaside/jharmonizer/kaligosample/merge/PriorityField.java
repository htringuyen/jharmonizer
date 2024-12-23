package io.javaside.jharmonizer.kaligosample.merge;

import lombok.Getter;

import java.util.Collection;
import java.util.Map;

public class PriorityField {

    private final String fieldName;

    @Getter
    private Object value;

    private final Map<String, Integer> sourcePriorityMap;

    private int currentPriority;

    private int currentCollectionSize = 0;

    private PriorityField(String fieldName, Map<String, Integer> sourcePriorityMap) {
        this.fieldName = fieldName;
        this.sourcePriorityMap = sourcePriorityMap;
        this.currentPriority = Integer.MAX_VALUE;
    }

    public static PriorityField of(String fieldName, Map<String, Integer> sourcePriorityMap) {
        return new PriorityField(fieldName, sourcePriorityMap);
    }


    public static PriorityField of(String fieldName) {
        return new PriorityField(fieldName, null);
    }

    public void writeField(String sourceId, Object value) {
        if (sourcePriorityMap == null) {
            writeField(value);
            return;
        }

        var valuePriority = sourcePriorityMap.get(sourceId) == null
                ? nonDefinedPriority() : sourcePriorityMap.get(sourceId);
        if (valuePriority <= currentPriority) {
            writeField(value);
            currentPriority = valuePriority;
        }
    }

    private void writeField(Object value) {
        if (value == null) {
            return;
        }
        if (/*isCollectionValue && */value instanceof Collection<?> collection) {
            if (collection.size() >= currentCollectionSize) {
                this.value = value;
                currentCollectionSize = collection.size();
            }
            return;
        }
        this.value = value;
    }

    private int nonDefinedPriority() {
        return Integer.MAX_VALUE - 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof PriorityField field) {
            return fieldName.equals(field.fieldName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return fieldName.hashCode();
    }
}





























