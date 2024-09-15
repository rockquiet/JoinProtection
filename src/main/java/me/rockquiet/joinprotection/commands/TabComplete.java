package me.rockquiet.joinprotection.commands;

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getLabel().equalsIgnoreCase("joinprotection")) {
            return List.of();
        }

        final List<String> results = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        int index = 0;
        if (args.length == 1) {
            if (sender.hasPermission("joinprotection.protect")) {
                results.add("protect");
            }
            if (sender.hasPermission("joinprotection.reload")) {
                results.add("reload");
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("protect") && sender.hasPermission("joinprotection.protect")) {
            index = 1;
            for (Player p : Bukkit.getOnlinePlayers()) {
                results.add(p.getName());
            }
        }

        StringUtil.copyPartialMatches(args[index], results, completions);
        Collections.sort(completions);
        results.clear();
        results.addAll(completions);
        return results;
    }
}
