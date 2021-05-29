package dev.gokhun.posa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Stream;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "sanitize")
public class Sanitize implements Runnable {

	Logger logger = Logger.getGlobal();

	@Option(names = { "--keep-comments", "-k" }, description = "Does not remove comments starting with # character")
	boolean keepComments;

	@Option(names = { "--preserve-order", "-p" }, description = "Preserves order of properties")
	boolean preserveOrder;

	@Option(names = { "--disable-tree-walk", "-t" }, description = "Disables subdirectory search")
	boolean disableTreeWalk;

	@Option(names = { "--dir", "-d" }, description = "Directory for .properties files")
	String directory;

	@Override
	public void run() {
		try {
			final List<String> filePaths = new ArrayList<>();
			try (final Stream<Path> pathStream = Files.walk(Paths.get(directory))) {
				pathStream.filter(p -> p.toFile().isFile()).filter(p -> {
					if (p.toString().endsWith(".properties")) {
						logger.info(String.format("Discovered file: %s", p));
						return true;
					}
					return false;
				}).forEach(file -> filePaths.add(file.toString()));
			}
			for (final String file : filePaths) {
				logger.info(String.format("Sorting file: %s", file));
				final Properties properties = new Properties();
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
					properties.load(br);
				}
				try (OutputStream outputStream = new FileOutputStream(file)) {
					Properties sortedProperties = new PropertiesAdapter(properties);
					sortedProperties.store(outputStream, null);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
