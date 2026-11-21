package com.ikalagaming.plugins;

import com.github.zafarkhaja.semver.Version;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The versions of a dependency that a plugin accepts, such as {@code >=1.2.0 <2.0.0}. Each bound is
 * an operator ({@code >=}, {@code >}, {@code <=}, {@code <} or {@code =}) and a full semantic
 * version, and a version is accepted when it meets every bound. A bound with no operator means
 * exactly that version. No bounds at all accepts any version.
 *
 * <p>Versions are compared by semantic versioning precedence, so a pre-release comes before its
 * release: {@code 1.0.0-alpha} does not meet {@code >=1.0.0}.
 *
 * @param bounds The bounds a version has to meet.
 */
public record VersionRequirement(@NonNull List<Bound> bounds) {

    /** A requirement that accepts any version. */
    public static final VersionRequirement ANY = new VersionRequirement(List.of());

    /** How a bound compares a version to its own. */
    public enum Operator {
        /** At least the version. */
        AT_LEAST(">="),
        /** Newer than the version. */
        NEWER(">"),
        /** At most the version. */
        AT_MOST("<="),
        /** Older than the version. */
        OLDER("<"),
        /** Exactly the version. */
        EXACTLY("=");

        /** How the operator is written. */
        private final String symbol;

        /**
         * An operator.
         *
         * @param symbol How it is written.
         */
        Operator(String symbol) {
            this.symbol = symbol;
        }

        /**
         * Whether the result of comparing a version to the bound passes.
         *
         * @param comparison The version compared to the bound's version, as from compareTo.
         * @return Whether it passes.
         */
        boolean passes(int comparison) {
            return switch (this) {
                case AT_LEAST -> comparison >= 0;
                case NEWER -> comparison > 0;
                case AT_MOST -> comparison <= 0;
                case OLDER -> comparison < 0;
                case EXACTLY -> comparison == 0;
            };
        }
    }

    /**
     * One bound on a version.
     *
     * @param operator How versions are compared to this one.
     * @param version The version.
     */
    public record Bound(@NonNull Operator operator, @NonNull Version version) {
        @Override
        public String toString() {
            return operator.symbol + version;
        }
    }

    /**
     * A requirement, copying the bounds.
     *
     * @param bounds The bounds a version has to meet.
     */
    public VersionRequirement {
        bounds = List.copyOf(bounds);
    }

    /**
     * Read a requirement from its bounds, separated by spaces, such as {@code >=1.2.0 <2.0.0}.
     *
     * @param texts The bounds as written.
     * @return The requirement.
     * @throws IllegalArgumentException If a bound isn't an operator and a full semantic version.
     */
    public static VersionRequirement parse(@NonNull List<String> texts) {
        List<Bound> bounds = new ArrayList<>();
        for (String text : texts) {
            bounds.add(VersionRequirement.parseBound(text));
        }
        return new VersionRequirement(bounds);
    }

    /**
     * Read one bound.
     *
     * @param text The bound as written, such as {@code >=1.2.0}.
     * @return The bound.
     * @throws IllegalArgumentException If it isn't an operator and a full semantic version.
     */
    private static Bound parseBound(@NonNull String text) {
        Operator operator = Operator.EXACTLY;
        String version = text;
        // Two-character operators first, so >= isn't read as >
        for (Operator candidate :
                List.of(
                        Operator.AT_LEAST,
                        Operator.AT_MOST,
                        Operator.NEWER,
                        Operator.OLDER,
                        Operator.EXACTLY)) {
            if (text.startsWith(candidate.symbol)) {
                operator = candidate;
                version = text.substring(candidate.symbol.length());
                break;
            }
        }
        if (!Version.isValid(version)) {
            throw new IllegalArgumentException(
                    "'" + text + "' is not a version bound, such as >=1.2.0 or <2.0.0");
        }
        return new Bound(operator, VersionRequirement.precedenceOf(version));
    }

    /**
     * Whether a version meets every bound.
     *
     * @param version The version, which must be a valid semantic version.
     * @return Whether it is accepted.
     */
    public boolean accepts(@NonNull String version) {
        if (bounds.isEmpty()) {
            return true;
        }
        if (!Version.isValid(version)) {
            return false;
        }
        Version parsed = VersionRequirement.precedenceOf(version);
        return bounds.stream()
                .allMatch(bound -> bound.operator().passes(parsed.compareTo(bound.version())));
    }

    /**
     * A version without its build metadata, which semantic versioning ignores for precedence, so
     * {@code 1.2.3+build.7} is the same as {@code 1.2.3}.
     *
     * @param version A valid semantic version.
     * @return The version without build metadata.
     */
    private static Version precedenceOf(@NonNull String version) {
        int build = version.indexOf('+');
        return Version.parse(build < 0 ? version : version.substring(0, build));
    }

    /**
     * Whether any version is accepted.
     *
     * @return True if there are no bounds.
     */
    public boolean isAny() {
        return bounds.isEmpty();
    }

    @Override
    public String toString() {
        return bounds.isEmpty()
                ? "any version"
                : bounds.stream().map(Bound::toString).collect(Collectors.joining(" "));
    }
}
