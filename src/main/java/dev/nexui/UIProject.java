
package dev.nexui;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public final class UIProject {

    private final String id;
    private final String name;
    private final int schemaVersion;
    private final List<?> components;

    private UIProject(
            String id,
            String name,
            int schemaVersion,
            List<?> components
    ) {
        this.id = id;
        this.name = name;
        this.schemaVersion = schemaVersion;
        this.components = components;
    }

    public static UIProject load(
            JavaPlugin plugin,
            String projectId
    ) {
        if (projectId == null
                || !projectId.matches("[a-zA-Z0-9_-]+")) {
            throw new IllegalArgumentException(
                    "Invalid project ID"
            );
        }

        File projectsFolder = new File(
                plugin.getDataFolder(),
                "projects"
        );

        File file = new File(
                projectsFolder,
                projectId + ".yml"
        );

        if (!file.isFile()) {
            throw new IllegalArgumentException(
                    "Project not found: " + projectId
                    + " | Path: " + file.getAbsolutePath()
            );
        }

        YamlConfiguration yaml =
                YamlConfiguration.loadConfiguration(file);

        String format = yaml.getString("format");

        if (!"nexui".equals(format)) {
            throw new IllegalArgumentException(
                    "Invalid NEXUI project format"
            );
        }

        int schemaVersion =
                yaml.getInt("schemaVersion", -1);

        if (schemaVersion != 1) {
            throw new IllegalArgumentException(
                    "Unsupported schema version: "
                    + schemaVersion
            );
        }

        String id = yaml.getString("project.id", projectId);
        String name = yaml.getString(
                "project.name",
                "Untitled UI"
        );

        List<?> components = yaml.getList("components");

        if (components == null) {
            throw new IllegalArgumentException(
                    "Missing components list"
            );
        }

        return new UIProject(
                id,
                name,
                schemaVersion,
                components
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public List<?> getComponents() {
        return components;
    }
}
