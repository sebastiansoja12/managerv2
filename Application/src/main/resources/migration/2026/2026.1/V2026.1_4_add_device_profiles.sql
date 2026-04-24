CREATE TABLE IF NOT EXISTS terminals
(
    device_id                BIGINT       NOT NULL PRIMARY KEY,
    external_device_id       VARCHAR(255) NOT NULL,
    device_type              VARCHAR(32)  NOT NULL,
    status                   VARCHAR(32)  NOT NULL,
    created_at               DATETIME(6)  NOT NULL,
    updated_at               DATETIME(6)  NOT NULL,
    user_id                  BIGINT       NULL,
    department_code          VARCHAR(255) NULL,
    version                  VARCHAR(255) NULL,
    last_update              DATETIME(6)  NULL,
    active                   BOOLEAN      NULL,

    -- Identity block
    hardware_uuid            VARCHAR(36)  NULL,
    serial_number            VARCHAR(255) NULL,
    imei                     VARCHAR(255) NULL,
    mac_address              VARCHAR(255) NULL,
    asset_tag                VARCHAR(255) NULL,
    barcode                  VARCHAR(255) NULL,
    external_system_id       VARCHAR(255) NULL,
    mdm_device_id            VARCHAR(255) NULL,

    -- Hardware block
    manufacturer             VARCHAR(255) NULL,
    model                    VARCHAR(255) NULL,
    product_name             VARCHAR(255) NULL,
    cpu                      VARCHAR(255) NULL,
    ram_mb                   INT          NULL,
    storage_mb               INT          NULL,
    screen_resolution        VARCHAR(255) NULL,
    has_scanner              BOOLEAN      NULL,
    has_camera               BOOLEAN      NULL,
    has_nfc                  BOOLEAN      NULL,
    has_gps                  BOOLEAN      NULL,
    ruggedized               BOOLEAN      NULL,

    -- Software block
    os_name                  VARCHAR(255) NULL,
    os_version               VARCHAR(255) NULL,
    firmware_version         VARCHAR(255) NULL,
    kernel_version           VARCHAR(255) NULL,
    bootloader_version       VARCHAR(255) NULL,
    app_version              VARCHAR(255) NULL,
    build_number             VARCHAR(255) NULL,
    rooted                   BOOLEAN      NULL,
    developer_mode_enabled   BOOLEAN      NULL,

    -- Network block
    ip_address               VARCHAR(255) NULL,
    public_ip_address        VARCHAR(255) NULL,
    network_type             VARCHAR(32)  NULL,
    carrier                  VARCHAR(255) NULL,
    sim_serial               VARCHAR(255) NULL,
    roaming                  BOOLEAN      NULL,
    vpn_connected            BOOLEAN      NULL,
    wifi_ssid                VARCHAR(255) NULL,
    bluetooth_mac            VARCHAR(255) NULL,

    -- Security block
    encrypted                BOOLEAN      NULL,
    secure_boot_enabled      BOOLEAN      NULL,
    tamper_detected          BOOLEAN      NULL,
    screen_lock_enabled      BOOLEAN      NULL,
    biometric_enabled        BOOLEAN      NULL,
    compromised              BOOLEAN      NULL,
    failed_login_attempts    INT          NULL,
    last_security_scan_at    DATETIME(6)  NULL,
    security_policy_version  VARCHAR(255) NULL,
    certificate_fingerprint  VARCHAR(255) NULL,

    -- Location block
    latitude                 DOUBLE       NULL,
    longitude                DOUBLE       NULL,
    altitude                 DOUBLE       NULL,
    accuracy_meters          FLOAT        NULL,
    last_known_address       VARCHAR(255) NULL,
    geo_zone                 VARCHAR(255) NULL,
    gps_enabled              BOOLEAN      NULL,
    last_location_update_at  DATETIME(6)  NULL,

    -- Ownership block
    previous_user_id         BIGINT       NULL,
    team_id                  BIGINT       NULL,
    vehicle_id               BIGINT       NULL,
    assigned_role            VARCHAR(255) NULL,
    assigned_at              DATETIME(6)  NULL,
    unassigned_at            DATETIME(6)  NULL
);

