package com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.tesla_primary;


import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class TeslaPrimaryBlock extends Block implements IWrenchable {
    public TeslaPrimaryBlock(Properties p_49795_) {
        super(p_49795_);
    }

    static final VoxelShape TESLA_PRIMARY_SHAPE = Shapes.or(
            Block.box(0,0,0,16,3,16),
            Block.box(1,3,1,15,13,15),
            Block.box(0,13,0,16,16,16)
    );


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TESLA_PRIMARY_SHAPE;
    }
}
