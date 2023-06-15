package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final JoinProtection joinProtection;
    private final ProtectionHandler protectionHandler;

    public JoinListener(JoinProtection joinProtection,
                        ProtectionHandler protectionHandler) {
        this.joinProtection = joinProtection;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!joinProtection.getConfig().getBoolean("plugin.enabled") || !player.hasPermission("joinprotection.use")) {
            return;
        }

        protectionHandler.startProtection(player);
    }
}
