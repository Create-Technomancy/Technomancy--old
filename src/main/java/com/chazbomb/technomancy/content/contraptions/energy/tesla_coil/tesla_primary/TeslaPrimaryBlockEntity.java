package com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.tesla_primary;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TeslaPrimaryBlockEntity extends BlockEntity {
    public TeslaPrimaryBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }
}

