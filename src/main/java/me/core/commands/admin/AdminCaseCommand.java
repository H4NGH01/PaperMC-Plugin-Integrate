package me.core.commands.admin;

import me.core.cases.CaseKey;
import me.core.cases.CaseType;
import me.core.commands.PluginCommand;
import me.core.items.CaseKeyStack;
import me.core.items.CaseStack;
import me.core.utils.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminCaseCommand extends PluginCommand {

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.translatable("command.usages"));
            player.sendMessage("/admin-case list");
            player.sendMessage("/admin-case give");
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.translatable("command.case.list"));
            for (CaseType type : CaseType.values()) {
                player.sendMessage(Component.translatable(type.getTranslationKey()).append(Component.text(" id:\"" + type.getID() + "\"")));
            }
            player.sendMessage(Component.translatable("command.case.key_list"));
            for (CaseKey key : CaseKey.values()) {
                player.sendMessage(Component.translatable(key.getTranslationKey()).append(Component.text(" id:\"" + key.getID() + "\"")));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 1) {
                player.sendMessage(Component.translatable("command.case.invalid"));
                return;
            }
            String id = args[1];
            if (id.equalsIgnoreCase("random_case")) {
                CaseStack stack = plugin.getCaseManager().generateRandomCaseStack();
                player.getInventory().addItem(stack);
                player.sendMessage(Component.translatable("command.case.gave").args(Component.translatable(stack.getCaseType().getTranslationKey())));
                return;
            }
            for (CaseType type : CaseType.values()) {
                if (type.getID().equalsIgnoreCase(id)) {
                    CaseStack stack = new CaseStack(plugin.getCaseManager().getCaseByType(type));
                    player.getInventory().addItem(stack);
                    player.sendMessage(Component.translatable("command.case.gave").args(Component.translatable(stack.getCaseType().getTranslationKey())));
                    return;
                }
            }
            for (CaseKey key : CaseKey.values()) {
                if (key.getID().equalsIgnoreCase(id)) {
                    CaseKeyStack stack = new CaseKeyStack(key);
                    player.getInventory().addItem(stack);
                    player.sendMessage(Component.translatable("command.case.gave").args(Component.translatable(stack.getKeyType().getTranslationKey())));
                    return;
                }
            }
            player.sendMessage(Component.translatable("command.case.invalid"));
            return;
        }
        player.sendMessage(ComponentUtil.translate(NamedTextColor.RED, "command.unknown.argument"));
        player.sendMessage(Component.translatable("command.usages"));
        player.sendMessage("/admin-case list");
        player.sendMessage("/admin-case give");
    }

    @Override
    public String name() {
        return "admin-case";
    }

    @Override
    public String info() {
        return "Admin case commands";
    }

    @Override
    public String[] aliases() {
        return new String[]{"a-case"};
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("list");
            list.add("give");
        }
        if (args.length > 1 & args[0].equalsIgnoreCase("give")) {
            for (CaseType type : CaseType.values()) {
                list.add(type.getID());
            }
            for (CaseKey key : CaseKey.values()) {
                list.add(key.getID());
            }
            list.add("random_case");
        }
        return list;
    }
}