CREATE TABLE IF NOT EXISTS scanners
(
    device_id                BIGINT       NOT NULL PRIMARY KEY,
    external_device_id       VARCHAR(255) NOT NULL,
    device_type              VARCHAR(32)  NOT NULL,
    status                   VARCHAR(32)  NOT NULL,
    created_at               DATETIME(6)  NOT NULL,
    updated_at               DATETIME(6)  NOT NULL,
    user_id                  BIGINT       NULL,
    department_code          VARCHAR(255) NULL,

    -- Identity block
    hardware_uuid            VARCHAR(36)  NULL,
    serial_number            VARCHAR(255) NULL,
    imei                     VARCHAR(255) NULL,
    mac_address              VARCHAR(255) NULL,
    asset_tag                VARCHAR(255) NULL,
    barcode                  VARCHAR(255) NULL,
    external_system_id       VARCHAR(255) NULL,
    mdm_device_id            VARCHAR(255) NULL,

    -- Hardware block
    manufacturer             VARCHAR(255) NULL,
    model                    VARCHAR(255) NULL,
    product_name             VARCHAR(255) NULL,
    cpu                      VARCHAR(255) NULL,
    ram_mb                   INT          NULL,
    storage_mb               INT          NULL,
    screen_resolution        VARCHAR(255) NULL,
    has_scanner              BOOLEAN      NULL,
    has_camera               BOOLEAN      NULL,
    has_nfc                  BOOLEAN      NULL,
    has_gps                  BOOLEAN      NULL,
    ruggedized               BOOLEAN      NULL,

    -- Network block
    ip_address               VARCHAR(255) NULL,
    public_ip_address        VARCHAR(255) NULL,
    network_type             VARCHAR(32)  NULL,
    carrier                  VARCHAR(255) NULL,
    sim_serial               VARCHAR(255) NULL,
    roaming                  BOOLEAN      NULL,
    vpn_connected            BOOLEAN      NULL,
    wifi_ssid                VARCHAR(255) NULL,
    bluetooth_mac            VARCHAR(255) NULL,

    -- Ownership block
    previous_user_id         BIGINT       NULL,
    team_id                  BIGINT       NULL,
    vehicle_id               BIGINT       NULL,
    assigned_role            VARCHAR(255) NULL,
    assigned_at              DATETIME(6)  NULL,
    unassigned_at            DATETIME(6)  NULL,

    -- Scanner specific
    scan_type                VARCHAR(32)  NULL,
    scanner_type             VARCHAR(32)  NULL
);

CREATE TABLE IF NOT EXISTS mobiles
(
    device_id                BIGINT       NOT NULL PRIMARY KEY,
    external_device_id       VARCHAR(255) NOT NULL,
    device_type              VARCHAR(32)  NOT NULL,
    status                   VARCHAR(32)  NOT NULL,
    created_at               DATETIME(6)  NOT NULL,
    updated_at               DATETIME(6)  NOT NULL,
    user_id                  BIGINT       NULL,
    department_code          VARCHAR(255) NULL,
    version                  VARCHAR(255) NULL,
    last_update              DATETIME(6)  NULL,
    active                   BOOLEAN      NULL,

    -- Identity block
    hardware_uuid            VARCHAR(36)  NULL,
    serial_number            VARCHAR(255) NULL,
    imei                     VARCHAR(255) NULL,
    mac_address              VARCHAR(255) NULL,
    asset_tag                VARCHAR(255) NULL,
    barcode                  VARCHAR(255) NULL,
    external_system_id       VARCHAR(255) NULL,
    mdm_device_id            VARCHAR(255) NULL,

    -- Hardware block
    manufacturer             VARCHAR(255) NULL,
    model                    VARCHAR(255) NULL,
    product_name             VARCHAR(255) NULL,
    cpu                      VARCHAR(255) NULL,
    ram_mb                   INT          NULL,
    storage_mb               INT          NULL,
    screen_resolution        VARCHAR(255) NULL,
    has_scanner              BOOLEAN      NULL,
    has_camera               BOOLEAN      NULL,
    has_nfc                  BOOLEAN      NULL,
    has_gps                  BOOLEAN      NULL,
    ruggedized               BOOLEAN      NULL,

    -- Software block
    os_name                  VARCHAR(255) NULL,
    os_version               VARCHAR(255) NULL,
    firmware_version         VARCHAR(255) NULL,
    kernel_version           VARCHAR(255) NULL,
    bootloader_version       VARCHAR(255) NULL,
    app_version              VARCHAR(255) NULL,
    build_number             VARCHAR(255) NULL,
    rooted                   BOOLEAN      NULL,
    developer_mode_enabled   BOOLEAN      NULL,

    -- Network block
    ip_address               VARCHAR(255) NULL,
    public_ip_address        VARCHAR(255) NULL,
    network_type             VARCHAR(32)  NULL,
    carrier                  VARCHAR(255) NULL,
    sim_serial               VARCHAR(255) NULL,
    roaming                  BOOLEAN      NULL,
    vpn_connected            BOOLEAN      NULL,
    wifi_ssid                VARCHAR(255) NULL,
    bluetooth_mac            VARCHAR(255) NULL,

    -- Security block
    encrypted                BOOLEAN      NULL,
    secure_boot_enabled      BOOLEAN      NULL,
    tamper_detected          BOOLEAN      NULL,
    screen_lock_enabled      BOOLEAN      NULL,
    biometric_enabled        BOOLEAN      NULL,
    compromised              BOOLEAN      NULL,
    failed_login_attempts    INT          NULL,
    last_security_scan_at    DATETIME(6)  NULL,
    security_policy_version  VARCHAR(255) NULL,
    certificate_fingerprint  VARCHAR(255) NULL,

    -- Location block
    latitude                 DOUBLE       NULL,
    longitude                DOUBLE       NULL,
    altitude                 DOUBLE       NULL,
    accuracy_meters          FLOAT        NULL,
    last_known_address       VARCHAR(255) NULL,
    geo_zone                 VARCHAR(255) NULL,
    gps_enabled              BOOLEAN      NULL,
    last_location_update_at  DATETIME(6)  NULL,

    -- Ownership block
    previous_user_id         BIGINT       NULL,
    team_id                  BIGINT       NULL,
    vehicle_id               BIGINT       NULL,
    assigned_role            VARCHAR(255) NULL,
    assigned_at              DATETIME(6)  NULL,
    unassigned_at            DATETIME(6)  NULL
);

