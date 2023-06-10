package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class MoveListener implements Listener {

    private final JoinProtection joinProtection;

    public MoveListener(JoinProtection joinProtection) {
        this.joinProtection = joinProtection;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (JoinListener.invinciblePlayers.containsKey(uuid)) {
            Location joinLocation = JoinListener.invinciblePlayers.get(uuid);
            boolean shouldCancel = joinProtection.getConfig().getBoolean("cancel.on-move");
            double distance = joinProtection.getConfig().getDouble("cancel.distance");

            if (shouldCancel && joinLocation.set(joinLocation.getX(), player.getLocation().getY(), joinLocation.getZ()).distance(player.getLocation()) >= distance && event.hasChangedOrientation()) {
                JoinListener.invinciblePlayers.remove(player.getUniqueId());
                player.sendActionBar(MiniMessage.miniMessage().deserialize(joinProtection.getConfig().getString("messages.protectionDeactivated")));
            }
        }
    }
}
