package net.rapust.blockreset.block;

import lombok.Getter;
import org.bukkit.Material;

public class ConfigBlock {

    @Getter
    private final Material material;
    @Getter
    private final Byte durability;
    @Getter
    private final Long time;

    public ConfigBlock(Material material, Byte durability, Long time) {
        this.material = material;
        this.durability = durability;
        this.time = time;
    }

}
