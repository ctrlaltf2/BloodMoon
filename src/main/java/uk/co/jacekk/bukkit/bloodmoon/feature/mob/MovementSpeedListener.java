package uk.co.jacekk.bukkit.bloodmoon.feature.mob;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityLiving;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class MovementSpeedListener implements Listener {

    private final BloodMoon plugin;
    private final Random random = new Random();

    public MovementSpeedListener(BloodMoon plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStart(BloodMoonStartEvent event) {
        World world = event.getWorld();
        PluginConfig worldConfig = plugin.getConfig(world);

        if (plugin.isFeatureEnabled(world, Feature.MOVEMENT_SPEED)) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (worldConfig.getStringList(Config.FEATURE_MOVEMENT_SPEED_MOBS).contains(entity.getType().name())) {
                    try {
                        BloodMoonEntityLiving bloodMoonEntity = BloodMoonEntityLiving.getBloodMoonEntity(((CraftLivingEntity) entity).getHandle());
                        double multiplier = worldConfig.getDouble((this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_MOVEMENT_SPEED_FAST_CHANCE)) ? Config.FEATURE_MOVEMENT_SPEED_FAST_MULTIPLIER : Config.FEATURE_MOVEMENT_SPEED_MULTIPLIER);
                        bloodMoonEntity.setAttributeMultiplier(multiplier, GenericAttributes.MOVEMENT_SPEED);
                    } catch (IllegalArgumentException e) {
                        // This means the entity is not supported *shrug*
                    } catch (Exception e) {
                    	plugin.getLogger().warning(entity.getName() + " is not supported for movement-speed!");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStop(BloodMoonEndEvent event) {
        World world = event.getWorld();

        if (plugin.isFeatureEnabled(world, Feature.MOVEMENT_SPEED)) {
            for (LivingEntity entity : world.getLivingEntities()) {
                try {
                    BloodMoonEntityLiving.getBloodMoonEntity(((CraftLivingEntity) entity).getHandle()).clearAttributeMultiplier(GenericAttributes.MOVEMENT_SPEED);
                } catch (IllegalArgumentException e) {
                    // This means the entity is not supported *shrug*
                } catch (Exception e) {
                	plugin.getLogger().warning(entity.getName() + " is not supported for movement-speed!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        World world = entity.getWorld();
        PluginConfig worldConfig = plugin.getConfig(world);

        if (plugin.isActive(world) && plugin.isFeatureEnabled(world, Feature.MOVEMENT_SPEED)) {
            if (worldConfig.getStringList(Config.FEATURE_MOVEMENT_SPEED_MOBS).contains(entity.getType().name())) {
                try {
                    BloodMoonEntityLiving bloodMoonEntity = BloodMoonEntityLiving.getBloodMoonEntity(((CraftLivingEntity) entity).getHandle());
                    double multiplier = worldConfig.getDouble((this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_MOVEMENT_SPEED_FAST_CHANCE)) ? Config.FEATURE_MOVEMENT_SPEED_FAST_MULTIPLIER : Config.FEATURE_MOVEMENT_SPEED_MULTIPLIER);
                    bloodMoonEntity.setAttributeMultiplier(multiplier, GenericAttributes.MOVEMENT_SPEED);
                } catch (IllegalArgumentException e) {
                	plugin.getLogger().warning(entity.getName() + " is not supported for movement-speed!");
                } catch (Exception e) {
                	plugin.getLogger().warning(entity.getName() + " is not supported for movement-speed!");
                }
            }
        }
    }

}
