package lang;

public class LangFileEntry {
    private long hash, offset;
    private String string;

    public LangFileEntry(long hash, String string, long offset) {
        this.hash = hash;
        this.string = string;
        this.offset = offset;
    }

    public long hash() {
        return hash;
    }

    public long offset() {
        return offset;
    }

    public String string() {
        return string;
    }
}
