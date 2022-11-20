package dev.Cosmos616.technomancy.content.curiosities.weapons.firearm;

import com.simibubi.create.content.curiosities.zapper.ZapperItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public class AbstractFirearmItem extends Item {
	public AbstractFirearmItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack firearm = new ItemStack(this);
		firearm.getOrCreateTag().putInt("Ammunition", 0);
		return firearm;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack firearm = player.getItemInHand(hand);
		// todo fire gun thingy
		
		return InteractionResultHolder.pass(firearm);
	}
}
