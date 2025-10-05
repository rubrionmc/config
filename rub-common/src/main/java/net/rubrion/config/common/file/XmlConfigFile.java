package net.rubrion.config.common.file;

import net.rubrion.config.api.ConfigFile;
import net.rubrion.config.api.adapter.Adapters;
import net.rubrion.config.api.adapter.TypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class XmlConfigFile implements ConfigFile {

    private final Path path;
    private Map<String, Object> data;
    private boolean loaded;

    public XmlConfigFile(Path path) {
        this.path = path;
        this.data = new LinkedHashMap<>();
        this.loaded = false;
        load();
    }

    @Override
    public String getPath() {
        return path.toString();
    }

    @Override
    public void reload() {
        load();
    }

    private void load() {
        if (!Files.exists(path)) {
            copyFromResources();
            if (!Files.exists(path)) {
                data = new LinkedHashMap<>();
                loaded = true;
                return;
            }
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(path.toFile());
            document.getDocumentElement().normalize();

            data = parseElement(document.getDocumentElement());
            loaded = true;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException("Failed to load config: " + path, e);
        }
    }

    @SuppressWarnings("unchecked")
    private @NotNull Map<String, Object> parseElement(@NotNull Element element) {
        Map<String, Object> result = new LinkedHashMap<>();
        NodeList childNodes = element.getChildNodes();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attr = attributes.item(i);
            result.put("@" + attr.getNodeName(), attr.getNodeValue());
        }

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                String nodeName = childElement.getNodeName();
                Object existing = result.get(nodeName);

                if (existing == null) {
                    if (hasElementChildren(childElement)) {
                        result.put(nodeName, parseElement(childElement));
                    } else {
                        result.put(nodeName, getElementValue(childElement));
                    }
                } else if (existing instanceof List) {
                    List<Object> list = (List<Object>) existing;
                    if (hasElementChildren(childElement)) {
                        list.add(parseElement(childElement));
                    } else {
                        list.add(getElementValue(childElement));
                    }
                } else {
                    List<Object> list = new ArrayList<>();
                    if (hasElementChildren(childElement)) {
                        list.add(parseElement((Element) existing));
                        list.add(parseElement(childElement));
                    } else {
                        list.add(existing);
                        list.add(getElementValue(childElement));
                    }
                    result.put(nodeName, list);
                }
            }
        }

        return result;
    }

    private boolean hasElementChildren(@NotNull Element element) {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }
        return false;
    }

    private Object getElementValue(@NotNull Element element) {
        NodeList children = element.getChildNodes();
        StringBuilder value = new StringBuilder();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                value.append(child.getNodeValue());
            }
        }

        String strValue = value.toString().trim();

        if (strValue.equalsIgnoreCase("true") || strValue.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(strValue);
        }

        try {
            return Integer.parseInt(strValue);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(strValue);
            } catch (NumberFormatException e2) {
                return strValue;
            }
        }
    }

    private void copyFromResources() {
        String resourcePath = "config/" + path.getFileName().toString();

        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) return;

            Files.createDirectories(path.getParent());
            Files.copy(in, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("config");
            document.appendChild(rootElement);

            buildXml(document, rootElement, data);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                transformer.transform(new DOMSource(document), new StreamResult(writer));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save config: " + path, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void buildXml(Document document, Element parent, @NotNull Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.startsWith("@")) {
                parent.setAttribute(key.substring(1), value.toString());
            } else if (value instanceof Map) {
                Element child = document.createElement(key);
                parent.appendChild(child);
                buildXml(document, child, (Map<String, Object>) value);
            } else if (value instanceof List) {
                for (Object item : (List<?>) value) {
                    if (item instanceof Map) {
                        Element child = document.createElement(key);
                        parent.appendChild(child);
                        buildXml(document, child, (Map<String, Object>) item);
                    } else {
                        Element child = document.createElement(key);
                        child.setTextContent(item.toString());
                        parent.appendChild(child);
                    }
                }
            } else {
                // Simple value
                Element child = document.createElement(key);
                child.setTextContent(value.toString());
                parent.appendChild(child);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = getValue(key);
        if (value == null) {
            return Optional.empty();
        }

        if (type.isInstance(value)) {
            return Optional.of((T) value);
        }

        TypeAdapter<T> adapter = Adapters.get(type);
        if (adapter != null) {
            return Optional.of(adapter.deserialize(value, type));
        }

        return Optional.empty();
    }

    @Override
    public <T> T get(String key, Class<T> type, T defaultValue) {
        return get(key, type).orElse(defaultValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(@NotNull String key, Object value) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            current = (Map<String, Object>) current.computeIfAbsent(parts[i], k -> new LinkedHashMap<>());
        }

        current.put(parts[parts.length - 1], value);
    }

    @Override
    public boolean exists(String key) {
        return getValue(key) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(@NotNull String key) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) return;
            current = (Map<String, Object>) next;
        }

        current.remove(parts[parts.length - 1]);
    }

    @Override
    public Set<String> getKeys() {
        return collectKeys(data, "");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> getKeys(String section) {
        Object value = getValue(section);
        if (value instanceof Map) {
            return collectKeys((Map<String, Object>) value, section.isEmpty() ? "" : section + ".");
        }
        return Collections.emptySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSection(String section) {
        Object value = getValue(section);
        if (value instanceof Map) {
            return new LinkedHashMap<>((Map<String, Object>) value);
        }
        return Collections.emptyMap();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> elementType) {
        Object value = getValue(key);
        if (!(value instanceof List<?> rawList)) {
            return new ArrayList<>();
        }

        TypeAdapter<T> adapter = Adapters.get(elementType);

        if (adapter != null) {
            return rawList.stream()
                    .map(item -> adapter.deserialize(item, elementType))
                    .collect(Collectors.toList());
        }

        return rawList.stream()
                .filter(elementType::isInstance)
                .map(item -> (T) item)
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(String key, Class<K> keyType, Class<V> valueType) {
        Object value = getValue(key);
        if (!(value instanceof Map<?, ?> rawMap)) {
            return new LinkedHashMap<>();
        }

        Map<K, V> result = new LinkedHashMap<>();

        TypeAdapter<K> keyAdapter = Adapters.get(keyType);
        TypeAdapter<V> valueAdapter = Adapters.get(valueType);

        for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
            K k = keyAdapter != null
                    ? keyAdapter.deserialize(entry.getKey(), keyType)
                    : (K) entry.getKey();
            V v = valueAdapter != null
                    ? valueAdapter.deserialize(entry.getValue(), valueType)
                    : (V) entry.getValue();
            result.put(k, v);
        }

        return result;
    }

    @Override
    public void setDefault(String key, Object value) {
        if (!exists(key)) {
            set(key, value);
        }
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @SuppressWarnings("unchecked")
    private @Nullable Object getValue(@NotNull String key) {
        if (key.isEmpty()) return data;

        String[] parts = key.split("\\.");
        Object current = data;

        for (String part : parts) {
            if (!(current instanceof Map)) return null;
            current = ((Map<String, Object>) current).get(part);
            if (current == null) return null;
        }

        return current;
    }

    @SuppressWarnings("unchecked")
    private @NotNull Set<String> collectKeys(@NotNull Map<String, Object> map, String prefix) {
        Set<String> keys = new HashSet<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix + entry.getKey();
            keys.add(key);

            if (entry.getValue() instanceof Map) {
                keys.addAll(collectKeys((Map<String, Object>) entry.getValue(), key + "."));
            }
        }

        return keys;
    }
}
