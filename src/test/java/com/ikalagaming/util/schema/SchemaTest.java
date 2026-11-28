package com.ikalagaming.util.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Reading values through a schema, and writing only what differs. */
class SchemaTest {
    private static final Schema BLOCK =
            Schema.of(
                    "block",
                    Field.reference("material", "materials", null, "What it is made of"),
                    Field.textList("tags", "tags", "Its tags"),
                    Field.choice("collision", "full", "How things collide with it", "full", "none"),
                    Field.flag("item", true, "Whether it is also an item"),
                    Field.number("light", 0, 0, 15, "How bright it glows"));

    @Test
    void valuesAreConvertedDefaultedAndChecked() {
        final Map<String, Object> raw = new LinkedHashMap<>();
        raw.put("tags", List.of("solid"));
        raw.put("collision", " NONE ");
        raw.put("light", 3);
        raw.put("colour", "red");
        final Schema.Values values = BLOCK.read(raw);

        assertEquals(List.of("solid"), values.texts("tags"));
        assertEquals("none", values.text("collision"));
        assertEquals(3.0, values.number("light"));
        assertTrue(values.flag("item"), "The default");
        assertEquals(null, values.text("material"));
        assertEquals(Map.of("colour", "red"), values.unknown());
        assertEquals(1, values.problems().size());
        assertFalse(values.problems().getFirst().error(), "An unknown field only warns");
    }

    @Test
    void badValuesAndMissingRequiredFieldsAreErrors() {
        final Schema withName =
                Schema.of("named", Field.text("name", null, "Its name").asRequired());
        assertEquals(1, withName.read(Map.of()).errors().size());

        final Schema.Values values = BLOCK.read(Map.of("light", 20, "item", "yes"));
        assertEquals(2, values.errors().size(), values.problems().toString());
        assertTrue(
                values.errors().stream().anyMatch(e -> e.message().contains("0 to 15")),
                values.errors().toString());
    }

    @Test
    void onlyWhatDiffersIsWritten() {
        final Map<String, Object> values = new LinkedHashMap<>();
        values.put("light", 0.0);
        values.put("item", false);
        values.put("tags", List.of());
        values.put("collision", "none");
        assertEquals(Map.of("collision", "none", "item", false), BLOCK.differing(values));
        assertEquals(
                List.of("collision", "item"),
                List.copyOf(BLOCK.differing(values).keySet()),
                "In field order");
    }
}
