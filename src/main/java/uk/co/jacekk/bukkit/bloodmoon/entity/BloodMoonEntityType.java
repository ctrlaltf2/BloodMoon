package uk.co.jacekk.bukkit.bloodmoon.entity;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.exceptions.EntityRegistrationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum BloodMoonEntityType {

	// Name, technical name, id, nms class, custom class, health, movement speed
    CREEPER("Creeper", "creeper", 50, EntityType.CREEPER,net.minecraft.server.v1_12_R1.EntityCreeper.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityCreeper.class, 20.0, 0.25),
    SKELETON("Skeleton","skeleton",51, EntityType.SKELETON,net.minecraft.server.v1_12_R1.EntitySkeleton.class,uk.co.jacekk.bukkit.bloodmoon.nms.EntitySkeleton.class, 20.0, 0.25),
    SPIDER("Spider", "spider", 52, EntityType.SPIDER,net.minecraft.server.v1_12_R1.EntitySpider.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntitySpider.class, 16.0, 0.3),
    GIANT_ZOMBIE("Giant", "giant", 53, EntityType.GIANT, net.minecraft.server.v1_12_R1.EntityGiantZombie.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityGiantZombie.class, 100.0, 0.5),
    ZOMBIE("Zombie", "zombie", 54, EntityType.ZOMBIE, net.minecraft.server.v1_12_R1.EntityZombie.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityZombie.class, 20.0, 0.23),
    GHAST("Ghast", "ghast", 56, EntityType.GHAST, net.minecraft.server.v1_12_R1.EntityGhast.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityGhast.class, 10.0, 0.7),
    ENDERMAN("Enderman","enderman",58, EntityType.ENDERMAN,net.minecraft.server.v1_12_R1.EntityEnderman.class,uk.co.jacekk.bukkit.bloodmoon.nms.EntityEnderman.class, 40.0, 0.3),
    BLAZE("Blaze", "blaze", 61, EntityType.BLAZE, net.minecraft.server.v1_12_R1.EntityBlaze.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityBlaze.class, 20.0, 0.23),
    WITHER("WitherBoss", "wither", 64, EntityType.WITHER, net.minecraft.server.v1_12_R1.EntityWither.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityWither.class, 300.0, 0.6),
    WITCH("Witch", "witch", 66, EntityType.WITCH, net.minecraft.server.v1_12_R1.EntityWitch.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityWitch.class, 26.0, 0.25);

    private final String name;
    private final String technical_name;
    private final int id;
    private final EntityType entityType;
    private final Class<? extends EntityInsentient> nmsClass;
    private final Class<? extends EntityInsentient> bloodMoonClass;
    private final double max_health;
    private final double move_speed;

    private static boolean registered = false;

    private BloodMoonEntityType(String name, String technical_name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> bloodMoonClass, double max_health, double move_speed) {
        this.name = name;
        this.technical_name = technical_name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.bloodMoonClass = bloodMoonClass;
        this.max_health = max_health;
        this.move_speed = move_speed;
    }

    @SuppressWarnings("unchecked")
    public static void registerEntities() throws EntityRegistrationException {
        if (registered) {
            throw new EntityRegistrationException("Already registered.");
        }

        RegistryMaterials<MinecraftKey, Class<?>> regMap;
        Map<Class<?>, Class<?>> idMap;
        List<String> nameMap;

        // Get map of registered names and ids
        try {
        	regMap = ReflectionUtils.getFieldValue(EntityTypes.class, "b", RegistryMaterials.class, null);
            idMap = ReflectionUtils.getFieldValue(RegistryMaterials.class, "b", Map.class, regMap);
            nameMap = ReflectionUtils.getFieldValue(EntityTypes.class, "g", List.class, null);
        } catch (Exception e) {
            throw new EntityRegistrationException("Failed to get existing entity maps.", e);
        }

        // remove them and replace them with bloodmoon types
        for (BloodMoonEntityType entity : values()) {
            try {
                nameMap.remove(entity.getName());
                idMap.remove(entity.getID());

                ReflectionUtils.invokeMethod(EntityTypes.class, "a", Void.class, null, new Class<?>[]{int.class, String.class, Class.class, String.class}, new Object[]{entity.getID(), entity.getTechnicalName(), entity.getBloodMoonClass(), entity.getName()});
            } catch (Exception e) {
                throw new EntityRegistrationException("Failed to call EntityTypes.a() for " + entity.getName(), e);
            }
        }

        
        ArrayList<Integer> biomeIDs = new ArrayList<Integer>();
        
        // 1 - 39
        for(int i = 0; i < 40;++i)
        	biomeIDs.add(i);
        
        biomeIDs.add(127); // the void
        
        for(int i = 129; i < 135;++i)
        	biomeIDs.add(i);
        
        biomeIDs.add(140);
        biomeIDs.add(149);
        biomeIDs.add(151);
        biomeIDs.add(155);
        biomeIDs.add(156);
        biomeIDs.add(157);
        biomeIDs.add(158);
        
        for(int i = 160; i < 168;++i)
        	biomeIDs.add(i);

        for (int ID : biomeIDs) {
        	BiomeBase biomebase = BiomeBase.getBiome(ID);

            //for (String field : new String[]{"as", "at", "au", "av"}){
            // TODO: getMobs(EnumCreatureType) instead of reflection
            for (EnumCreatureType T : new EnumCreatureType[] {EnumCreatureType.AMBIENT, EnumCreatureType.CREATURE, EnumCreatureType.MONSTER, EnumCreatureType.WATER_CREATURE}) {
                try {
                    //System.out.println(field);
                    @SuppressWarnings("static-access")
					List<BiomeBase.BiomeMeta> mobList = biomebase.getMobs(T);
//					@SuppressWarnings("unchecked")
                    //List<BiomeMeta> mobList = (List<BiomeMeta>) ReflectionUtils.getFieldValue(BiomeBase.class, field, BiomeBase.class, biomeBase);

                    // XXX: Possible failure, check .b is correct
                    for (BiomeBase.BiomeMeta meta : mobList) {
                        for (BloodMoonEntityType entity : values()) {
                            if (entity.getNMSClass().equals(meta.b)) {
                                meta.b = entity.getBloodMoonClass();
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new EntityRegistrationException("Failed to modify biome data field " + T.toString(), e);
                }
            }
        }

        registered = true;
    }

    public String getName() {
        return this.name;
    }
    
    public String getTechnicalName() {
    	return this.technical_name;
    }

    public int getID() {
        return this.id;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Class<? extends EntityInsentient> getNMSClass() {
        return this.nmsClass;
    }

    public Class<? extends EntityInsentient> getBloodMoonClass() {
        return this.bloodMoonClass;
    }

    private EntityInsentient createEntity(World world) {
        try {
            return this.getBloodMoonClass().getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void spawnEntity(Location location) {
        World world = ((CraftWorld) location.getWorld()).getHandle();

        EntityInsentient entity = this.createEntity(world);
        entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        world.addEntity(entity, SpawnReason.CUSTOM);
        //TODO: Figure out what this does, removing this call
//        entity.p(null);
    }
    
    public double maxHealth() {
    	return this.max_health;
    }
    
    public double movementSpeed() {
    	return this.move_speed;
    }
}
