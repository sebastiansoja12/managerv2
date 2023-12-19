#!/usr/bin/env bash

VERSION="version/2023_13"
export VERSION

mvn liquibase:diff \
-Dliquibase.diffChangeLogFile="src/${VERSION}/diff.sql" \
-Dliquibase.referenceUrl="jdbc:mysql://localhost:3306/dev_local" \
-Dliquibase.referenceUsername="root" \
-Dliquibase.referencePassword="RootROOT123!" \
-Dliquibase.referenceDriver="com.mysql.cj.jdbc.Driver" \
-Dliquibase.diffTypes=tables,indexes,views