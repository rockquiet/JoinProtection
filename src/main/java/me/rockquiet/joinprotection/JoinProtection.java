package me.rockquiet.joinprotection;

import com.tchristofferson.configupdater.ConfigUpdater;
import me.rockquiet.joinprotection.commands.JoinProtectionCommand;
import me.rockquiet.joinprotection.commands.TabComplete;
import me.rockquiet.joinprotection.external.LuckPermsContext;
import me.rockquiet.joinprotection.external.MiniPlaceholders;
import me.rockquiet.joinprotection.external.PlaceholderApi;
import me.rockquiet.joinprotection.listeners.*;
import net.luckperms.api.LuckPerms;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JoinProtection extends JavaPlugin {

    @Override
    public void onEnable() {
        // check if server is based on paper
        if (Arrays.stream(Package.getPackages()).noneMatch(aPackage -> aPackage.getName().contains("io.papermc"))) {
            getLogger().warning("======================================================");
            getLogger().warning(" You are running incompatible server software.");
            getLogger().warning(" Please consider using Paper as your server software.");
            getLogger().warning("======================================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // check if server version is 1.18.1 or below
        String bukkitVersion = Bukkit.getBukkitVersion();
        if (Integer.parseInt(bukkitVersion.split("\\.")[1].replace("-R0", "")) <= 18 && !bukkitVersion.contains("1.18.2")) {
            getLogger().warning("=================================================");
            getLogger().warning(" You are running an incompatible server version.");
            getLogger().warning(" Please consider updating to 1.18.2 or newer.");
            getLogger().warning("=================================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        try {
            ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().warning("Unable to update the config.yml: " + e);
        }
        saveConfig();

        MessageManager messageManager = new MessageManager();
        ProtectionHandler protectionHandler = new ProtectionHandler(this, messageManager);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(protectionHandler, this);
        pluginManager.registerEvents(new JoinListener(this, protectionHandler), this);
        pluginManager.registerEvents(new DamageListener(this, messageManager, protectionHandler), this);
        pluginManager.registerEvents(new AttackListener(this, protectionHandler), this);
        pluginManager.registerEvents(new MoveListener(this, protectionHandler), this);
        pluginManager.registerEvents(new BlockListener(protectionHandler), this);
        pluginManager.registerEvents(new InventoryListener(protectionHandler), this);

        getCommand("joinprotection").setExecutor(new JoinProtectionCommand(this, messageManager));
        getCommand("joinprotection").setTabCompleter(new TabComplete());

        // PlaceholderAPI hook
        if (getConfig().getBoolean("integration.placeholderapi.enabled") && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderApi(this, protectionHandler).register();
            getLogger().info("Hooked into PlaceholderAPI.");
        }
        // MiniPlaceholders hook
        if (getConfig().getBoolean("integration.miniplaceholders.enabled") && Bukkit.getPluginManager().getPlugin("MiniPlaceholders") != null) {
            new MiniPlaceholders(this, protectionHandler).registerExpansion();
            getLogger().info("Hooked into MiniPlaceholders.");
        }
        // LuckPerms hook
        if (getConfig().getBoolean("integration.luckperms.enabled") && Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                LuckPerms api = provider.getProvider();
                api.getContextManager().registerCalculator(new LuckPermsContext(protectionHandler));
                getLogger().info("Hooked into LuckPerms.");
            }
        }

        boolean updateChecks = getConfig().getBoolean("plugin.update-checks");

        Metrics metrics = new Metrics(this, 19289);
        metrics.addCustomChart(new SimplePie("update_checks", () -> String.valueOf(updateChecks)));

        if (updateChecks) {
            new UpdateChecker(this);
        }
    }
}