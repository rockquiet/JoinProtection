package me.rockquiet.joinprotection;

import me.rockquiet.joinprotection.commands.JoinProtectionCommand;
import me.rockquiet.joinprotection.commands.TabComplete;
import me.rockquiet.joinprotection.listeners.BlockListener;
import me.rockquiet.joinprotection.listeners.DamageListener;
import me.rockquiet.joinprotection.listeners.JoinListener;
import me.rockquiet.joinprotection.listeners.MoveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class JoinProtection extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ProtectionHandler protectionHandler = new ProtectionHandler(this);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(protectionHandler, this);
        pluginManager.registerEvents(new JoinListener(this, protectionHandler), this);
        pluginManager.registerEvents(new DamageListener(this, protectionHandler), this);
        pluginManager.registerEvents(new MoveListener(this, protectionHandler), this);
        pluginManager.registerEvents(new BlockListener(protectionHandler), this);

        getCommand("joinprotection").setExecutor(new JoinProtectionCommand(this));
        getCommand("joinprotection").setTabCompleter(new TabComplete());

        if (getConfig().getBoolean("plugin.update-checks")) {
            try {
                new UpdateChecker().check(this);
            } catch (IOException e) {
                getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        }
    }
}