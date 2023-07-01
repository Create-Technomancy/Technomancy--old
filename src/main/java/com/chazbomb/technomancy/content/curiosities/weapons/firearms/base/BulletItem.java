package com.chazbomb.technomancy.content.curiosities.weapons.firearms.base;

import net.minecraft.world.item.Item;

public class BulletItem extends Item {
	public BulletItem(Properties properties, AmmoType ammo) {
		super(properties);
		this.ammunition = ammo;
	}
	
	protected AmmoType ammunition;
	public AmmoType getAmmo() {
		return this.ammunition;
	}
	
	public enum AmmoType {
		DEFAULT("default");
		
		public final String id;
		
		AmmoType(String tag) {
			this.id = tag;
		}
	}
}
