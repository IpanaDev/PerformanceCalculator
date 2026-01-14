package me.ipana.lang;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LangUnpacker {
    private static final Gson GSON = new Gson();

    public static LanguagePackJson read(Path inputPath, String lang) throws Exception {
        Path labelsPath = inputPath.resolve("Labels_Global.bin");
        byte[] labelsData = Files.readAllBytes(labelsPath);

        LangFile labelsPack = LangParser.parseFile(labelsData);
        Map<Long, String> labelMap = new HashMap<>();

        for (LangFileEntry e : labelsPack.entries()) {
            labelMap.put(e.hash(), e.string());
        }
        Path filePath = new File(inputPath.toFile(), lang+"_Global.bin").toPath();
        byte[] fileData = Files.readAllBytes(filePath);
        LangFile langFile = LangParser.parseFile(fileData);
        LanguagePackJson langJson = new LanguagePackJson(new HashMap<>(), new ArrayList<>());
        for (LangFileEntry e : langFile.entries()) {
            langJson.entries().put(labelMap.get(e.hash()), e.string());
        }

        for (int c : langFile.charMap().entryTable()) {
            if (c >= 0x80) {
                langJson.specialChars().add(String.valueOf((char) c));
            }
        }
        return langJson;
    }

	public static void unpack(Path inputPath, Path outputPath) throws Exception {
		if (!Files.exists(outputPath)) {
			Files.createDirectories(outputPath);
		}

		Path labelsPath = inputPath.resolve("Labels_Global.bin");
		byte[] labelsData = Files.readAllBytes(labelsPath);

		LangFile labelsPack = LangParser.parseFile(labelsData);
		Map<Long, String> labelMap = new HashMap<>();

		for (LangFileEntry e : labelsPack.entries()) {
			labelMap.put(e.hash(), e.string());
		}

		// get all files ending with _Global.bin
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputPath, "*_Global.bin")) {
			for (Path filePath : stream) {
				String fileName = filePath.getFileName().toString();

				if (fileName.equals("Largest_Global.bin")) {
					continue;
				}

				byte[] fileData = Files.readAllBytes(filePath);
				LangFile langFile = LangParser.parseFile(fileData);
				String cleanName = removeExtension(fileName);
				Path jsonPath = Paths.get(outputPath.toString(), cleanName + ".json");

				LanguagePackJson langJson = new LanguagePackJson(new HashMap<>(), new ArrayList<>());
				for (LangFileEntry e : langFile.entries()) {
					langJson.entries().put(labelMap.get(e.hash()), e.string());
				}

				for (int c : langFile.charMap().entryTable()) {
					if (c >= 0x80) {
						langJson.specialChars().add(String.valueOf((char) c));
					}
				}

				saveLanguageJson(jsonPath, langJson);
			}
		}
	}

	public static String removeExtension(String fileName) {
		int lastIndexOfPeriod = fileName.lastIndexOf('.');
		if (lastIndexOfPeriod == -1) {
			return fileName; // No extension found
		}

		if (lastIndexOfPeriod == 0) {
			return fileName; // Filename starts with a period, so it's likely a hidden file without an extension
		}

		// Return the filename without the extension
		return fileName.substring(0, lastIndexOfPeriod);
	}

	private static void saveLanguageJson(Path jsonPath, LanguagePackJson langJson) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(jsonPath)) {
			GSON.toJson(langJson, writer);
		}
	}
}
