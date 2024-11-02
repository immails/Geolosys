package com.oitsjustjose.geolosys.common.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
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

    public static BlockState readBlockState(Block block, CompoundTag pTag) {
        BlockState blockstate = block.defaultBlockState();
        if (pTag.contains("Properties", 10)) {
            CompoundTag compoundtag = pTag.getCompound("Properties");
            StateDefinition<Block, BlockState> statedefinition = block.getStateDefinition();

            for(String s : compoundtag.getAllKeys()) {
                Property<?> property = statedefinition.getProperty(s);
                if (property != null) {
                    blockstate = setValueHelper(blockstate, property, s, compoundtag, pTag);
                }
            }
        }

        return blockstate;
    }

    private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S pStateHolder, Property<T> pProperty, String pPropertyName, CompoundTag pPropertiesTag, CompoundTag pBlockStateTag) {
        Optional<T> optional = pProperty.getValue(pPropertiesTag.getString(pPropertyName));
        if (optional.isPresent()) {
            return pStateHolder.setValue(pProperty, optional.get());
        } else {
            Geolosys.getInstance().LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", pPropertyName, pPropertiesTag.getString(pPropertyName), pBlockStateTag.toString());
            return pStateHolder;
        }
    }
}
