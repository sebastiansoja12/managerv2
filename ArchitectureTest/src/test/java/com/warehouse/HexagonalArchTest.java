package com.warehouse;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;

public class HexagonalArchTest {

    @TestFactory
    public DynamicNode verifyArchRulesForBoundedContexts() throws IOException {
        final Stream<BoundedContext> contexts = BoundedContextVisitor.Creator.findBoundedContexts();

        return dynamicContainer("ArchUnit tests for Manager 2.0",
                contexts.map(BoundedContext::boundedContextTests));
    }
}
