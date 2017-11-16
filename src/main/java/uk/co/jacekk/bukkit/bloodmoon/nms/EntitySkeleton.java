package uk.co.jacekk.bukkit.bloodmoon.nms;

import net.minecraft.server.v1_12_R1.Enchantments;
import net.minecraft.server.v1_12_R1.EnchantmentManager;
import net.minecraft.server.v1_12_R1.EntityArrow;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.IRangedEntity;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.SoundEffects;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntitySkeleton;

//public class EntitySkeleton {
public class EntitySkeleton extends net.minecraft.server.v1_12_R1.EntitySkeleton implements IRangedEntity {

    private Plugin plugin;
    private BloodMoonEntitySkeleton bloodMoonEntity;

//    @SuppressWarnings({ "rawtypes", "unchecked" })
	public EntitySkeleton(net.minecraft.server.v1_12_R1.World world) {
        super(world);

        Plugin gPlugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
        plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
        
        if (gPlugin == null || !(gPlugin instanceof BloodMoon)) {
            this.world.removeEntity(this);
            return;
        }
    }

    @Override
    // attackEntityWithRangedAttack(EntityLiving target, float distanceFactor)
    public void a(net.minecraft.server.v1_12_R1.EntityLiving target, float f) {
		int barrage_amount = 1;
		boolean onFire = false;
        
    	if(plugin != null) {
    		try {
	    		BloodMoon bPlugin = (BloodMoon) plugin;
				World bukkitWorld = this.world.worldData.world.getWorld();
				PluginConfig worldConfig = bPlugin.getConfig(bukkitWorld);
	    		
	    		if (bPlugin.isActive(bukkitWorld)) {
	    			if(worldConfig.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED) &&
	    					(this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_FIRE_ARROWS_CHANCE))
	    		        || (EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_FIRE, super.b(EnumHand.MAIN_HAND)) > 0)) {
	    				onFire = true;
	    			}
	    			
	    			if(worldConfig.getBoolean(Config.FEATURE_SKELETON_VOLLEY_ENABLED) &&
	    					(this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_SKELETON_VOLLEY_CHANCE))) {
	    				barrage_amount = worldConfig.getInt(Config.FEATURE_SKELETON_VOLLEY_AMOUNT);
	    			}
	    		}
    		} catch (Exception e) {
    			plugin.getLogger().warning("Exception caught while attempting to modify skeleton attack:" + e);
    			e.printStackTrace();
    		}
    	}
    	
    	
    	this.fire(target, f, barrage_amount, onFire);
    }
    	
  
    public void fire(net.minecraft.server.v1_12_R1.EntityLiving target, float distanceFactor, int amount, boolean onFire) {
    	float accuracy = 14 - this.world.getDifficulty().a() * 4;
    	float velocity = 1.6F;
    	
    	if(amount > 1)
    		accuracy *= 2;
    	
    	for(int i = 0; i < amount;++i) {
	    	EntityArrow entityarrow = this.a(distanceFactor);
	        double d0 = target.locX - this.locX;
	        //									minY				 height
	        double d1 = target.getBoundingBox().b + (double) (target.length / 3.0F) - entityarrow.locY;
	        double d2 = target.locZ - this.locZ;
	        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
	        // setThrowableHeading
	        entityarrow.shoot(d0, d1 + d3 * 0.2D, d2, velocity, accuracy);
	        
	        if(onFire)
	        	entityarrow.setOnFire(100);
	        
	        // 	 playsound		ENTITY_SKELETON_SHOOT
	        this.a(SoundEffects.gW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	        this.world.addEntity(entityarrow);
    	}
    }
}
