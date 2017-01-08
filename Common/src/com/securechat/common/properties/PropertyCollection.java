package com.securechat.common.properties;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.securechat.common.JsonUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PropertyCollection {
	private Map<IProperty, Object> properties;
	private JSONObject source;
	private List<IProperty> defaultProperties;

	public PropertyCollection(JSONObject source, IProperty... defaultProperties) {
		this.source = source != null ? new JSONObject(source.toMap()) : new JSONObject();
		properties = new HashMap<IProperty, Object>();
		this.defaultProperties = Arrays.asList(defaultProperties);
	}

	public <T> T getPermissive(IProperty<T> property) {
		T val = get(property);
		if (val == null) {
			val = property.getDefault();
			set(property, val);
		}
		return val;
	}

	public <T> T get(IProperty<T> property) {
		if (!properties.containsKey(property)) {
			if (source.has(property.getName())) {
				set(property, property.load(source));
				source.remove(property.getName());
			} else if (defaultProperties.contains(property)) {
				set(property, property.getDefault());
			} else {
				return null;
			}
		}
		return (T) properties.get(property);
	}

	public <T> void set(IProperty<T> property, T value) {
		properties.put(property, value);
	}
	
	public void addProperty(IProperty property){
		defaultProperties.add(property);
	}

	public Map<IProperty, Object> getProperties() {
		return properties;
	}

	public JSONObject getSource() {
		return source;
	}

	public void saveToFile(File file) {
		JSONObject result = new JSONObject(source.toMap());
		for (Entry<IProperty, Object> property : properties.entrySet()) {
			property.getKey().store(result, property.getValue());
		}

		for (IProperty property : defaultProperties) {
			if (!result.has(property.getName())) {
				property.storeDefault(result);
			}
		}
		JsonUtil.writeFile(file, result);
	}
	
	public void loadFile(File file){
		properties.clear();
		source = JsonUtil.parseFile(file);
	}

	public static PropertyCollection loadFile(File file, IProperty... defaultProperties) {
		return new PropertyCollection(JsonUtil.parseFile(file), defaultProperties);
	}

}
