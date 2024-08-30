package com.oitsjustjose.geolosys.common.utils;

import java.util.HashSet;
import java.util.Objects;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class Utils {
    public static ItemStack blockStateToStack(BlockState state) {
        return new ItemStack(state.getBlock().asItem(), 1);
    }

    public static boolean doStatesMatch(BlockState state1, BlockState state2) {
        return getRegistryName(state1).equals(getRegistryName(state2));
    }

    public static String getRegistryName(Block block) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString();
    }

    public static String getRegistryName(BlockState state) {
        return getRegistryName(state.getBlock());
    }

    public static BlockPos getTopSolidBlock(LevelReader world, BlockPos start) {
        BlockPos retPos = new BlockPos(start.getX(), world.getHeight() - 1, start.getZ());
        while (retPos.getY() > 0) {
            if (world.getBlockState(retPos).isSolidRender(world, retPos)) {
                break;
            }
            retPos = retPos.below();
        }
        return retPos;
    }

    public static MutableComponent tryTranslate(String transKey, Object... values) {
        try {
            TranslatableContents contents = new TranslatableContents(transKey, transKey, values);
            return contents.resolve(null, null, 0);
        } catch (CommandSyntaxException ex) {
            return Component.empty().append(transKey);
        }
    }

    private static MutableComponent combineBlocksToComponent(final HashSet<String> blocks) {
        MutableComponent component = Component.empty();
        Integer i = 0;
        for (String block : blocks) {
            component.append(Component.translatable("block." + block).withStyle(ChatFormatting.GOLD));
            i++;
            if (i < blocks.size()) component.append(Component.literal(" & ").withStyle(ChatFormatting.GRAY));
        }
        return component;
    }

    public static MutableComponent getProspectingTranslatedComponent(final HashSet<String> blocks) {
        return MutableComponent.create(
            new TranslatableContents("geolosys.pro_pick.tooltip.found_surface", null, new Object[]{
                combineBlocksToComponent(blocks), 
        })).withStyle(ChatFormatting.GRAY);
    }

    public static MutableComponent getProspectingTranslatedComponent(final HashSet<String> blocks, final Integer direction) {
        return MutableComponent.create(
            new TranslatableContents("geolosys.pro_pick.tooltip.found", null, new Object[]{
                combineBlocksToComponent(blocks), 
                Component.translatable("item.geolosys.pro_pick.direction." + Integer.valueOf(direction))
                    .withStyle(ChatFormatting.WHITE)
        })).withStyle(ChatFormatting.GRAY);
    }
}
