package lang;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LangParser {
	public static LangFile parseFile(byte[] data) {
		if (LangUtils.isFileEncoded(data)) {
			data = LangUtils.xorDecode(data);
		}

		ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

		long chunkLen = Integer.toUnsignedLong(buffer.getInt(4));
		long entryCount = Integer.toUnsignedLong(buffer.getInt(8));
		List<LangFileEntry> entries = new ArrayList<>();

		long stringsStart = Integer.toUnsignedLong(buffer.getInt(16));

		long offset = chunkLen + 8;
		if (buffer.getInt((int) offset) == 0) {
			offset += Integer.toUnsignedLong(buffer.getInt((int) offset + 4)) + 8;
		}
        int pos = (int)(offset+8);
		CharMap chm = CharMap.fromChunk(pos, buffer);

		offset = 36;
		int entryIdx = 0;
		while (entryIdx < entryCount) {
			long hash = Integer.toUnsignedLong(buffer.getInt((int) offset));
			long location = Integer.toUnsignedLong(buffer.getInt((int) offset + 4));
			//byte[] strBytes = LangUtils.ztString(data, (int) (stringsStart + 8 + location), data.length);
			entries.add(new LangFileEntry(hash, chm.decodeBytes(data, (int) (stringsStart + 8 + location), data.length), location));
			entryIdx++;
			offset += 8;
            if (offset == stringsStart+8) {
                break;
            }
		}

		return new LangFile(entries, chm);
	}
}
