package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class MoveListener implements Listener {

    private final JoinProtection plugin;
    private final ProtectionHandler protectionHandler;

    public MoveListener(JoinProtection joinProtection,
                        ProtectionHandler protectionHandler) {
        this.plugin = joinProtection;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().distanceSquared(event.getTo()) < 0.01) return;

        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (player.hasPermission("joinprotection.bypass.cancel-on-move")) return;
        if (!protectionHandler.hasProtection(playerUUID)) return;

        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("cancel.on-move")) return;

        Location joinLocation = protectionHandler.getLocation(playerUUID);
        Location playerLocation = player.getLocation();
        double distance = config.getDouble("cancel.distance");
        double distanceSquared = distance * distance;

        if (joinLocation.set(joinLocation.getX(), playerLocation.getY(), joinLocation.getZ()).distanceSquared(playerLocation) >= distanceSquared) {
            protectionHandler.cancelProtection(player, "messages.protectionDeactivated");
        }
    }
}
