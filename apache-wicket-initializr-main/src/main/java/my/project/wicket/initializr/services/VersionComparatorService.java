package my.project.wicket.initializr.services;

import my.project.wicket.initializr.beans.ArtifactVersionViewBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * A version comparator service that compares version strings.
 */
@Component
public class VersionComparatorService implements IVersionComparatorService {

    private static final List<String> PRE_RELEASE_ORDER = Arrays.asList("alpha", "beta", "RC", "M");

    @Override
    public int compare(ArtifactVersionViewBean version2, ArtifactVersionViewBean version1) {
        String[] parts1 = normalizeVersion(version1.getVersion()).split("\\.");
        String[] parts2 = normalizeVersion(version2.getVersion()).split("\\.");
        int maxLength = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < maxLength; i++) {
            String part1 = i < parts1.length ? parts1[i] : "0";
            String part2 = i < parts2.length ? parts2[i] : "0";

            int comparison = comparePart(part1, part2);
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }

    private String normalizeVersion(String version) {
        return version.replaceAll("(?i)-", ".").replaceAll("(?i)([a-z]+)", ".$1.");
    }

    private int comparePart(String part1, String part2) {
        try {
            // Try comparing as integers
            return Integer.compare(Integer.parseInt(part1), Integer.parseInt(part2));
        } catch (NumberFormatException e) {
            // Compare as strings for pre-release tags
            return comparePreRelease(part1, part2);
        }
    }

    private int comparePreRelease(String part1, String part2) {
        String normalized1 = normalizePreRelease(part1);
        String normalized2 = normalizePreRelease(part2);

        int index1 = PRE_RELEASE_ORDER.indexOf(normalized1.toLowerCase());
        int index2 = PRE_RELEASE_ORDER.indexOf(normalized2.toLowerCase());

        if (index1 == -1 && index2 == -1) {
            // Compare as strings if both are unknown pre-release types
            return part1.compareTo(part2);
        }
        if (index1 == -1) return 1; // Unknown pre-release types come after known ones
        if (index2 == -1) return -1;
        return Integer.compare(index1, index2);
    }

    private String normalizePreRelease(String part) {
        return part.replaceAll("\\d", "").toLowerCase();
    }
}
