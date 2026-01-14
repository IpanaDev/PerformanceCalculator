package me.ipana.lang;

import java.util.List;
import java.util.Map;

public class LanguagePackJson {
    private Map<String, String> entries;
    private List<String> specialChars;

    public LanguagePackJson(Map<String, String> entries, List<String> specialChars) {
        this.entries = entries;
        this.specialChars = specialChars;
    }

    public Map<String, String> entries() {
        return entries;
    }

    public List<String> specialChars() {
        return specialChars;
    }
}