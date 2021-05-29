package dev.gokhun.posa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import picocli.CommandLine;

@QuarkusTest
public class PosaTest {

	@Test
	public void blackBox() throws IOException, NoSuchAlgorithmException {
		Posa posa = new Posa();
		CommandLine cmd = new CommandLine(posa);

		StringWriter sw = new StringWriter();
		cmd.setOut(new PrintWriter(sw));

		Files.copy(Paths.get("src/test/resources/ugly.properties"), Paths.get("/tmp/ugly.properties"),
				StandardCopyOption.REPLACE_EXISTING);

		int exitCode = cmd.execute("sanitize", "--dir", "/tmp/ugly.properties");
		assertEquals(0, exitCode);

		String ugly = this.getSha(Paths.get("/tmp/ugly.properties"));
		String pretty = this.getSha(Paths.get("src/test/resources/pretty.properties"));
		assertEquals(ugly, pretty);
	}

	protected String getSha(Path path) throws NoSuchAlgorithmException, IOException {
		final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
		final byte[] bytes = digest.digest(Files.readAllBytes(path));
		StringBuilder hexString = new StringBuilder(2 * bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xff & bytes[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
