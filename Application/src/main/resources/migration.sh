#!/bin/bash

MIGRATION_DIR="migration"
DB_HOST="localhost"
DB_USER=""
DB_PASSWORD=""
DB_NAME="dev"

# Sprawdzenie, czy mysql jest dostępny
if ! command -v mysql &> /dev/null; then
  echo "Polecenie 'mysql' nie zostało znalezione! Upewnij się, że jest zainstalowane i znajduje się w PATH."
  exit 1
fi

# Definicja komendy SQL
SQL_COMMAND="mysql -h $DB_HOST -u $DB_USER --password=$DB_PASSWORD $DB_NAME"

# Sprawdzenie, czy folder migration istnieje
if [ ! -d "$MIGRATION_DIR" ]; then
  echo "Folder $MIGRATION_DIR nie istnieje!"
  exit 1
fi

echo "Przechodzę do folderu migration: $MIGRATION_DIR"
cd "$MIGRATION_DIR" || exit 1

echo "Rozpoczynam migracje..."
for FILE in *.sql; do
  if [ -f "$FILE" ]; then
    echo "Wykonuję: $FILE"
    $SQL_COMMAND < "$FILE"
    if [ $? -ne 0 ]; then
      echo "Błąd podczas wykonywania $FILE"
      exit 1
    fi
  fi
done

echo "Wszystkie migracje zakończone."
