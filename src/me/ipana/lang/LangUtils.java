package me.ipana.lang;

public class LangUtils {
	public static byte[] xorDecode(byte[] b) {
		if (b.length < 1) {
			return new byte[0];
		}

		byte[] out = new byte[b.length];
		int i = b.length - 1;

		while (i > 0) {
			out[i] = (byte) (b[i] ^ b[i - 1]);
			i--;
		}

		out[0] = (byte) (b[0] ^ 0x6B);

		return out;
	}

	public static byte[] xorEncode(byte[] b) {
		if (b.length < 1) {
			return new byte[0];
		}

		byte[] out = new byte[b.length];
		out[0] = (byte) (b[0] ^ 0x6B);

		for (int i = 1; i < b.length; i++) {
			out[i] = (byte) (b[i] ^ out[i - 1]);
		}

		return out;
	}


	public static int binHash(String s) {
		if (s.isEmpty()) {
			return 0;
		}

		int hash = s.charAt(0) - 33;
		for (int i = 1; i < s.length(); i++) {
			hash *= 33;
			hash += s.charAt(i);
		}

		return hash;
	}
	public static byte[] ztString(byte[] b, int start, int end) {
		for (int i = start; i < end; i++) {
			if (b[i] == 0) {
                if (i == start) {
                    return new byte[0];
                }
                int arrayLength = i - start;
				byte[] sb = new byte[arrayLength];
				System.arraycopy(b, start, sb, 0, arrayLength);
				return sb;
			}
		}
		return new byte[0];
	}

	public static boolean isFileEncoded(byte[] b) {
		if (b.length < 26) {
			return false;
		}

		byte[] globalBytes = {'G', 'l', 'o', 'b', 'a', 'l'};
		for (int i = 0; i < globalBytes.length; i++) {
			if (b[i + 20] != globalBytes[i]) {
				return true;
			}
		}

		return false;
	}
}
