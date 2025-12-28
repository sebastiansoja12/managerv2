package com.warehouse;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BoundedContextVisitor extends SimpleFileVisitor<Path> implements FileVisitor<Path> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HexagonalArchTest.class);

    private static final String PATH_PATTERN =
            "^(.*\\\\)(.*)(\\\\src.*main.*warehouse\\\\)(.*)(.*)(\\\\domain.*)";

    private static final String ROOT = "com.warehouse";

    private static final Pattern PATTERN = Pattern.compile(PATH_PATTERN);

    private final Set<BoundedContext.BoundedContextBuilder> contexts;

    private final List<String> dependencies;

    private final Set<String> allowedModules;

    private final Path rootPath;

    public BoundedContextVisitor(Path rootPath) throws IOException {
        this.contexts = new HashSet<>();
        this.dependencies = loadModuleDependencies();
        this.allowedModules = loadAllowedModules();
        this.rootPath = rootPath.toAbsolutePath().normalize();
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path path, final BasicFileAttributes attrs) {
        final String absolutePath = normalize(path.toAbsolutePath().toString());
        final Path abs = path.toAbsolutePath().normalize();
        if (abs.getParent() != null && abs.getParent().equals(rootPath)) {
            String dirName = abs.getFileName().toString();
            if (!allowedModules.contains(dirName)) {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        final Matcher matcher = PATTERN.matcher(absolutePath);
        final boolean isDomainDirectory = matcher.find();
        if (isDomainDirectory) {
            final String module = getModule(matcher);
            final String domain = getDomain(matcher);
            final String name = createName(domain);
            final String rootPackage = pack(domain);

            final BoundedContext.BoundedContextBuilder boundedContextBuilder = BoundedContext
                    .builder()
                    .name(name)
                    .rootPackage(rootPackage);

            if (!contexts.contains(boundedContextBuilder)) {
                LOGGER.info("Found bounded context {}", name);
                validateModuleInDependencies(domain, module);
                contexts.add(boundedContextBuilder);
                return FileVisitResult.CONTINUE;
            } else {
                return FileVisitResult.SKIP_SIBLINGS;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    private void validateModuleInDependencies(@NonNull final String domain, @NonNull final String module) {
        final boolean moduleInPom = dependencies
                .stream()
                .anyMatch(line -> line.contains(module));

        if (!moduleInPom) {
            throw new IllegalStateException("Module " + module + " of domain "
                    + domain + " is missing in architecture module (pom.xml)");
        }
    }

    private Set<String> loadAllowedModules() throws IOException {
        Path rootPom = Paths.get(Creator.getManagerDirectory().toString(), "pom.xml");

        if (!Files.exists(rootPom)) {
            throw new IllegalStateException("Root pom.xml not found at " + rootPom);
        }

        final Set<String> modules = new HashSet<>();

        for (String line : Files.readAllLines(rootPom)) {
            line = line.trim();
            if (line.startsWith("<module>") && line.endsWith("</module>")) {
                String module = line
                        .replace("<module>", "")
                        .replace("</module>", "")
                        .trim();

                modules.add(module);
            }
        }

        LOGGER.info("Allowed modules for architecture scan: {}", modules);
        return modules;
    }


    private List<String> loadModuleDependencies() throws IOException {
        final Path architectureTestDirectory = getArchitectureTestDirectory();
        final Path path = Paths.get(architectureTestDirectory.toAbsolutePath().toString(), "pom.xml");
        return Files.readAllLines(path);
    }

    private String getModule(final Matcher matcher) {
        return matcher.group(2);
    }

    private String createName(final String domain) {
        return domain.toUpperCase();
    }

    private String getDomain(final Matcher matcher) {
        return matcher.group(2);
    }

    private String pack(final String domain) {
        return ROOT + "." + domain;
    }

    private static Path getArchitectureTestDirectory() {
        return Paths.get("").toAbsolutePath();
    }

    private static String normalize(final String path) {
        return path.replace("/", "\\");
    }

    public static class Creator {
        public static Stream<BoundedContext> findBoundedContexts() throws IOException {
            final Path path = getManagerDirectory();
            LOGGER.info("Search bounded context in path: {}. ", normalize(path.toString()));

            final BoundedContextVisitor boundedContextVisitor = new BoundedContextVisitor(path);
            Files.walkFileTree(path, boundedContextVisitor);
            return boundedContextVisitor.contexts
                    .stream()
                    .map(BoundedContext.BoundedContextBuilder::build);
        }

        private static Path getManagerDirectory() {
            return getArchitectureTestDirectory().getParent();
        }
    }
}
