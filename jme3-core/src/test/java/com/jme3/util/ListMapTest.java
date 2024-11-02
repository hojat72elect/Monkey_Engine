
package com.jme3.util;

import java.util.Map.Entry;
import org.junit.Test;

/**
 * Check if the {@link ListMap} class is working correctly.
 * 
 * @author Kirill Vainer
 */
public class ListMapTest {

    @Test
    public void testListMap() {
        ListMap<String, String> listMap = new ListMap<>();
        listMap.put("bob", "hello");
        assert "hello".equals(listMap.get("bob"));
        assert "hello".equals(listMap.remove("bob"));
        assert listMap.size() == 0;
        assert listMap.isEmpty();

        listMap.put("abc", "1");
        listMap.put("def", "2");
        listMap.put("ghi", "3");
        listMap.put("jkl", "4");
        listMap.put("mno", "5");
        assert "3".equals(listMap.get("ghi"));
        assert listMap.size() == 5;
        assert !listMap.isEmpty();

        // check iteration order, should be consistent
        for (int i = 0; i < listMap.size(); i++) {
            String expectedValue = Integer.toString(i + 1);
            String key = listMap.getKey(i);
            String value = listMap.getValue(i);
            Entry<String, String> entry = listMap.getEntry(i);
            assert key.equals(entry.getKey());
            assert value.equals(entry.getValue());
            assert expectedValue.equals(value);
        }
    }
}
