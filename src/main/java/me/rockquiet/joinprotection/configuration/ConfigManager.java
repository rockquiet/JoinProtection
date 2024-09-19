package me.rockquiet.joinprotection.configuration;

import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import me.rockquiet.joinprotection.JoinProtection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public final class ConfigManager {

    private final JoinProtection plugin;

    private final Path dataFolderPath;
    private final Path configPath;
    private final YamlConfigurationStore<Config> store;
    private Config config;

    public ConfigManager(JoinProtection plugin) {
        this.plugin = plugin;
        this.dataFolderPath = Path.of(plugin.getDataFolder().getPath());
        this.configPath = dataFolderPath.resolve("config.yml");

        if (Files.exists(configPath)) {
            convert1to2();
        }

        final YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder()
                .charset(StandardCharsets.UTF_8)
                .createParentDirectories(true)
                .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
                .header(Config.HEADER)
                .build();
        this.store = new YamlConfigurationStore<>(Config.class, properties);

        update();
    }

    public Config get() {
        if (config == null) {
            load();
        }
        return config;
    }

    public void load() {
        config = store.load(configPath);
    }

    public void save() {
        store.save(config, configPath);
    }

    public void update() {
        config = store.update(configPath);
    }

    public void backup() {
        final Path backupsPath = dataFolderPath.resolve("backups");
        try {
            plugin.getLogger().info("Backing up config into the backups directory...");
            if (!Files.exists(backupsPath)) Files.createDirectory(backupsPath);
            final Path configBackupPath = backupsPath.resolve(new SimpleDateFormat("'config_'yyyyMMdd-HHmm'.yml'").format(new Date()));
            Files.copy(configPath, configBackupPath);
        } catch (IOException e) {
            plugin.getLogger().warning("Unable to backup config: " + e);
        }
    }

    // update config from 1.x to 2
    private void convert1to2() {
        final FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(configPath.toFile());
        if (oldConfig.contains("file-version")) return;

        backup();
        plugin.getLogger().warning("Updating config from 1.x to 2.0");

        // reworked messages
        oldConfig.set("messages", null);

        // improved cancel on move
        final String cancelOnMovePath = "cancel.on-move";
        final String cancelDistancePath = "cancel.distance";

        final boolean cancelOnMoveValue = oldConfig.getBoolean(cancelOnMovePath);
        final double cancelDistanceValue = oldConfig.getDouble(cancelDistancePath);

        oldConfig.set(cancelOnMovePath, Map.of(
                "enabled", cancelOnMoveValue,
                "distance", cancelDistanceValue
        ));
        oldConfig.set(cancelDistancePath, null);

        // _ to -
        renameKey(oldConfig, "modules.disable_damage_by_entities", "modules.disable-damage-by-entities");
        renameKey(oldConfig, "modules.disable_damage_by_blocks", "modules.disable-damage-by-blocks");
        renameKey(oldConfig, "modules.disable_damage", "modules.disable-damage");
        renameKey(oldConfig, "modules.disable_entity_targeting", "modules.disable-entity-targeting.enabled");

        // fix default sound
        final String soundType = "sound.type";
        if (Objects.requireNonNullElse(oldConfig.getString(soundType), "").equalsIgnoreCase("ITEM_SHIELD_BLOCK")) {
            oldConfig.set(soundType, null);
        }

        // renamed protected to protectionActive
        renameKey(oldConfig, "integration.placeholderapi.status.protected", "integration.placeholderapi.status.protection-active");
        renameKey(oldConfig, "integration.miniplaceholders.status.protected", "integration.miniplaceholders.status.protection-active");

        try {
            oldConfig.save(configPath.toFile());
            plugin.getLogger().warning("Updated the config to 2.0");
            plugin.getLogger().warning("The messages section needs to be reconfigured!");
        } catch (IOException e) {
            plugin.getLogger().warning("Unable to save updated config: " + e);
        }
    }

    private void renameKey(FileConfiguration config, String oldPath, String newPath) {
        final Object value = config.get(oldPath);

        config.set(oldPath, null);
        config.set(newPath, value);
    }
}
