package uk.co.jacekk.bukkit.bloodmoon.entity;

import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.IAttribute;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class AttributeDefaults {
	public static double fromGeneric(IAttribute gen_attr, BloodMoonEntityType entity, BloodMoon plugin) {
		if(gen_attr.toString().equals(GenericAttributes.maxHealth.toString()))
			return entity.maxHealth();
		else if(gen_attr.toString().equals(GenericAttributes.FOLLOW_RANGE.toString()))
			return 32.0;
		else if(gen_attr.toString().equals(GenericAttributes.c.toString())) // Knockback resist
			return 0.0;
		else if(gen_attr.toString().equals(GenericAttributes.MOVEMENT_SPEED.toString()))
			return entity.movementSpeed();
		else if(gen_attr.toString().equals(GenericAttributes.e.toString())) // TODO: What is this attribute?
			return 0.0;
		else if(gen_attr.toString().equals(GenericAttributes.ATTACK_DAMAGE.toString()))
			return 2.0; // TODO: Varies on both mob and difficulty
		else if(gen_attr.toString().equals(GenericAttributes.g.toString())) // attack speed
			return 4.0;
		else if(gen_attr.toString().equals(GenericAttributes.h.toString())) // armor
			return 0.0; // Most mobs are 0.0 this won't matter much
		else if(gen_attr.toString().equals(GenericAttributes.i.toString())) // armor toughness
			return 0.0;
		else if(gen_attr.toString().equals(GenericAttributes.j.toString())) // luck
			return 0.0;
		else {
			plugin.getLogger().warning(gen_attr.toString() + " didn't match any generic attributes; " + GenericAttributes.i.toString() + ' ' + GenericAttributes.FOLLOW_RANGE.toString());
			return 0.0;
		}
		/* 
  public static final net.minecraft.server.v1_12_R1.IAttribute maxHealth;
  public static final net.minecraft.server.v1_12_R1.IAttribute FOLLOW_RANGE;
  public static final net.minecraft.server.v1_12_R1.IAttribute c; // kb resist
  public static final net.minecraft.server.v1_12_R1.IAttribute MOVEMENT_SPEED;
  public static final net.minecraft.server.v1_12_R1.IAttribute e; // ???
  public static final net.minecraft.server.v1_12_R1.IAttribute ATTACK_DAMAGE;
  public static final net.minecraft.server.v1_12_R1.IAttribute g; // attack speed
  public static final net.minecraft.server.v1_12_R1.IAttribute h; // armor
  public static final net.minecraft.server.v1_12_R1.IAttribute i; // armor toughness
  public static final net.minecraft.server.v1_12_R1.IAttribute j; // luck */
	}
}
