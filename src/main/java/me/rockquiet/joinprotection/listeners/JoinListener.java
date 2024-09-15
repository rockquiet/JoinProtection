package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final JoinProtection plugin;
    private final ProtectionHandler protectionHandler;

    public JoinListener(JoinProtection joinProtection,
                        ProtectionHandler protectionHandler) {
        this.plugin = joinProtection;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("joinprotection.use")) return;

        if (!protectionHandler.isEnabledInWorld(player.getWorld()) && !player.hasPermission("joinprotection.bypass.world-list")) {
            return;
        }

        if (player.hasPlayedBefore() && plugin.getConfig().getBoolean("plugin.first-join-only")) {
            return;
        }

        protectionHandler.startJoinProtection(player);
    }
}
