package com.oitsjustjose.geolosys.common.items;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class CoalItem extends Item {

    private int burnTime;

    public CoalItem(int burnTime) {
        super(new Item.Properties().fireResistant());
        this.burnTime = burnTime * 100;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTime;
    }
}
