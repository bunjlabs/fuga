package com.bunjlabs.fuga.settings.loader;

import com.bunjlabs.fuga.settings.MutableSettingsNode;
import com.bunjlabs.fuga.settings.SettingsNode;
import com.bunjlabs.fuga.settings.SettingsValue;
import com.bunjlabs.fuga.settings.support.DefaultSettingsNode;
import com.bunjlabs.fuga.settings.support.DefaultSettingsValue;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JacksonBasedSettingsLoader implements SettingsLoader {

    private final ObjectMapper objectMapper;

    public JacksonBasedSettingsLoader(JsonFactory factory) {
        this.objectMapper = new ObjectMapper(factory);
    }

    @Override
    public SettingsNode load(InputStream is) throws SettingsLoaderException {
        var settings = new DefaultSettingsNode();
        JsonNode rootNode;

        try {
            rootNode = objectMapper.readTree(is);
        } catch (JsonParseException e) {
            throw new SettingsLoaderException("Bad input file format", e);
        } catch (IOException e) {
            throw new SettingsLoaderException("Read error", e);
        }

        fillSettings(settings, rootNode);

        return settings;
    }

    private void fillSettings(MutableSettingsNode settings, JsonNode node) {
        node.fields().forEachRemaining(element -> {
            var n = element.getValue();
            var nType = n.getNodeType();

            switch (nType) {
                case BOOLEAN:
                case NUMBER:
                case STRING: {
                    var value = switchPrimitive(n);
                    settings.set(element.getKey(), value);
                    break;
                }
                case ARRAY: {
                    var elements = n.elements();
                    var list = new ArrayList<>();
                    elements.forEachRemaining(e -> list.add(switchPrimitive(e).value()));

                    var value = new DefaultSettingsValue(List.class, list);
                    settings.set(element.getKey(), value);
                    break;
                }
                case OBJECT: {
                    var childSettingsNode = settings.node(element.getKey());
                    fillSettings(childSettingsNode, n);
                    break;
                }
            }
        });
    }

    private SettingsValue switchPrimitive(JsonNode node) {
        var type = node.getNodeType();

        switch (type) {
            case BOOLEAN: {
                return new DefaultSettingsValue(boolean.class, node.asBoolean());
            }
            case NUMBER: {
                return new DefaultSettingsValue(long.class, node.asLong());
            }
            case STRING: {
                return new DefaultSettingsValue(String.class, node.asText());
            }
        }

        return new DefaultSettingsValue(void.class, null);
    }
}
