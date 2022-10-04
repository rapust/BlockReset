package net.rapust.blockreset.block;

import lombok.Getter;
import net.rapust.blockreset.BlockReset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class BlocksManager {

    @Getter
    private static BlocksManager manager;
    @Getter
    private final List<PlacedBlock> placedBlocks;
    @Getter
    private final List<ConfigBlock> configBlocks;

    @Getter
    private Long timeToClear;

    private Boolean canBreak;

    public BlocksManager() {
        manager = this;
        placedBlocks = new ArrayList<>();
        configBlocks = new ArrayList<>();
        reload();
    }

    public void reload() {
        FileConfiguration config = BlockReset.getInstance().getConfig();
        canBreak = config.getBoolean("settings.canBreakBlock");
        timeToClear = config.getLong("settings.timeToClearAfterLast");
        configBlocks.clear();
        ConfigurationSection blocks = config.getConfigurationSection("settings.blocks");
        blocks.getKeys(false).forEach(key -> {
            try {
                Material material = Material.valueOf(blocks.getString(key+".material"));
                Byte durability = null;
                Integer d = blocks.getInt(key+".durability");
                if (d != 0) {
                    durability = d.byteValue();
                }
                Long time = blocks.getLong(key+".time");
                ConfigBlock configBlock = new ConfigBlock(material, durability, time);
                configBlocks.add(configBlock);
            } catch (Exception ignored) {}
        });
    }

    public void clear() {
        List<PlacedBlock> backupBlocks = new ArrayList<>(placedBlocks);
        placedBlocks.clear();
        backupBlocks.forEach(PlacedBlock::returnToOriginal);
    }

    public void addBlock(Block block, Material originalMaterial, Byte originalData) {
        placedBlocks.add(new PlacedBlock(block, originalMaterial, originalData));
    }

    public void removePlacedBlock(PlacedBlock placedBlock) {
        placedBlocks.remove(placedBlock);
    }

    public PlacedBlock getPlacedBlock(Block block) {
        for (PlacedBlock placedBlock : placedBlocks) {
            if (placedBlock.getBlock().getLocation().equals(block.getLocation())) return placedBlock;
        }
        return null;
    }

    public Boolean breakBlock(Block block) {
        PlacedBlock placedBlock = getPlacedBlock(block);
        if (placedBlock == null) return null;
        if (placedBlock.getWaitingIndex() == 0 || canBreak) {
            removePlacedBlock(placedBlock);
            return false;
        }
        return true;
    }

}
