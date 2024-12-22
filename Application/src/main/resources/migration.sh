#!/bin/bash

MIGRATION_DIR="migration"
DB_HOST="localhost"
DB_USER=""
DB_PASSWORD=""
DB_NAME="dev"

if ! command -v mysql &> /dev/null; then
  echo "The 'mysql' command was not found! Please ensure it is installed and available in the PATH."
  exit 1
fi

SQL_COMMAND="mysql -h $DB_HOST -u $DB_USER --password=$DB_PASSWORD $DB_NAME"

if [ ! -d "$MIGRATION_DIR" ]; then
  echo "The folder $MIGRATION_DIR does not exist!"
  exit 1
fi

echo "Navigating to the migration folder: $MIGRATION_DIR"
cd "$MIGRATION_DIR" || exit 1

echo "Starting migrations..."
for FILE in *.sql; do
  if [ -f "$FILE" ]; then
    echo "Executing: $FILE"
    $SQL_COMMAND < "$FILE"
    if [ $? -ne 0 ]; then
      echo "Error occurred while executing $FILE"
      exit 1
    fi
  fi
done

echo "All migrations completed."
