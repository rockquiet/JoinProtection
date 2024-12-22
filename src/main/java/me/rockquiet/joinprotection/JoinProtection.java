package me.rockquiet.joinprotection;

import me.rockquiet.joinprotection.commands.JoinProtectionCommand;
import me.rockquiet.joinprotection.commands.TabComplete;
import me.rockquiet.joinprotection.configuration.Config;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.external.LuckPermsContext;
import me.rockquiet.joinprotection.external.MiniPlaceholders;
import me.rockquiet.joinprotection.external.PlaceholderApi;
import me.rockquiet.joinprotection.listeners.*;
import me.rockquiet.joinprotection.scheduler.PlatformScheduler;
import me.rockquiet.joinprotection.scheduler.folia.FoliaScheduler;
import me.rockquiet.joinprotection.scheduler.paper.PaperScheduler;
import me.rockquiet.joinprotection.scheduler.spigot.SpigotScheduler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public class JoinProtection extends JavaPlugin {

    private ConfigManager configManager;
    private PlatformScheduler scheduler;

    private boolean isPaper;
    private BukkitAudiences audiences;

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onEnable() {
        // check if server is based on paper
        if (Arrays.stream(Package.getPackages()).anyMatch(aPackage -> aPackage.getName().contains("io.papermc"))) {
            isPaper = true;
        }

        this.scheduler = createPlatformScheduler();
        this.configManager = new ConfigManager(this);

        this.audiences = BukkitAudiences.create(this);
        MessageManager messageManager = new MessageManager(this);

        ProtectionHandler protectionHandler = new ProtectionHandler(this, messageManager);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(protectionHandler, this);
        pluginManager.registerEvents(new JoinListener(configManager, protectionHandler), this);
        pluginManager.registerEvents(new WorldChangeListener(configManager, protectionHandler), this);
        pluginManager.registerEvents(new DamageListener(this, messageManager, protectionHandler), this);
        pluginManager.registerEvents(new AttackListener(configManager, protectionHandler), this);
        pluginManager.registerEvents(new BlockListener(configManager, protectionHandler), this);
        pluginManager.registerEvents(new ItemDropListener(configManager, protectionHandler), this);
        if (isPaper()) {
            pluginManager.registerEvents(new PaperMoveListener(configManager, protectionHandler), this);
            pluginManager.registerEvents(new PaperItemPickupListener(configManager, protectionHandler), this);
        } else {
            pluginManager.registerEvents(new MoveListener(configManager, protectionHandler), this);
            pluginManager.registerEvents(new ItemPickupListener(configManager, protectionHandler), this);
        }

        getCommand("joinprotection").setExecutor(new JoinProtectionCommand(this, messageManager, protectionHandler));
        getCommand("joinprotection").setTabCompleter(new TabComplete(configManager));

        // PlaceholderAPI hook
        if (config().integration.placeholderapi.enabled && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderApi(this, protectionHandler).register();
            getLogger().info("Enabled PlaceholderAPI integration");
        }
        // MiniPlaceholders hook
        if (config().integration.miniplaceholders.enabled && Bukkit.getPluginManager().getPlugin("MiniPlaceholders") != null) {
            new MiniPlaceholders(this, protectionHandler).registerExpansion();
            getLogger().info("Enabled MiniPlaceholders integration");
        }
        // LuckPerms hook
        if (config().integration.luckperms.enabled && Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            new LuckPermsContext(protectionHandler).register();
            getLogger().info("Registered LuckPerms context");
        }

        boolean updateChecks = config().plugin.updateChecks;

        Metrics metrics = new Metrics(this, 19289);
        metrics.addCustomChart(new SimplePie("update_checks", () -> String.valueOf(updateChecks)));

        if (updateChecks) {
            new UpdateChecker(this);
        }
    }

    @Override
    public void onDisable() {
        if (this.audiences != null) {
            this.audiences.close();
            this.audiences = null;
        }
    }

    public ConfigManager configManager() {
        return configManager;
    }

    public Config config() {
        return configManager.get();
    }

    private PlatformScheduler createPlatformScheduler() {
        final boolean isRegionSchedulerAvailable = hasClass("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
        if (hasClass("io.papermc.paper.threadedregions.RegionizedServer") && isRegionSchedulerAvailable) {
            return new FoliaScheduler(this);
        } else if (isPaper && isRegionSchedulerAvailable) {
            return new PaperScheduler(this);
        } else {
            return new SpigotScheduler(this);
        }
    }

    public PlatformScheduler getScheduler() {
        return scheduler;
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