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

    /** Two node types, as a registry would give them. */
    private static final Variants NODES =
            new Variants() {
                private final Map<String, Schema> types =
                        Map.of(
                                "constant",
                                Schema.of(
                                        "constant",
                                        Field.number("value", 0, -1e9, 1e9, "Its value")
                                                .asRequired()),
                                "add",
                                Schema.of(
                                        "add",
                                        Field.list(
                                                "args",
                                                Field.union(
                                                        "arg",
                                                        "A node",
                                                        Field.number("n", 0, -1e9, 1e9, "A number"),
                                                        Field.reference(
                                                                "id", "nodes", null, "An ID"),
                                                        Field.typed("node", this, "A node")),
                                                "What it adds")));

                @Override
                public String name() {
                    return "node";
                }

                @Override
                public java.util.SortedSet<String> names() {
                    return new java.util.TreeSet<>(types.keySet());
                }

                @Override
                public java.util.Optional<Schema> schemaOf(String type) {
                    return java.util.Optional.ofNullable(types.get(type));
                }
            };

    @Test
    void nestedTypedValuesAreReadWithPaths() {
        final Schema file = Schema.of("file", Field.typed("root", NODES, "The node"));
        final Map<String, Object> raw =
                Map.of(
                        "root",
                        Map.of(
                                "type",
                                "add",
                                "args",
                                List.of(
                                        2,
                                        "a:b",
                                        Map.of("type", "constant", "value", 3),
                                        Map.of("type", "constant"),
                                        Map.of("type", "bogus"))));
        final Schema.Values values = file.read(raw);

        @SuppressWarnings("unchecked")
        final Map<String, Object> root = (Map<String, Object>) values.get("root");
        assertEquals("add", root.get("type"));
        assertEquals(
                List.of(2.0, "a:b", Map.of("type", "constant", "value", 3.0)),
                List.copyOf((List<?>) root.get("args")).subList(0, 3));
        final List<String> paths =
                values.errors().stream().map(Schema.Problem::field).sorted().toList();
        assertEquals(
                List.of("root.args[3].value", "root.args[4].type"),
                paths,
                values.problems().toString());
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
