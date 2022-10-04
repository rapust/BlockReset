package net.rapust.blockreset;

import lombok.Getter;
import net.rapust.blockreset.block.BlocksManager;
import net.rapust.blockreset.block.PlacedBlock;
import net.rapust.blockreset.command.BlockResetCommand;
import net.rapust.blockreset.listener.BlockBreakListener;
import net.rapust.blockreset.listener.BlockPlaceListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlockReset extends JavaPlugin {

    @Getter
    private static BlockReset instance;

    private Integer schedulerId;

    @Override
    public void onEnable() {
        instance = this;

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getConfig().options().copyDefaults();
            saveDefaultConfig();
        }

        new BlocksManager();

        getCommand("blockreset").setExecutor(new BlockResetCommand());

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BlockBreakListener(), this);
        manager.registerEvents(new BlockPlaceListener(), this);

        startScheduler();

        getLogger().info("Plugin is enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(schedulerId);
        HandlerList.unregisterAll(this);
        BlocksManager.getManager().clear();
        getLogger().info("Plugin is disabled!");
    }

    public String getMessage(String messageCode) {
        messageCode = "messages."+messageCode;
        String message = getConfig().getString(messageCode);
        if (message == null) return messageCode;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void startScheduler() {
        schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

            List<PlacedBlock> blocks = new ArrayList<>(BlocksManager.getManager().getPlacedBlocks());
            blocks.forEach(PlacedBlock::update);

        }, 2L, 0L);
    }

}
