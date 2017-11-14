package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.List;
import net.minecraft.server.v1_12_R1.Enchantments;
import net.minecraft.server.v1_12_R1.EnchantmentManager;
import net.minecraft.server.v1_12_R1.EntityArrow;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntitySkeletonAbstract;
import net.minecraft.server.v1_12_R1.EntitySkeletonWither;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.IRangedEntity;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.IProjectile;
import net.minecraft.server.v1_12_R1.PathfinderGoalFleeSun;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_12_R1.PathfinderGoalRestrictSun;
import net.minecraft.server.v1_12_R1.EntityTippedArrow;
import net.minecraft.server.v1_12_R1.SoundEffects;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntitySkeleton;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

//public class EntitySkeleton {
public class EntitySkeleton extends net.minecraft.server.v1_12_R1.EntitySkeleton implements IRangedEntity {

    private BloodMoon plugin;
    private BloodMoonEntitySkeleton bloodMoonEntity;

//    @SuppressWarnings({ "rawtypes", "unchecked" })
	public EntitySkeleton(net.minecraft.server.v1_12_R1.World world) {
        super(world);

        Plugin gPlugin = Bukkit.getPluginManager().getPlugin("BloodMoon");

        if (gPlugin == null || !(gPlugin instanceof BloodMoon)) {
            this.world.removeEntity(this);
            return;
        }
//
//        this.plugin = (BloodMoon) gPlugin;
//
//        this.bukkitEntity = new CraftSkeleton((CraftServer) this.plugin.getServer(), this);
//        this.bloodMoonEntity = new BloodMoonEntitySkeleton(this.plugin, this, BloodMoonEntityType.SKELETON);
//
//        try {
//            ReflectionUtils.getFieldValue(this.goalSelector.getClass(), "b", List.class, this.goalSelector).clear();
//            ReflectionUtils.getFieldValue(this.targetSelector.getClass(), "b", List.class, this.targetSelector).clear();
//
//            this.goalSelector.a(1, new PathfinderGoalFloat(this));
//            this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
//            this.goalSelector.a(3, new PathfinderGoalFleeSun(this, 1.0d));
//            // NOTE: See bJ() below
//            this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0d));
//            this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
//            this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
//
//            this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
//            this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, false, true));
//        } catch (Exception e) {
//            e.printStackTrace();
//            this.world.removeEntity(this);
//        }
    }

    @Override
    // attackEntityWithRangedAttack(EntityLiving target, float distanceFactor)
    public void a(net.minecraft.server.v1_12_R1.EntityLiving target, float f) {
        EntityArrow entityarrow = this.a(f);
        double d0 = target.locX - this.locX;
        //									minY				 height
        double d1 = target.getBoundingBox().b + (double) (target.length / 3.0F) - entityarrow.locY;
        double d2 = target.locZ - this.locZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        // setThrowableHeading
        entityarrow.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, (float) (14 - this.world.getDifficulty().a() * 4));
        // 	 playsound		ENTITY_SKELETON_SHOOT
        this.a(SoundEffects.gW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(entityarrow);
    	
        //String worldName = this.world.worldData.getName();
        //PluginConfig worldConfig = plugin.getConfig(worldName);
        World bukkitWorld = this.world.worldData.world.getWorld();
        PluginConfig worldConfig = plugin.getConfig(bukkitWorld);

        if (plugin.isActive(bukkitWorld) && worldConfig.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED) && (this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_FIRE_ARROWS_CHANCE))
                || (EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_FIRE, this.b(EnumHand.MAIN_HAND)) > 0)) {
            final EntityCombustEvent event = new EntityCombustEvent(entityarrow.getBukkitEntity(), 100);
            this.world.getServer().getPluginManager().callEvent(event);
        }
    }
}
