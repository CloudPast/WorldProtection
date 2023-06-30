package org.plugin.chantingcat.pluginmain;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.chantingcat.commands.ReloadCommand;
import org.plugin.chantingcat.event.worldEvent;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    private File customConfigFile;
    private FileConfiguration customConfig;
    public static final String ANSI_YELLOW = "\033[33m";
    public static final String ANSI_RESET = "\033[0m";
    @Override
    public void onEnable() {
        int version = Integer.parseInt(getServer().getBukkitVersion().substring(2,4));
        if (version > 12) {
            saveDefaultConfig();
            reloadConfig();
            createMessage();
            Bukkit.getPluginManager().registerEvents(new worldEvent(),this);
            Bukkit.getPluginCommand("wpreload").setExecutor(new ReloadCommand());
            getLogger().info(ANSI_YELLOW + "服务器版本:" + getServer().getBukkitVersion() + ANSI_RESET);
            getLogger().info(ANSI_YELLOW + "版本兼容 WorldProtection已成功启动！" + ANSI_RESET);
            getLogger().info(ANSI_YELLOW + "欢迎使用本插件~！ 有问题联系QQ:1700721266" + ANSI_RESET);
        } else {
            getLogger().info(ANSI_YELLOW + "版本不兼容 插件已经卸载" + ANSI_RESET);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    public FileConfiguration getMessage(){
        return this.customConfig;
    }
    public void createMessage(){
        customConfigFile = new File(getDataFolder(),"message.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("message.yml", false);
        }
        customConfig = new YamlConfiguration();
        try{
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
