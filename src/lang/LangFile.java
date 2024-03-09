package lang;

import java.util.List;

public class LangFile {
    private List<LangFileEntry> entries;
    private CharMap charMap;

    public LangFile(List<LangFileEntry> entries, CharMap charMap) {
        this.entries = entries;
        this.charMap = charMap;
    }

    public List<LangFileEntry> entries() {
        return entries;
    }

    public CharMap charMap() {
        return charMap;
    }

    public LangFileEntry findEntryByHash(long hash) {
        for (LangFileEntry entry : entries) {
            if (entry.hash() == hash) {
                return entry;
            }
        }
        return null;
    }

    public LangFileEntry findEntryByName(String name) {
        return findEntryByHash(LangUtils.binHash(name));
    }
}
