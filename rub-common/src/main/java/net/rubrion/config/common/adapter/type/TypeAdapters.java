/**
 * RPL-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the RPL-LICENSE. <br>
 * License at: <a href="https://github.com/rubrionmc/.github/blob/main/licensens/RUBRION_PUBLIC">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <leycm@proton.me> <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package net.rubrion.config.common.adapter.type;

import net.rubrion.config.api.adapter.TypeAdapter;

import java.util.*;

class StringAdapter implements TypeAdapter<String> {
    @Override
    public String fromConfig(Object configValue) {
        return configValue == null ? null : configValue.toString();
    }

    @Override
    public Object toConfig(String value) {
        return value;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}

class IntegerAdapter implements TypeAdapter<Integer> {
    @Override
    public Integer fromConfig(Object configValue) {
        if (configValue == null) return null;
        if (configValue instanceof Number) {
            return ((Number) configValue).intValue();
        }
        return Integer.parseInt(configValue.toString());
    }

    @Override
    public Object toConfig(Integer value) {
        return value;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}

class LongAdapter implements TypeAdapter<Long> {
    @Override
    public Long fromConfig(Object configValue) {
        if (configValue == null) return null;
        if (configValue instanceof Number) {
            return ((Number) configValue).longValue();
        }
        return Long.parseLong(configValue.toString());
    }

    @Override
    public Object toConfig(Long value) {
        return value;
    }

    @Override
    public Class<Long> getType() {
        return Long.class;
    }
}

class DoubleAdapter implements TypeAdapter<Double> {
    @Override
    public Double fromConfig(Object configValue) {
        if (configValue == null) return null;
        if (configValue instanceof Number) {
            return ((Number) configValue).doubleValue();
        }
        return Double.parseDouble(configValue.toString());
    }

    @Override
    public Object toConfig(Double value) {
        return value;
    }

    @Override
    public Class<Double> getType() {
        return Double.class;
    }
}

class FloatAdapter implements TypeAdapter<Float> {
    @Override
    public Float fromConfig(Object configValue) {
        if (configValue == null) return null;
        if (configValue instanceof Number) {
            return ((Number) configValue).floatValue();
        }
        return Float.parseFloat(configValue.toString());
    }

    @Override
    public Object toConfig(Float value) {
        return value;
    }

    @Override
    public Class<Float> getType() {
        return Float.class;
    }
}

class BooleanAdapter implements TypeAdapter<Boolean> {
    @Override
    public Boolean fromConfig(Object configValue) {
        if (configValue == null) return null;
        if (configValue instanceof Boolean) {
            return (Boolean) configValue;
        }
        return Boolean.parseBoolean(configValue.toString());
    }

    @Override
    public Object toConfig(Boolean value) {
        return value;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }
}

@SuppressWarnings("rawtypes")
class ListAdapter implements TypeAdapter<List> {
    @Override
    public List fromConfig(Object configValue) {
        if (configValue == null) return new ArrayList<>();
        if (configValue instanceof List) {
            return (List) configValue;
        }
        return Collections.singletonList(configValue);
    }

    @Override
    public Object toConfig(List value) {
        return value;
    }

    @Override
    public Class<List> getType() {
        return List.class;
    }
}

@SuppressWarnings("rawtypes")
class MapAdapter implements TypeAdapter<Map> {
    @Override
    public Map fromConfig(Object configValue) {
        if (configValue == null) return new HashMap<>();
        if (configValue instanceof Map) {
            return (Map) configValue;
        }
        return new HashMap<>();
    }

    @Override
    public Object toConfig(Map value) {
        return value;
    }

    @Override
    public Class<Map> getType() {
        return Map.class;
    }
}