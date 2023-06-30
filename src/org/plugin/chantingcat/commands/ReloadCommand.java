package org.plugin.chantingcat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.plugin.chantingcat.pluginmain.Main;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class ReloadCommand implements CommandExecutor {
    private Plugin plugin = getPlugin(Main.class);
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()){
            this.plugin.reloadConfig();
            getPlugin(Main.class).createMessage();
            commandSender.sendMessage(
                    getPlugin(Main.class).getMessage().getString("pluginPrefix") +
                            getPlugin(Main.class).getMessage().getString("pluginReload"));
            return true;
        }
        commandSender.sendMessage(
                getPlugin(Main.class).getMessage().getString("pluginPrefix") +
                        getPlugin(Main.class).getMessage().getString("pluginMessage.notPerm"));
        return true;
    }

}
