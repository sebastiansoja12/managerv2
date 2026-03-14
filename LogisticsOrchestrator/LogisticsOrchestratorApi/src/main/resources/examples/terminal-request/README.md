# TerminalRequest examples

This directory contains ready-to-use XML payloads for `TerminalRequest`:

- `reject-request.xml`
- `return-request.xml`
- `missed-request.xml`

## Generate JAXB classes from XSD

Run from repository root:

```bash
mvn -pl LogisticsOrchestrator/LogisticsOrchestratorApi -am generate-sources
```

Generated classes will be available in:

`LogisticsOrchestrator/LogisticsOrchestratorApi/target/generated-sources/jaxb/com/warehouse/terminal/jaxb`
