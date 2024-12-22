package me.rockquiet.joinprotection.commands;

import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.configuration.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    private final ConfigManager configManager;

    public TabComplete(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getLabel().equalsIgnoreCase("joinprotection")) {
            return Collections.emptyList();
        }

        final List<String> results = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                if (sender.hasPermission(Permissions.PROTECT)) results.add(JoinProtectionCommand.PROTECT);
                if (sender.hasPermission(Permissions.CANCEL)) results.add(JoinProtectionCommand.CANCEL);
                if (sender.hasPermission(Permissions.RELOAD)) results.add(JoinProtectionCommand.RELOAD);
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase(JoinProtectionCommand.PROTECT) && sender.hasPermission(Permissions.PROTECT)
                        || args[0].equalsIgnoreCase(JoinProtectionCommand.CANCEL) && sender.hasPermission(Permissions.CANCEL)
                ) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        results.add(p.getName());
                    }
                }
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase(JoinProtectionCommand.PROTECT) && sender.hasPermission(Permissions.PROTECT)) {
                    results.add(String.valueOf(configManager.get().plugin.protectionTime));
                }
            }
            default -> {
                return Collections.emptyList();
            }
        }

        final List<String> completions = new ArrayList<>();
        final int index = args.length > 1 ? args.length - 1 : 0;
        StringUtil.copyPartialMatches(args[index], results, completions);
        Collections.sort(completions);

        return completions;
    }
}
