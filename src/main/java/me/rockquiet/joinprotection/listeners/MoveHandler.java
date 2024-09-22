package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.Config;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.configuration.Permissions;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class MoveHandler {

    private final ConfigManager configManager;
    private final ProtectionHandler protectionHandler;

    protected MoveHandler(ConfigManager configManager, ProtectionHandler protectionHandler) {
        this.configManager = configManager;
        this.protectionHandler = protectionHandler;
    }

    public final void handlePlayerMove(PlayerMoveEvent event) {
        if (hasNotMoved(event)) return;

        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        if (player.hasPermission(Permissions.BYPASS_CANCEL_ON_MOVE)) return;
        if (!protectionHandler.hasProtection(playerUUID)) return;

        final Config config = configManager.get();
        if (!config.cancel.onMove.enabled) return;

        // fix for player getting pushed by an entity
        if (config.cancel.onMove.disablePushing && player.getNearbyEntities(0.5, 0.5, 0.5).stream().anyMatch(LivingEntity.class::isInstance)) {
            teleport(player, event.getFrom());
            return;
        }

        Location joinLocation = protectionHandler.getProtectionInfo(playerUUID).location();
        Location playerLocation = player.getLocation();

        if (config.cancel.onMove.ignoreYAxis) {
            joinLocation.setY(playerLocation.getY());
        }

        final double distance = config.cancel.onMove.distance;
        final double distanceSquared = distance * distance;

        if (joinLocation.getWorld() != playerLocation.getWorld() || joinLocation.distanceSquared(playerLocation) >= distanceSquared) {
            protectionHandler.cancelProtection(player, config.messages.protectionDeactivated);
        }
    }

    public boolean hasNotMoved(PlayerMoveEvent event) {
        return event.getFrom().distanceSquared(event.getTo()) < 0.01;
    }

    public void teleport(Player player, Location fromLocation) {
        player.teleport(fromLocation);
    }
}
