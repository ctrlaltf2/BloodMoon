package uk.co.jacekk.bukkit.bloodmoon.entity;

import java.util.Random;
import java.util.UUID;

import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.AttributeModifier;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.IAttribute;

import org.bukkit.Location;
import org.bukkit.World;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public abstract class BloodMoonEntityLiving {

    private static final UUID maxHealthUID = UUID.fromString("f8b0a945-2d6a-4bdb-9a6f-59c285bf1e5d");
    private static final UUID followRangeUID = UUID.fromString("1737400d-3c18-41ba-8314-49a158481e1e");
    private static final UUID knockbackResistanceUID = UUID.fromString("8742c557-fcd5-4079-a462-b58db99b0f2c");
    private static final UUID movementSpeedUID = UUID.fromString("206a89dc-ae78-4c4d-b42c-3b31db3f5a7c");
    private static final UUID attackDamageUID = UUID.fromString("7bbe3bb1-079d-4150-ac6f-669e71550776");

    protected BloodMoon plugin;
    protected EntityLiving nmsEntity;
    protected Entity bukkitEntity;
    protected BloodMoonEntityType type;

    protected Random rand = new Random();

    public BloodMoonEntityLiving(BloodMoon plugin, EntityLiving nmsEntity, BloodMoonEntityType type) {
        this.plugin = plugin;
        this.nmsEntity = nmsEntity;
        this.bukkitEntity = nmsEntity.getBukkitEntity();
        this.type = type;
    }

    public static BloodMoonEntityLiving getBloodMoonEntity(EntityLiving nmsEntity) {
        try {
            return ReflectionUtils.getFieldValue(nmsEntity.getClass(), "bloodMoonEntity", BloodMoonEntityLiving.class, nmsEntity);
        } catch (Exception e) {
            throw new IllegalArgumentException(nmsEntity.getClass().getName() + " not supported");
        }
    }
    
    public void setAttributeMultiplier(double multiplier, IAttribute attribute_enum) {
        try {
            AttributeInstance theAttribute = this.nmsEntity.getAttributeInstance(attribute_enum);
            if (theAttribute == null) {
                this.plugin.getLogger().warning("Set " + attribute_enum.toString() + " on entity " + this.nmsEntity.getName() + " failed");
                return;
            } 
            double oldValue = theAttribute.getValue();
            theAttribute.setValue(oldValue * multiplier);
        } catch (Exception e) {
//            this.plugin.getLogger().warning("Set " + attribute_enum.toString() + " on entity " + this.nmsEntity.getName() + " failed. This entity likely isn't supported for this attribute and should be removed from the config");
        }
    }
    
    public void clearAttributeMultiplier(IAttribute attribute_enum) {
        try {
            AttributeInstance theAttribute = this.nmsEntity.getAttributeInstance(attribute_enum);
            if (theAttribute == null) {
//                this.plugin.getLogger().warning("Clear " + attribute_enum.toString() + " on entity " + this.nmsEntity.getName() + " failed");
                return;
            }
            theAttribute.setValue(AttributeDefaults.fromGeneric(attribute_enum, this.type, plugin));
        } catch (Exception e) {
//            this.plugin.getLogger().warning("Clear " + attribute_enum.toString() + " on entity " + this.nmsEntity.getName() + " failed. This entity likely isn't supported for this attribute and should be removed from the config");
        }
    }

    //public abstract void onTick();
    public void onTick() {

    }

    public World getBukkitWorld() {
        return nmsEntity.getWorld().getWorld();
    }

    public String getWorldName() {
        return nmsEntity.getWorld().getWorld().getName();
    }

    public Location getLocation() {
        return bukkitEntity.getLocation();
    }

    public EntityType getEntityType() {
        return bukkitEntity.getType();
    }

}