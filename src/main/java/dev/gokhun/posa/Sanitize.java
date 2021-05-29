package dev.gokhun.posa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "sanitize", description = "Removes comments and orders properties")
public class Sanitize implements Runnable {

	@Option(names = { "--dir", "-d" }, description = "Directory for .properties files")
	String directory;

	@Option(names = { "--walk-subdirs", "-w" }, description = "Should the subdirectories searched for files")
	boolean walkSubdirs;

	@Override
	public void run() {
		List<File> files = this.findFiles(directory, walkSubdirs);
		for (File file : files) {
			info(String.format("Sorting file: %s", file.getPath()));
			final Properties properties = new Properties();
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
				properties.load(br);
			} catch (IOException e) {
				error("An error occured while loading properties", e);
			}
			try (OutputStream outputStream = new FileOutputStream(file)) {
				Properties sortedProperties = new SortedProperties(properties);
				sortedProperties.store(outputStream, null);
			} catch (IOException e) {
				error("An error occured while saving properties", e);
			}
		}
	}

	private List<File> findFiles(String directory, boolean walk) {
		List<File> properties = new ArrayList<>();
		try (Stream<Path> pathStream = walk ? Files.walk(Paths.get(directory), 1) : Files.walk(Paths.get(directory))) {
			pathStream.map(Path::toFile).filter(f -> f.isFile() && f.getPath().endsWith(".properties")).forEach(f -> {
				info(String.format("Discovered file: %s", f.getPath()));
				properties.add(f);
			});
		} catch (IOException e) {
			error("An error occured during properties scan", e);
		}
		return properties;
	}

	private static void info(String info) {
		System.out.println(info);
	}

	private static void error(String error, Throwable t) {
		// More than enough
		System.err.println(error);
		t.printStackTrace();
	}
}
