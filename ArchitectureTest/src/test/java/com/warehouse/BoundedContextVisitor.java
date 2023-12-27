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

    public BoundedContextVisitor() throws IOException {
        this.contexts = new HashSet<>();
        this.dependencies = loadModuleDependencies();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
        final String absolutePath = normalize(path.toAbsolutePath().toString());
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

    private void validateModuleInDependencies(@NonNull String domain, @NonNull String module) {
        final boolean moduleInPom = dependencies
                .stream()
                .anyMatch(line -> line.contains(module));

        if (!moduleInPom) {
            throw new IllegalStateException("Module " + module + " of domain "
                    + domain + " is missing in architecture module (pom.xml)");
        }
    }

    private List<String> loadModuleDependencies() throws IOException {
        final Path architectureTestDirectory = getArchitectureTestDirectory();
        final Path path = Paths.get(architectureTestDirectory.toAbsolutePath().toString(), "pom.xml");
        return Files.readAllLines(path);
    }

    private String getModule(Matcher matcher) {
        return matcher.group(2);
    }

    private String createName(String domain) {
        return domain.toUpperCase();
    }

    private String getDomain(Matcher matcher) {
        return matcher.group(2);
    }

    private String pack(String domain) {
        return ROOT + domain;
    }

    private static Path getArchitectureTestDirectory() {
        return Paths.get("").toAbsolutePath();
    }

    private static String normalize(String path) {
        return path.replace("/", "\\");
    }

    public static class Creator {
        public static Stream<BoundedContext> findBoundedContexts() throws IOException {
            final Path path = getManagerDirectory();
            LOGGER.info("Search bounded context in path: {}. ", normalize(path.toString()));

            final BoundedContextVisitor boundedContextVisitor = new BoundedContextVisitor();
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
