package me.rockquiet.joinprotection;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ProtectionHandler implements Listener {

    public static final Map<UUID, Location> invinciblePlayers = new HashMap<>();
    private final JoinProtection joinProtection;

    public ProtectionHandler(JoinProtection joinProtection) {
        this.joinProtection = joinProtection;
    }

    public void startProtection(Player player) {
        UUID uuid = player.getUniqueId();

        invinciblePlayers.put(uuid, player.getLocation());

        AtomicInteger bonusTime = new AtomicInteger();
        player.getEffectivePermissions().stream()
                .filter(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission().startsWith("joinprotection.plus-")).map(permissionAttachmentInfo -> {
                    String permission = permissionAttachmentInfo.getPermission();
                    String[] segments = permission.split("-");
                    return Integer.parseInt(segments[segments.length - 1]);
                }).max(Integer::compareTo).ifPresent(bonusTime::set);

        final int protectionTime = joinProtection.getConfig().getInt("plugin.protection-time") + bonusTime.get();

        new BukkitRunnable() {
            int timeRemaining = protectionTime;

            @Override
            public void run() {
                if (ProtectionHandler.invinciblePlayers.containsKey(uuid)) {
                    // runs until timer reached 1
                    if (timeRemaining <= protectionTime && timeRemaining >= 1) {
                        if (joinProtection.getConfig().contains("messages.timeRemaining") && !joinProtection.getConfig().getString("messages.timeRemaining").isBlank()) {
                            player.sendActionBar(MiniMessage.miniMessage().deserialize(joinProtection.getConfig().getString("messages.timeRemaining").replace("%time%", String.valueOf(timeRemaining))));
                        }
                    }
                    // runs once
                    if (timeRemaining == 0) {
                        ProtectionHandler.invinciblePlayers.remove(uuid);
                        cancel();
                        if (joinProtection.getConfig().contains("messages.protectionEnded") && !joinProtection.getConfig().getString("messages.protectionEnded").isBlank()) {
                            player.sendActionBar(MiniMessage.miniMessage().deserialize(joinProtection.getConfig().getString("messages.protectionEnded")));
                        }
                    }
                    timeRemaining--;
                } else {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(joinProtection, 0, 20);

        spawnParticles(player);
    }

    private void spawnParticles(Player player) {
        if (joinProtection.getConfig().getBoolean("particles.enabled")) {
            final long refreshRate = joinProtection.getConfig().getLong("particles.refresh-rate");
            final double circles = joinProtection.getConfig().getLong("particles.circles");
            new BukkitRunnable() {
                final String particle = joinProtection.getConfig().getString("particles.type");
                final int particleAmount = joinProtection.getConfig().getInt("particles.amount");

                @Override
                public void run() {
                    if (invinciblePlayers.containsKey(player.getUniqueId())) {
                        Location location = player.getLocation().add(0, 1.5, 0);

                        for (double i = 0; i <= Math.PI; i += Math.PI / circles) { // 10 being the amount of circles.
                            double radius = Math.sin(i); // we get the current radius
                            double y = Math.cos(i); // we get the current y value.
                            for (double a = 0; a < Math.PI * 2; a += Math.PI / circles) {
                                double x = Math.cos(a) * radius;
                                double z = Math.sin(a) * radius;
                                location.add(x, y, z);
                                // display particle at 'location'.
                                player.getWorld().spawnParticle(Particle.valueOf(particle), location, particleAmount);
                                location.subtract(x, y, z);
                            }
                        }
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(joinProtection, 0, refreshRate);
        }
    }

    public boolean hasProtection(Player player) {
        return invinciblePlayers.containsKey(player.getUniqueId());
    }

    public Location getLocation(Player player) {
        if (hasProtection(player)) {
            return invinciblePlayers.get(player.getUniqueId());
        }
        return null;
    }

    public void cancelProtectionIfEnabled(Player player, String permission, String cancelOn, String messageOnCancel) {
        if (invinciblePlayers.containsKey(player.getUniqueId()) && !player.hasPermission(permission)) {
            boolean shouldCancel = (joinProtection.getConfig().contains(cancelOn) && joinProtection.getConfig().getBoolean(cancelOn));

            if (shouldCancel) {
                cancelProtection(player, messageOnCancel);
            }
        }
    }

    public void cancelProtection(Player player, String messageOnCancel) {
        invinciblePlayers.remove(player.getUniqueId());
        if (joinProtection.getConfig().contains(messageOnCancel) && !joinProtection.getConfig().getString(messageOnCancel).isBlank()) {
            player.sendActionBar(MiniMessage.miniMessage().deserialize(joinProtection.getConfig().getString(messageOnCancel)));
        }
    }

    public boolean isEventCancelled(Player player, String module) {
        return joinProtection.getConfig().contains(module) && joinProtection.getConfig().getBoolean(module) && invinciblePlayers.containsKey(player.getUniqueId());
    }

    // clear map
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        invinciblePlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        invinciblePlayers.remove(event.getPlayer().getUniqueId());
    }
}
