package net.rapust.blockreset.block;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class PlacedBlock {

    @Getter
    private final Block block;

    private final Material originalMaterial;
    private final Byte orginalData;

    @Getter
    private Integer waitingIndex;
    private Long lastUpdate;

    public PlacedBlock(Block block, Material originalMaterial, Byte orginalData) {
        this.block = block;
        this.originalMaterial = originalMaterial;
        this.orginalData = orginalData;
        this.waitingIndex = 0;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void update() {
        BlocksManager manager = BlocksManager.getManager();
        if (checkIsBroken()) {
            manager.removePlacedBlock(this);
            return;
        }
        Long time = System.currentTimeMillis();
        if (waitingIndex >= manager.getConfigBlocks().size()) {
            if (lastUpdate+manager.getTimeToClear()<time) {
                returnToOriginal();
            }
        } else {
            ConfigBlock waiting = manager.getConfigBlocks().get(waitingIndex);
            if (lastUpdate+waiting.getTime()<time) {
                block.setType(waiting.getMaterial());
                if (waiting.getDurability() != null) {
                    block.setData(waiting.getDurability());
                }
                lastUpdate = time;
                waitingIndex++;
            }
        }
    }

    private Boolean checkIsBroken() {
        return block.getType() == Material.AIR;
    }

    public void returnToOriginal() {
        Block block = this.block.getLocation().getBlock();
        block.setType(originalMaterial);
        block.setData(orginalData);
        BlocksManager.getManager().removePlacedBlock(this);
    }

}
