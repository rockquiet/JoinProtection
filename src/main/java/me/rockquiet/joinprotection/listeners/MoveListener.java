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

    private final JoinProtection joinProtection;
    private final ProtectionHandler protectionHandler;

    public MoveListener(JoinProtection joinProtection,
                        ProtectionHandler protectionHandler) {
        this.joinProtection = joinProtection;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (protectionHandler.hasProtection(playerUUID) && event.getFrom().distanceSquared(event.getTo()) > 0.01 && !player.hasPermission("joinprotection.bypass.cancel-on-move")) {
            Location joinLocation = protectionHandler.getLocation(playerUUID);
            Location playerLocation = player.getLocation();

            FileConfiguration config = joinProtection.getConfig();
            boolean shouldCancel = config.getBoolean("cancel.on-move");
            double distance = config.getDouble("cancel.distance");
            double distanceSquared = distance * distance;

            if (shouldCancel && joinLocation.set(joinLocation.getX(), playerLocation.getY(), joinLocation.getZ()).distanceSquared(playerLocation) >= distanceSquared) {
                protectionHandler.cancelProtection(player, "messages.protectionDeactivated");
            }
        }
    }
}
