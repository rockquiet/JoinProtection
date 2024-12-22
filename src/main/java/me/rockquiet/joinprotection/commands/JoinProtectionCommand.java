package me.rockquiet.joinprotection.commands;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.MessageManager;
import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.Config;
import me.rockquiet.joinprotection.configuration.Permissions;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public class JoinProtectionCommand implements CommandExecutor {

    protected static final String PROTECT = "protect";
    protected static final String CANCEL = "cancel";
    protected static final String RELOAD = "reload";

    private final JoinProtection plugin;
    private final MessageManager messageManager;
    private final ProtectionHandler protectionHandler;

    public JoinProtectionCommand(JoinProtection joinProtection,
                                 MessageManager messageManager,
                                 ProtectionHandler protectionHandler) {
        this.plugin = joinProtection;
        this.messageManager = messageManager;
        this.protectionHandler = protectionHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;

        return switch (args[0].toLowerCase(Locale.ROOT)) {
            case PROTECT -> protect(sender, removeFirstArg(args));
            case CANCEL -> cancel(sender, removeFirstArg(args));
            case RELOAD -> reload(sender);
            default -> false;
        };
    }

    private String[] removeFirstArg(@NotNull String[] args) {
        return args.length == 0 ? new String[]{} : Arrays.copyOfRange(args, 1, args.length);
    }

    private boolean protect(@NotNull CommandSender sender, @NotNull String[] args) {
        // /joinprotection protect <player> <time>
        if (hasNoPerms(sender, Permissions.PROTECT)) return false;
        final Config config = plugin.config();

        // check/get player
        if (args.length == 0 || args[0].isBlank()) {
            messageManager.sendMessage(sender, config.messages.commandUsage, Placeholder.parsed("command_usage", "joinprotection protect <b><player></b> <time>"));
            return false;
        }
        Player targetPlayer = plugin.getServer().getPlayerExact(args[0]);
        if (targetPlayer == null) {
            messageManager.sendMessage(sender, config.messages.playerNotFound, Placeholder.unparsed("player", args[0]));
            return false;
        }

        // player already protected?
        if (protectionHandler.hasProtection(targetPlayer.getUniqueId())) {
            messageManager.sendMessage(sender, config.messages.alreadyProtected, Placeholder.unparsed("player", targetPlayer.getName()));
            return false;
        }

        // get time
        if (args.length == 1 || args[1].isBlank()) {
            messageManager.sendMessage(sender, config.messages.commandUsage, Placeholder.parsed("command_usage", "joinprotection protect <player> <b><time></b>"));
            return false;
        }
        int protectionTime;
        try {
            protectionTime = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            messageManager.sendMessage(sender, config.messages.invalidNumberFormat, Placeholder.unparsed("number", args[1]));
            return false;
        }
        if (protectionTime <= 0) {
            messageManager.sendMessage(sender, config.messages.numberMustBePositive, Placeholder.unparsed("number", String.valueOf(protectionTime)));
            return false;
        }

        // run protection
        protectionHandler.startCommandProtection(targetPlayer, protectionTime);
        messageManager.sendMessage(sender, config.messages.protect,
                Placeholder.unparsed("player", targetPlayer.getName()),
                Placeholder.unparsed("time", String.valueOf(protectionTime))
        );

        if (config.modules.disableEntityTargeting.enabled) {
            protectionHandler.clearMobTargets(targetPlayer);
        }

        return true;
    }

    private boolean cancel(@NotNull CommandSender sender, @NotNull String[] args) {
        // /joinprotection cancel <player>
        if (hasNoPerms(sender, Permissions.CANCEL)) return false;
        final Config config = plugin.config();

        // check/get player
        if (args.length == 0 || args[0].isBlank()) {
            messageManager.sendMessage(sender, config.messages.commandUsage, Placeholder.parsed("command_usage", "joinprotection cancel <b><player></b>"));
            return false;
        }
        Player targetPlayer = plugin.getServer().getPlayerExact(args[0]);
        if (targetPlayer == null) {
            messageManager.sendMessage(sender, config.messages.playerNotFound, Placeholder.unparsed("player", args[0]));
            return false;
        }

        if (!protectionHandler.hasProtection(targetPlayer.getUniqueId())) {
            messageManager.sendMessage(sender, config.messages.notProtected, Placeholder.unparsed("player", targetPlayer.getName()));
            return false;
        }

        protectionHandler.cancelProtection(targetPlayer, config.messages.protectionDeactivated);
        messageManager.sendMessage(sender, config.messages.cancel, Placeholder.unparsed("player", targetPlayer.getName()));

        return true;
    }

    private boolean reload(@NotNull CommandSender sender) {
        if (hasNoPerms(sender, Permissions.RELOAD)) return false;

        plugin.configManager().load();
        protectionHandler.calculateParticleCoordinates();

        messageManager.sendMessage(sender, plugin.config().messages.reload);
        return true;
    }

    private boolean hasNoPerms(@NotNull CommandSender sender, @NotNull String permission) {
        if ((sender instanceof Player player) && !player.hasPermission(permission)) {
            messageManager.sendMessage(player, plugin.config().messages.noPerms);
            return true;
        }
        return false;
    }
}