CREATE TABLE IF NOT EXISTS mobile_installed_apps
(
    device_id BIGINT       NOT NULL,
    app_name  VARCHAR(255) NOT NULL,
    PRIMARY KEY (device_id, app_name),
    CONSTRAINT fk_mobile_installed_apps_device
        FOREIGN KEY (device_id) REFERENCES mobiles (device_id)
);

-- Migrate terminal records from legacy unified device table.
INSERT INTO terminals (
    device_id,
    external_device_id,
    device_type,
    status,
    created_at,
    updated_at,
    user_id,
    department_code,
    version,
    last_update,
    active,
    app_version,
    assigned_role,
    assigned_at
)
SELECT d.device_id,
       CONCAT('legacy-device-', d.device_id),
       'TERMINAL',
       CASE WHEN d.active = 1 THEN 'ACTIVE' ELSE 'BLOCKED' END,
       COALESCE(d.last_update, NOW(6)),
       COALESCE(d.last_update, NOW(6)),
       u.user_id,
       d.department_code,
       d.version,
       d.last_update,
       d.active,
       d.version,
       'LEGACY_MIGRATION',
       COALESCE(d.last_update, NOW(6))
FROM device d
         LEFT JOIN users u ON u.username = d.username
WHERE d.device_type = 'TERMINAL'
  AND NOT EXISTS (SELECT 1 FROM terminals t WHERE t.device_id = d.device_id);

-- Device version stores new types (SCANNER/MOBILE), so keep it as string-based type.
ALTER TABLE device_version
    MODIFY COLUMN device_type VARCHAR(50) NOT NULL;

-- Switch legacy FK from device_pair(device_id)->device(device_id) to terminal table or remove when absent.
SET @legacy_pair_fk := (
    SELECT kcu.constraint_name
    FROM information_schema.key_column_usage kcu
    WHERE kcu.table_schema = DATABASE()
      AND kcu.table_name = 'device_pair'
      AND kcu.column_name = 'device_id'
      AND kcu.referenced_table_name = 'device'
    LIMIT 1
);

SET @drop_legacy_pair_fk_sql := IF(
        @legacy_pair_fk IS NULL,
        'SELECT 1',
        CONCAT('ALTER TABLE device_pair DROP FOREIGN KEY ', @legacy_pair_fk)
                               );

PREPARE stmt_drop_legacy_pair_fk FROM @drop_legacy_pair_fk_sql;
EXECUTE stmt_drop_legacy_pair_fk;
DEALLOCATE PREPARE stmt_drop_legacy_pair_fk;

SET @has_terminal_pair_fk := (
    SELECT COUNT(1)
    FROM information_schema.key_column_usage kcu
    WHERE kcu.table_schema = DATABASE()
      AND kcu.table_name = 'device_pair'
      AND kcu.column_name = 'device_id'
      AND kcu.referenced_table_name = 'terminals'
);

SET @orphan_pair_rows := (
    SELECT COUNT(1)
    FROM device_pair dp
    WHERE dp.device_id IS NOT NULL
      AND NOT EXISTS (SELECT 1 FROM terminals t WHERE t.device_id = dp.device_id)
);

SET @add_terminal_pair_fk_sql := IF(
        @has_terminal_pair_fk = 0 AND @orphan_pair_rows = 0,
        'ALTER TABLE device_pair ADD CONSTRAINT fk_device_pair_terminal_device_id FOREIGN KEY (device_id) REFERENCES terminals(device_id)',
        'SELECT 1'
                                );

PREPARE stmt_add_terminal_pair_fk FROM @add_terminal_pair_fk_sql;
EXECUTE stmt_add_terminal_pair_fk;
DEALLOCATE PREPARE stmt_add_terminal_pair_fk;

SET @has_pair_key_idx := (
    SELECT COUNT(1)
    FROM information_schema.statistics s
    WHERE s.table_schema = DATABASE()
      AND s.table_name = 'device_pair'
      AND s.index_name = 'idx_device_pair_pair_key'
);

SET @create_pair_key_idx_sql := IF(
        @has_pair_key_idx = 0,
        'CREATE INDEX idx_device_pair_pair_key ON device_pair(pair_key)',
        'SELECT 1'
                              );

PREPARE stmt_pair_key_idx FROM @create_pair_key_idx_sql;
EXECUTE stmt_pair_key_idx;
DEALLOCATE PREPARE stmt_pair_key_idx;
