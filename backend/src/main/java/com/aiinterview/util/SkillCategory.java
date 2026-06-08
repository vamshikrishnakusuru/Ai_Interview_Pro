package com.aiinterview.util;



public enum SkillCategory {
    JAVA,
    SPRING_BOOT,
    REACT,
    MYSQL,
    DSA,
    DBMS,
    OOPS,
    OS,
    AI_ML,
    GENERAL;

    public static SkillCategory normalize(String input) {
        if (input == null || input.isBlank()) {
            return GENERAL;
        }

        String normalizedInput = input.toUpperCase().replace(" ", "_");

        // Direct match
        try {
            return SkillCategory.valueOf(normalizedInput);
        } catch (IllegalArgumentException e) {
            // Fuzzy search for partial matches
            for (SkillCategory cat : SkillCategory.values()) {
                if (normalizedInput.contains(cat.name()) || cat.name().contains(normalizedInput)) {
                    return cat;
                }
            }
            return GENERAL;
        }
    }
}
