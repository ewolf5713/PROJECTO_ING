package com.rurex.database;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {

    @Test
    void testSerializeAndParseMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("nombre", "Juan Perez");
        map.put("edad", 30);
        map.put("activo", true);
        map.put("correo", "juan@ucv.ve");

        String json = Json.serialize(map);
        assertNotNull(json);

        Map<String, Object> parsed = Json.parseMap(json);
        assertEquals("Juan Perez", parsed.get("nombre"));
        assertEquals(30, ((Number) parsed.get("edad")).intValue());
        assertEquals(true, parsed.get("activo"));
        assertEquals("juan@ucv.ve", parsed.get("correo"));
    }

    @Test
    void testSerializeAndParseList() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> item1 = new LinkedHashMap<>();
        item1.put("id", "1");
        item1.put("val", 100);

        Map<String, Object> item2 = new LinkedHashMap<>();
        item2.put("id", "2");
        item2.put("val", 200);

        list.add(item1);
        list.add(item2);

        String json = Json.serialize(list);
        List<Map<String, Object>> parsed = Json.parseList(json);
        assertEquals(2, parsed.size());
        assertEquals("1", parsed.get(0).get("id"));
        assertEquals(100, ((Number) parsed.get(0).get("val")).intValue());
        assertEquals("2", parsed.get(1).get("id"));
        assertEquals(200, ((Number) parsed.get(1).get("val")).intValue());
    }

    @Test
    void testEscapedStrings() {
        Map<String, Object> map = new HashMap<>();
        map.put("text", "Line 1\nLine 2\t\"quoted\"");

        String json = Json.serialize(map);
        Map<String, Object> parsed = Json.parseMap(json);
        assertEquals("Line 1\nLine 2\t\"quoted\"", parsed.get("text"));
    }
}
