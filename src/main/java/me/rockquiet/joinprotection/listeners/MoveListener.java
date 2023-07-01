package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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

        if (protectionHandler.hasProtection(player) && event.getFrom().distance(event.getTo()) > 0.1 && !player.hasPermission("joinprotection.bypass.cancel-on-move")) {
            Location joinLocation = protectionHandler.getLocation(player);
            boolean shouldCancel = joinProtection.getConfig().getBoolean("cancel.on-move");
            double distance = joinProtection.getConfig().getDouble("cancel.distance");

            if (shouldCancel && joinLocation.set(joinLocation.getX(), player.getLocation().getY(), joinLocation.getZ()).distance(player.getLocation()) >= distance) {
                protectionHandler.cancelProtection(player, "messages.protectionDeactivated");
            }
        }
    }
}
