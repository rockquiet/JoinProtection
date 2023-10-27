package me.rockquiet.joinprotection;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ProtectionHandler implements Listener {

    private static final Map<UUID, Location> invinciblePlayers = new HashMap<>();
    private final JoinProtection plugin;
    private final MessageManager messageManager;

    public ProtectionHandler(JoinProtection joinProtection,
                             MessageManager messageManager) {
        this.plugin = joinProtection;
        this.messageManager = messageManager;
    }

    public void startProtection(Player player) {
        FileConfiguration config = plugin.getConfig();
        UUID uuid = player.getUniqueId();

        invinciblePlayers.put(uuid, player.getLocation());

        AtomicInteger bonusTime = new AtomicInteger();
        player.getEffectivePermissions().stream()
                .filter(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission().startsWith("joinprotection.plus-"))
                .map(permissionAttachmentInfo -> {
                    String permission = permissionAttachmentInfo.getPermission();
                    String[] segments = permission.split("-");
                    return Integer.parseInt(segments[segments.length - 1]);
                }).max(Integer::compareTo).ifPresent(bonusTime::set);

        final int protectionTime = config.getInt("plugin.protection-time") + bonusTime.get();

        new BukkitRunnable() {
            int timeRemaining = protectionTime;

            @Override
            public void run() {
                if (hasProtection(uuid)) {
                    // runs until timer reached 1
                    if (timeRemaining <= protectionTime && timeRemaining >= 1) {
                        messageManager.sendActionbar(config, player, "messages.timeRemaining", "%time%", String.valueOf(timeRemaining));
                    }
                    // runs once
                    if (timeRemaining == 0) {
                        invinciblePlayers.remove(uuid);
                        cancel();
                        messageManager.sendActionbar(config, player, "messages.protectionEnded");
                    }
                    timeRemaining--;
                } else {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);

        spawnParticles(player);
    }

    private void spawnParticles(Player player) {
        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("particles.enabled")) return;

        if (plugin.isPaper() && plugin.getServer().getAverageTickTime() >= config.getDouble("particles.maximum-mspt")) {
            return;
        }

        new BukkitRunnable() {
            final Particle particle = Particle.valueOf(config.getString("particles.type"));
            final int particleAmount = config.getInt("particles.amount");
            final double circles = config.getLong("particles.circles");
            final UUID playerUUID = player.getUniqueId();
            final World world = player.getWorld();

            @Override
            public void run() {
                if (hasProtection(playerUUID)) {
                    Location location = player.getLocation().add(0, 1.5, 0);

                    for (double i = 0; i <= Math.PI; i += Math.PI / circles) {
                        double radius = Math.sin(i);
                        double y = Math.cos(i);
                        for (double a = 0; a < Math.PI * 2; a += Math.PI / circles) {
                            double x = Math.cos(a) * radius;
                            double z = Math.sin(a) * radius;
                            location.add(x, y, z);
                            world.spawnParticle(particle, location, particleAmount);
                            location.subtract(x, y, z);
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, config.getLong("particles.refresh-rate"));
    }

    public boolean isEnabledInWorld(World world) {
        FileConfiguration config = plugin.getConfig();
        List<String> worldList = config.getStringList("plugin.world-list");
        String worldName = world.getName();

        return switch (config.getString("plugin.list-type").toLowerCase()) {
            case "whitelist" -> worldList.stream().anyMatch(s -> s.equals(worldName));
            case "blacklist" -> worldList.stream().noneMatch(s -> s.equals(worldName));
            default -> true;
        };
    }

    public boolean hasProtection(UUID playerUUID) {
        return invinciblePlayers.containsKey(playerUUID);
    }

    public Location getLocation(UUID playerUUID) {
        if (hasProtection(playerUUID)) {
            return invinciblePlayers.get(playerUUID);
        }
        return null;
    }

    public void cancelProtectionIfEnabled(Player player, String permission, String cancelOn, String messageOnCancel) {
        FileConfiguration config = plugin.getConfig();
        if (invinciblePlayers.containsKey(player.getUniqueId()) && !player.hasPermission(permission) && config.contains(cancelOn) && config.getBoolean(cancelOn)) {
            cancelProtection(player, messageOnCancel);
        }
    }

    public void cancelProtection(Player player, String messageOnCancel) {
        invinciblePlayers.remove(player.getUniqueId());

        messageManager.sendActionbar(plugin.getConfig(), player, messageOnCancel);
    }

    public boolean isEventCancelled(UUID playerUUID, String module) {
        FileConfiguration config = plugin.getConfig();
        return config.contains(module) && config.getBoolean(module) && hasProtection(playerUUID);
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
