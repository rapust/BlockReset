package net.rapust.blockreset.listener;

import net.rapust.blockreset.block.BlocksManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        Boolean canBreak = BlocksManager.getManager().breakBlock(event.getBlock());
        if (canBreak == null) return;
        event.setCancelled(canBreak);
    }

}
