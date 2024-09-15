package me.rockquiet.joinprotection;

import com.tchristofferson.configupdater.ConfigUpdater;
import me.rockquiet.joinprotection.commands.JoinProtectionCommand;
import me.rockquiet.joinprotection.commands.TabComplete;
import me.rockquiet.joinprotection.external.LuckPermsContext;
import me.rockquiet.joinprotection.external.MiniPlaceholders;
import me.rockquiet.joinprotection.external.PlaceholderApi;
import me.rockquiet.joinprotection.listeners.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class JoinProtection extends JavaPlugin {

    private boolean isPaper = false;
    private BukkitAudiences audiences;

    @Override
    public void onEnable() {
        // check if server is based on paper
        if (Arrays.stream(Package.getPackages()).anyMatch(aPackage -> aPackage.getName().contains("io.papermc"))) {
            isPaper = true;
        }
        // check if server version is 1.17.1 or below
        String bukkitVersion = Bukkit.getBukkitVersion();
        if (Integer.parseInt(bukkitVersion.split("\\.")[1].replace("-R0", "")) <= 17 && !bukkitVersion.contains("1.17.1")) {
            getLogger().warning("=================================================");
            getLogger().warning(" You are running an incompatible server version.");
            getLogger().warning(" Please consider updating to 1.17.1 or newer.");
            getLogger().warning("=================================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        initConfigFile();

        this.audiences = BukkitAudiences.create(this);
        MessageManager messageManager = new MessageManager(this);

        ProtectionHandler protectionHandler = new ProtectionHandler(this, messageManager);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(protectionHandler, this);
        pluginManager.registerEvents(new JoinListener(this, protectionHandler), this);
        pluginManager.registerEvents(new WorldChangeListener(this, protectionHandler), this);
        pluginManager.registerEvents(new DamageListener(this, messageManager, protectionHandler), this);
        pluginManager.registerEvents(new AttackListener(protectionHandler), this);
        pluginManager.registerEvents(new BlockListener(protectionHandler), this);
        pluginManager.registerEvents(new ItemDropListener(protectionHandler), this);
        if (isPaper()) {
            pluginManager.registerEvents(new PaperMoveListener(this, protectionHandler), this);
            pluginManager.registerEvents(new PaperItemPickupListener(protectionHandler), this);
        } else {
            pluginManager.registerEvents(new MoveListener(this, protectionHandler), this);
            pluginManager.registerEvents(new ItemPickupListener(protectionHandler), this);
        }

        getCommand("joinprotection").setExecutor(new JoinProtectionCommand(this, messageManager, protectionHandler));
        getCommand("joinprotection").setTabCompleter(new TabComplete());

        // PlaceholderAPI hook
        if (getConfig().getBoolean("integration.placeholderapi.enabled") && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderApi(this, protectionHandler).register();
            getLogger().info("Enabled PlaceholderAPI integration");
        }
        // MiniPlaceholders hook
        if (getConfig().getBoolean("integration.miniplaceholders.enabled") && Bukkit.getPluginManager().getPlugin("MiniPlaceholders") != null) {
            new MiniPlaceholders(this, protectionHandler).registerExpansion();
            getLogger().info("Enabled MiniPlaceholders integration");
        }
        // LuckPerms hook
        if (getConfig().getBoolean("integration.luckperms.enabled") && Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            new LuckPermsContext(protectionHandler).register();
            getLogger().info("Registered LuckPerms context");
        }

        boolean updateChecks = getConfig().getBoolean("plugin.update-checks");

        Metrics metrics = new Metrics(this, 19289);
        metrics.addCustomChart(new SimplePie("update_checks", () -> String.valueOf(updateChecks)));

        if (updateChecks) {
            new UpdateChecker(this);
        }
    }

    private void initConfigFile() {
        final Path dataFolder = Path.of(getDataFolder().getPath());
        final Path configPath = dataFolder.resolve("config.yml");
        // backup pre 2.0 config // TODO proper auto migration
        if (Files.exists(configPath)) {
            final FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(configPath.toFile());

            if (!oldConfig.contains("file-version")) {
                getLogger().warning("==============================================");
                getLogger().warning(" The structure of the config.yml has changed!");
                getLogger().warning(" You will need to manually reconfigure it.");
                getLogger().warning("==============================================");

                final Path backupsPath = dataFolder.resolve("backups");
                try {
                    getLogger().info("Moving old config into the backups directory...");
                    if (!Files.exists(backupsPath)) Files.createDirectory(backupsPath);
                    // move old config
                    final Path configBackupPath = backupsPath.resolve(new SimpleDateFormat("'config_'yyyyMMdd-HHmm'.yml'").format(new Date()));
                    Files.move(configPath, configBackupPath);
                } catch (IOException e) {
                    getLogger().warning("Unable to backup old config: " + e);
                }
            }
        }

        // save config / update small changes
        saveDefaultConfig();
        try {
            ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().warning("Unable to update the config.yml: " + e);
        }
        saveConfig();
    }

    @Override
    public void onDisable() {
        if (this.audiences != null) {
            this.audiences.close();
            this.audiences = null;
        }
    }

    public @NonNull BukkitAudiences adventure() {
        if (this.audiences == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.audiences;
    }

    public boolean isPaper() {
        return isPaper;
    }
}