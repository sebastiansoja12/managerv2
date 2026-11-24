package com.warehouse.mcpkey.infrastructure.adapter.secondary.entity;

import com.warehouse.auth.McpModule;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.mcpkey.domain.enumeration.McpModuleAccess;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;

import java.util.EnumMap;
import java.util.Map;

@Entity(name = "mcpkey.McpAccessEntity")
@Table(name = "mcp_access_policies")
public class McpAccessEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mcp_module_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "module")
    @MapKeyEnumerated(EnumType.STRING)
    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", nullable = false)
    private Map<McpModule, McpModuleAccess> modules = new EnumMap<>(McpModule.class);

    protected McpAccessEntity() {
    }

    public McpAccessEntity(final UserId userId, final boolean enabled,
                           final Map<McpModule, McpModuleAccess> modules) {
        this.userId = userId;
        this.enabled = enabled;
        this.modules.putAll(modules);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Map<McpModule, McpModuleAccess> getModules() {
        return modules;
    }
}
