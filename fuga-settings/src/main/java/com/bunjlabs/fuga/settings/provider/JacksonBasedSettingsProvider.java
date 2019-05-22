package com.bunjlabs.fuga.settings.provider;

import com.bunjlabs.fuga.settings.settings.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JacksonBasedSettingsProvider implements SettingsProvider {

    private final ObjectMapper objectMapper;

    public JacksonBasedSettingsProvider(JsonFactory factory) {
        this.objectMapper = new ObjectMapper(factory);
    }

    @Override
    public Settings load(InputStream is) throws SettingsProviderException {
        MutableSettings settings = new DefaultSettings();
        JsonNode rootNode;

        try {
            rootNode = objectMapper.readTree(is);
        } catch (JsonParseException e) {
            throw new SettingsProviderException("Bad input file format", e);
        } catch (IOException e) {
            throw new SettingsProviderException("Read error", e);
        }

        populateSettings(settings, rootNode);

        return settings;
    }

    private void populateSettings(MutableSettings settings, JsonNode node) {
        node.fields().forEachRemaining(element -> {
            JsonNode n = element.getValue();
            JsonNodeType nType = n.getNodeType();

            switch (nType) {
                case BOOLEAN:
                case NUMBER:
                case STRING: {
                    SettingsValue value = switchPrimitive(n);
                    settings.set(element.getKey(), value);
                    break;
                }
                case ARRAY: {
                    Iterator<JsonNode> elements = n.elements();

                    List<Object> arrayList = new ArrayList<>();
                    elements.forEachRemaining(arrayElement -> arrayList.add(switchPrimitive(arrayElement).getValue()));

                    SettingsValue value = new DefaultSettingsValue(List.class, arrayList);
                    settings.set(element.getKey(), value);
                    break;
                }
                case OBJECT: {
                    MutableSettings childSettingsNode = settings.node(element.getKey());

                    populateSettings(childSettingsNode, n);
                    break;
                }
            }
        });
    }

    private SettingsValue switchPrimitive(JsonNode node) {
        JsonNodeType type = node.getNodeType();

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
