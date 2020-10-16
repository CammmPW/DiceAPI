package org.dicemc.diceapi.api;

import java.util.HashMap;
import java.util.Map;

public class LanguageString {
    private final String string;

    private final HashMap<String, String> replacements;

    public LanguageString(String string, HashMap<String, String> replacements) {
        this.string = string;
        this.replacements = replacements;
    }

    public LanguageString(String string) {
        this(string, new HashMap<>());
    }

    public void addReplacement(String replace, String replacement) {
        this.replacements.put(replace, replacement);
    }

    public void addAllReplacements(Map<String, String> replacements) {
        this.replacements.putAll(replacements);
    }

    public String format() {
        String newString = this.string;
        for (String s : this.replacements.keySet())
            newString = newString.replaceAll(s, this.replacements.get(s));
        return newString;
    }
}
