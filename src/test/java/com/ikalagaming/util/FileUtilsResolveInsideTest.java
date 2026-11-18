package com.ikalagaming.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Tests for {@link FileUtils#resolveInside(Path, String)}. */
class FileUtilsResolveInsideTest {

    @TempDir Path root;

    @ParameterizedTest
    @ValueSource(
            strings = {
                "a.png",
                "textures/a.png",
                "textures\\a.png",
                "./a.png",
                "textures/./a.png",
                "textures//a.png",
                "a..b.png",
                ".hidden",
                "not yet/made.png"
            })
    void allowsPathsInside(String name) {
        Path resolved = FileUtils.resolveInside(root, name).orElseThrow();
        assertTrue(resolved.isAbsolute());
        assertTrue(resolved.startsWith(root.toAbsolutePath().normalize()), resolved::toString);
    }

    @Test
    void resolvesBothSeparatorsTheSame() {
        assertEquals(
                FileUtils.resolveInside(root, "textures/a.png"),
                FileUtils.resolveInside(root, "textures\\a.png"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "",
                "..",
                "../a.png",
                "textures/../../a.png",
                "textures/../a.png",
                "..\\a.png",
                "...",
                ".. ",
                "textures/... /a.png",
                "/etc/passwd",
                "\\Windows\\win.ini",
                "\\\\host\\share\\a.png",
                "//host/share/a.png",
                "C:\\Windows\\win.ini",
                "C:a.png",
                "a.png:stream",
                "a\0.png"
            })
    void rejectsPathsThatLeaveOrAreAbsolute(String name) {
        assertTrue(FileUtils.resolveInside(root, name).isEmpty(), () -> "'" + name + "'");
    }

    @Test
    void rejectsLinksOutOfTheRoot(@TempDir Path outside) throws IOException {
        Path inside = Files.createDirectory(root.resolve("inside"));
        Files.writeString(outside.resolve("secret.txt"), "secret");
        Path link = inside.resolve("out");
        try {
            Files.createSymbolicLink(link, outside);
        } catch (IOException | UnsupportedOperationException e) {
            Assumptions.abort("Symbolic links can't be made here: " + e);
        }
        assertTrue(FileUtils.resolveInside(inside, "out/secret.txt").isEmpty());
        assertTrue(FileUtils.resolveInside(inside, "out/not-yet.txt").isEmpty());
    }

    @Test
    void allowsLinksThatStayInside() throws IOException {
        Path target = Files.createDirectory(root.resolve("target"));
        Files.writeString(target.resolve("a.txt"), "a");
        try {
            Files.createSymbolicLink(root.resolve("link"), target);
        } catch (IOException | UnsupportedOperationException e) {
            Assumptions.abort("Symbolic links can't be made here: " + e);
        }
        assertTrue(FileUtils.resolveInside(root, "link/a.txt").isPresent());
    }

    @Test
    void allowsARootThatDoesNotExistYet() {
        Path missing = root.resolve("missing");
        assertEquals(
                missing.toAbsolutePath().normalize().resolve("a.png"),
                FileUtils.resolveInside(missing, "a.png").orElseThrow());
    }
}
