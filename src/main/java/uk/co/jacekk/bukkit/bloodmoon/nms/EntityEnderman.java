package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderman;
import org.bukkit.plugin.Plugin;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityEndermen;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntityEnderman extends net.minecraft.server.v1_12_R1.EntityEnderman {

    private BloodMoon plugin;
    private BloodMoonEntityEndermen bloodMoonEntity;

    public int bt;
    public boolean bv;

    public EntityEnderman(World world) {
        super(world);

        Plugin gPlugin = Bukkit.getPluginManager().getPlugin("BloodMoon");

        if (gPlugin == null || !(gPlugin instanceof BloodMoon)) {
            this.world.removeEntity(this);
            return;
        }

        this.plugin = (BloodMoon) gPlugin;

        this.bukkitEntity = new CraftEnderman((CraftServer) this.plugin.getServer(), this);
        this.bloodMoonEntity = new BloodMoonEntityEndermen(this.plugin, this, BloodMoonEntityType.ENDERMAN);
    }

    // Is server world?
    @Override
    public boolean cC() {
        try {
            this.bloodMoonEntity.onTick();
            super.cB();
        } catch (Exception e) {
            plugin.getLogger().warning("Exception caught while ticking entity");
            e.printStackTrace();
        }
        return true;
    }

}
