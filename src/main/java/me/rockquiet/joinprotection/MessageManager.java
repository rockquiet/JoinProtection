package me.rockquiet.joinprotection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Locale;

public class MessageManager {

    private final JoinProtection plugin;
    private final MiniMessage msg;

    public MessageManager(JoinProtection plugin) {
        this.plugin = plugin;
        this.msg = MiniMessage.miniMessage();
    }

    private boolean messageEmpty(FileConfiguration config, String messagePath) {
        return !config.contains(messagePath) || config.getString(messagePath).isBlank();
    }

    private TagResolver getPrefixPlaceholder(FileConfiguration config) {
        return Placeholder.parsed("prefix", config.getString("messages.prefix"));
    }

    public void sendMessage(FileConfiguration config, CommandSender sender, String messagePath) {
        sendMessage(config, sender, messagePath, TagResolver.empty());
    }

    public void sendMessage(FileConfiguration config, CommandSender sender, String messagePath, TagResolver... tagResolvers) {
        if (messageEmpty(config, messagePath)) return;

        plugin.adventure().sender(sender).sendMessage(msg.deserialize(
                config.getString(messagePath),
                getPrefixPlaceholder(config), TagResolver.resolver(tagResolvers)
        ));
    }

    public void sendActionbar(FileConfiguration config, Player player, String messagePath, TagResolver... tagResolvers) {
        if (messageEmpty(config, messagePath)) return;

        plugin.adventure().player(player).sendActionBar(msg.deserialize(
                config.getString(messagePath),
                getPrefixPlaceholder(config), TagResolver.resolver(tagResolvers)
        ));
    }

    private Title.Times getTitleTimes(FileConfiguration config) {
        return Title.Times.times(
                Ticks.duration(config.getLong("display.title.fade-in")),
                Ticks.duration(config.getLong("display.title.stay")),
                Ticks.duration(config.getLong("display.title.fade-out"))
        );
    }

    public void sendTitle(FileConfiguration config, Player player, String messagePath, TagResolver... tagResolvers) {
        if (messageEmpty(config, messagePath)) return;

        plugin.adventure().player(player).showTitle(Title.title(
                msg.deserialize(config.getString(messagePath), getPrefixPlaceholder(config), TagResolver.resolver(tagResolvers)),
                Component.empty(),
                getTitleTimes(config)
        ));
    }

    public void sendSubTitle(FileConfiguration config, Player player, String messagePath, TagResolver... tagResolvers) {
        if (messageEmpty(config, messagePath)) return;

        plugin.adventure().player(player).showTitle(Title.title(
                Component.empty(),
                msg.deserialize(config.getString(messagePath), getPrefixPlaceholder(config), TagResolver.resolver(tagResolvers)),
                getTitleTimes(config)
        ));
    }

    public void sendProtectionInfo(FileConfiguration config, Player player, String messagePath, TagResolver... tagResolvers) {
        switch (config.getString("display.location").toLowerCase(Locale.ROOT)) {
            case "title" -> sendTitle(config, player, messagePath, tagResolvers);
            case "subtitle" -> sendSubTitle(config, player, messagePath, tagResolvers);
            case "chat" -> sendMessage(config, player, messagePath, tagResolvers);
            default -> sendActionbar(config, player, messagePath, tagResolvers);
        }
    }
}
