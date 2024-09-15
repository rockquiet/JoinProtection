package me.rockquiet.joinprotection.protection;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public enum ProtectionType {

    JOIN("messages.type.join"),
    WORLD("messages.type.world"),
    COMMAND("messages.type.command");

    private final String messagePath;

    ProtectionType(String messagePath) {
        this.messagePath = messagePath;
    }

    public String getMessagePath() {
        return messagePath;
    }

    public TagResolver getPlaceholder(FileConfiguration config) {
        return Placeholder.parsed("type", Objects.requireNonNullElse(config.getString(getMessagePath()), "N/A"));
    }
}
