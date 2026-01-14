package me.ipana.lang;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CharMap {
	private final int[] entryTable = new int[0xC00];
	private int numEntries;

	public static CharMap fromChunk(int position, ByteBuffer buffer) {
        buffer.position(position);
        CharMap cm = new CharMap();
        int ch1 = buffer.get();
        int ch2 = buffer.get();
        int ch3 = buffer.get();
        int ch4 = buffer.get();
        int readInt = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
        cm.numEntries = Integer.reverseBytes(readInt); // assuming little endian as in Go
        for (int i = 0; i < cm.entryTable.length; i++) {
            int ch5 = buffer.get();
            int ch6 = buffer.get();
            short readShort = (short)((ch5 << 8) + (ch6));
            cm.entryTable[i] = Short.reverseBytes(readShort) & 0xFFFF; // mask to treat as unsigned
        }
        return cm;
    }
    public String decodeBytes(byte[] b, int start, int end) {
        StringBuilder sb = new StringBuilder();

        for (int i = start; i < end; ) {
            if (b[i] == 0) {
                return sb.toString();
            }
            int curByte = b[i++] & 0xFF; // treat as unsigned

            if (curByte >= 0x80) {
                int histEntry = entryTable[curByte];

                if (histEntry >= 0x80) {
                    curByte = histEntry;
                } else if (histEntry != 0) {
                    int nextByte = b[i++] & 0xFF; // treat as unsigned
                    if (nextByte >= 0x80) {
                        curByte = entryTable[128 * histEntry - 128 + nextByte];
                    }
                } else {
                    throw new RuntimeException("Could not decode string");
                }
            }

            sb.append((char) curByte);
        }

        return sb.toString();
    }
	public String decodeBytes(byte[] b) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < b.length; ) {
			int curByte = b[i++] & 0xFF; // treat as unsigned

			if (curByte >= 0x80) {
				int histEntry = entryTable[curByte];

				if (histEntry >= 0x80) {
					curByte = histEntry;
				} else if (histEntry != 0) {
					int nextByte = b[i++] & 0xFF; // treat as unsigned
					if (nextByte >= 0x80) {
						curByte = entryTable[128 * histEntry - 128 + nextByte];
					}
				} else {
					throw new RuntimeException("Could not decode string");
				}
			}

			sb.append((char) curByte);
		}

		return sb.toString();
	}

	public byte[] encodeString(String str) {
		List<Byte> out = new ArrayList<>();

		for (char c : str.toCharArray()) {
			if (c >= 0xFF80) {
				throw new RuntimeException("FUCK");
			}

			if (c >= 0x80) {
				int curIndex = 128;
				int maxIndex = numEntries;

				if (numEntries > 128) {
					while (curIndex < maxIndex) {
						if (entryTable[curIndex] == c) {
							break;
						}
						curIndex++;
					}
				}

				if (curIndex >= 256) {
					if (curIndex != maxIndex) {
						c = 128;
						int searchIndex = 128;
						boolean update = true;

						while (entryTable[searchIndex] != curIndex >> 7) {
							c++;
							if (c >= 256) {
								update = false;
								break;
							}
							searchIndex++;
						}

						if (update) {
							out.add((byte) c);
							out.add((byte) (curIndex % 128 - 128));
						}
					}

					boolean notFound = c == 256 || curIndex == maxIndex;
					if (notFound) {
						throw new RuntimeException(String.format("could not encode character %s! string: %s", (int)c, str));
					}
				} else {
					out.add((byte) curIndex);
				}
			} else {
				out.add((byte) c);
			}
		}

		byte[] result = new byte[out.size()];
		for (int i = 0; i < out.size(); i++) {
			result[i] = out.get(i);
		}

		return result;
	}

	public int[] entryTable() {
		return entryTable;
	}
}