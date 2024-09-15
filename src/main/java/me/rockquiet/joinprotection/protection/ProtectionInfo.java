package me.rockquiet.joinprotection.protection;

import org.bukkit.Location;

public record ProtectionInfo(
        Location location,
        ProtectionType type
) {
}
