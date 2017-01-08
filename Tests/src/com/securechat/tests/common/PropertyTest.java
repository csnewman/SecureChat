package com.securechat.tests.common;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.securechat.common.properties.CollectionProperty;
import com.securechat.common.properties.PrimitiveProperty;
import com.securechat.common.properties.PropertyCollection;

public class PropertyTest {

	@Test
	public void basicGetAndSet() {
		PrimitiveProperty<String> a = new PrimitiveProperty<String>("a", "defaulta");
		PrimitiveProperty<Integer> b = new PrimitiveProperty<Integer>("b", 12);
		PrimitiveProperty<Boolean> c = new PrimitiveProperty<Boolean>("c", false);
		PropertyCollection collection = new PropertyCollection(null, c);

		assertTrue("default invalid", collection.get(a) == null);
		assertTrue("default invalid", collection.get(b) == null);
		assertTrue("default invalid", collection.get(c) == false);
		assertTrue("permissive default invalid", collection.getPermissive(a) == "defaulta");
		assertTrue("permissive default invalid", collection.getPermissive(b) == 12);
		assertTrue("permissive default invalid", collection.getPermissive(c) == false);

		collection.set(a, "something");

		assertTrue("invalid value", collection.get(a) == "something");
		assertTrue("invalid value", collection.get(b) == 12);
		assertTrue("invalid value", collection.get(c) == false);
	}

	@Test
	public void collectionGetAndSet() {
		PrimitiveProperty<String> a = new PrimitiveProperty<String>("a", "defaulta");
		PrimitiveProperty<Integer> b = new PrimitiveProperty<Integer>("b", 12);
		PrimitiveProperty<Boolean> c = new PrimitiveProperty<Boolean>("c", false);

		CollectionProperty c1 = new CollectionProperty("c1", b);
		PropertyCollection collection = new PropertyCollection(null, c1, c);

		assertTrue("default invalid", collection.get(a) == null);
		assertTrue("default invalid", collection.get(c1) != null);
		assertTrue("default invalid", collection.get(c) != null);
		
		collection.set(a, "something");
		assertTrue("invalid value", collection.get(a) == "something");
		
		PropertyCollection col1 = collection.get(c1);
		assertTrue("invalid value", col1 != null);
		assertTrue("invalid value", col1.get(b) == 12);
		
		col1.set(b, 24);
		assertTrue("invalid value", col1.get(b) == 24);
		
		PropertyCollection col2 = collection.get(c1);
		assertTrue("invalid value", col2.get(b) == 24);
	}
	
	@Test
	public void saveAndLoadGetAndSet() {
		PrimitiveProperty<String> a = new PrimitiveProperty<String>("a", "defaulta");
		PrimitiveProperty<Integer> b = new PrimitiveProperty<Integer>("b", 12);
		PrimitiveProperty<Boolean> c = new PrimitiveProperty<Boolean>("c", false);

		CollectionProperty c1 = new CollectionProperty("c1", b);
		PropertyCollection collection = new PropertyCollection(null, c1, c);

		assertTrue("default invalid", collection.get(a) == null);
		assertTrue("default invalid", collection.get(c1) != null);
		assertTrue("default invalid", collection.get(c) != null);
		
		collection.set(a, "something");
		assertTrue("invalid value", collection.get(a) == "something");
		
		PropertyCollection col1 = collection.get(c1);
		assertTrue("invalid value", col1 != null);
		assertTrue("invalid value", col1.get(b) == 12);
		
		col1.set(b, 24);
		assertTrue("invalid value", col1.get(b) == 24);
		
		File temp = new File("temp.json");
		collection.saveToFile(temp);
		
		collection.loadFile(temp);
		PropertyCollection col2 = collection.get(c1);
		
		assertTrue("invalid value", col2 != null);
		assertTrue("invalid value", col2.get(b) != null);
		assertTrue("invalid value", col2.get(b) == 24);
		
		temp.delete();
	}

}
