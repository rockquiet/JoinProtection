package me.rockquiet.joinprotection.protection;

import me.rockquiet.joinprotection.configuration.Config;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Objects;

public enum ProtectionType {

    JOIN,
    WORLD,
    COMMAND;

    public TagResolver getPlaceholder(Config config) {
        return Placeholder.parsed("type", Objects.requireNonNullElse(config.messages.type.get(this), "N/A"));
    }
}
