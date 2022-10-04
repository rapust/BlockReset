package net.rapust.blockreset.listener;

import net.rapust.blockreset.block.BlocksManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        BlocksManager.getManager().addBlock(event.getBlock(), event.getBlockReplacedState().getType(), event.getBlockReplacedState().getRawData());
    }

}
