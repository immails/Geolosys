package com.oitsjustjose.geolosys.common.items;

import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DepthMeterItem extends Item {

	public DepthMeterItem(Properties pProperties) {
		super(pProperties);
	}

	@OnlyIn(Dist.CLIENT)
	public InteractionResultHolder<ItemStack> use(@Nonnull Level pLevel, @Nonnull Player pPlayer, @Nonnull InteractionHand pUsedHand) {
		if (pLevel.isClientSide) {
			final int depth = pLevel.getSeaLevel() - pPlayer.blockPosition().getY();
			pPlayer.displayClientMessage(
				Component.translatable(
					depth < 0 ? "geolosys.pro_pick.depth.above" 
					: depth > 0 ? "geolosys.pro_pick.depth.below" 
					: "geolosys.pro_pick.depth.at", Math.abs(depth)
				), true);
		}
		ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
		return InteractionResultHolder.pass(itemstack);
   	}
	
}
