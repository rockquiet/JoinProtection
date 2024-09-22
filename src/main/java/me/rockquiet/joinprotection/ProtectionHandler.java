package me.rockquiet.joinprotection;

import com.github.Anon8281.universalScheduler.UniversalRunnable;
import me.rockquiet.joinprotection.configuration.Config;
import me.rockquiet.joinprotection.protection.ProtectionInfo;
import me.rockquiet.joinprotection.protection.ProtectionType;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProtectionHandler implements Listener {

    private final Map<UUID, ProtectionInfo> invinciblePlayers = new ConcurrentHashMap<>();

    private final JoinProtection plugin;
    private final MessageManager messageManager;

    private double[][] particleCoordinates;

    public ProtectionHandler(JoinProtection joinProtection,
                             MessageManager messageManager) {
        this.plugin = joinProtection;
        this.messageManager = messageManager;

        calculateParticleCoordinates();
    }

    private int getBonusTime(Player player) {
        return player.getEffectivePermissions().stream()
                .filter(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission().startsWith("joinprotection.plus-"))
                .map(permissionAttachmentInfo -> {
                    String permission = permissionAttachmentInfo.getPermission();
                    String[] segments = permission.split("-");
                    return Integer.parseInt(segments[segments.length - 1]);
                }).max(Integer::compareTo)
                .orElse(0);
    }

    public void startJoinProtection(Player player) {
        startProtection(player, plugin.config().plugin.protectionTime, ProtectionType.JOIN);
    }

    public void startWorldProtection(Player player) {
        startProtection(player, plugin.config().plugin.worldChangeProtectionTime, ProtectionType.WORLD);
    }

    public void startCommandProtection(Player player, int protectionTime) {
        startProtection(player, protectionTime, ProtectionType.COMMAND);
    }

    public void startProtection(Player player, int protectionTime, ProtectionType type) {
        if (protectionTime <= 0) return;
        final int finalProtectionTime = protectionTime + getBonusTime(player);

        final UUID uuid = player.getUniqueId();
        if (hasProtection(uuid)) return;

        invinciblePlayers.put(uuid, new ProtectionInfo(player.getLocation(), type));

        final Config config = plugin.config();
        new UniversalRunnable() {
            int timeRemaining = finalProtectionTime;

            @Override
            public void run() {
                if (hasProtection(uuid)) {
                    // runs until timer reached 1
                    if (timeRemaining <= finalProtectionTime && timeRemaining >= 1) {
                        messageManager.sendProtectionInfo(player, config.messages.timeRemaining,
                                type.getPlaceholder(config),
                                Placeholder.unparsed("time", String.valueOf(timeRemaining))
                        );
                    }
                    // runs once
                    if (timeRemaining == 0) {
                        invinciblePlayers.remove(uuid);
                        cancel();
                        messageManager.sendProtectionInfo(player, config.messages.protectionEnded, type.getPlaceholder(config));
                    }
                    timeRemaining--;
                } else {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);

        spawnParticles(player);
    }

    public void calculateParticleCoordinates() {
        final Config config = plugin.config();
        final double[] scale = config.particles.scaleFactor;
        final int circles = config.particles.circles;
        final double increment = Math.PI / circles;

        int arrayLocation = 0;
        particleCoordinates = new double[(circles + 1) * circles * 2][];

        for (double i = 0; i <= Math.PI; i += increment) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += increment) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                particleCoordinates[arrayLocation++] = new double[]{x * scale[0], y * scale[1], z * scale[2]};
            }
        }
    }

    private void spawnParticles(Player player) {
        final Config config = plugin.config();
        if (!config.particles.enabled) return;

        if (plugin.isPaper() && plugin.getServer().getAverageTickTime() >= config.particles.maximumMspt) {
            return;
        }

        new UniversalRunnable() {
            final Particle particle = config.particles.toParticle();
            final int particleAmount = config.particles.amount;

            final UUID playerUUID = player.getUniqueId();
            final World world = player.getWorld();

            @Override
            public void run() {
                if (hasProtection(playerUUID)) {
                    final Location location = player.getLocation().add(0, player.getHeight() / 2, 0);

                    for (double[] coord : particleCoordinates) {
                        final Location particleLocation = location.clone().add(coord[0], coord[1], coord[2]);
                        world.spawnParticle(particle, particleLocation, particleAmount);
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, config.particles.refreshRate);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isEnabledInWorld(World world) {
        final Config config = plugin.config();
        List<String> worldList = config.plugin.worldList;
        String worldName = world.getName();

        return switch (config.plugin.listType.toLowerCase(Locale.ROOT)) {
            case "whitelist" -> worldList.stream().anyMatch(s -> s.equals(worldName));
            case "blacklist" -> worldList.stream().noneMatch(s -> s.equals(worldName));
            default -> true;
        };
    }

    public boolean hasProtection(UUID playerUUID) {
        return invinciblePlayers.containsKey(playerUUID);
    }

    public ProtectionInfo getProtectionInfo(UUID playerUUID) {
        if (hasProtection(playerUUID)) {
            return invinciblePlayers.get(playerUUID);
        }
        return null;
    }

    public void clearMobTargets(Player player) {
        final Config config = plugin.config();
        final double horizontalRange = config.modules.disableEntityTargeting.horizontalRange;
        final double verticalRange = config.modules.disableEntityTargeting.verticalRange;

        player.getNearbyEntities(horizontalRange, verticalRange, horizontalRange).forEach(entity -> {
            if (entity instanceof Mob mob && Objects.equals(mob.getTarget(), player)) {
                mob.setTarget(null);
            }
        });
    }

    public void cancelProtectionIfEnabled(Player player, String permission, boolean cancelled, String messageOnCancel) {
        if (hasProtection(player.getUniqueId()) && !player.hasPermission(permission) && cancelled) {
            cancelProtection(player, messageOnCancel);
        }
    }

    public void cancelProtection(Player player, String messageOnCancel) {
        messageManager.sendProtectionInfo(player, messageOnCancel, getProtectionInfo(player.getUniqueId()).type().getPlaceholder(plugin.config()));

        invinciblePlayers.remove(player.getUniqueId());
    }

    public boolean isEventCancelled(UUID playerUUID, boolean cancelled) {
        return cancelled && hasProtection(playerUUID);
    }

    // clear map
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        invinciblePlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        invinciblePlayers.remove(event.getEntity().getUniqueId());
    }
}
