package dev.gokhun.posa;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine.Command;

@TopCommand
@Command(mixinStandardHelpOptions = true, subcommands = { Sanitize.class })
public class Posa {
}
