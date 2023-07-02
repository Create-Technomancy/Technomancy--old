package com.chazbomb.technomancy.content.contraptions.energy.ionized_bulb;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IonizedBulbBlock extends Block {
    public IonizedBulbBlock(Properties p_49795_) {
        super(p_49795_);
    }

    static final VoxelShape IONIZED_BULB_SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 2, 12),
            Block.box(5, 2, 5, 11, 10, 11)
    );

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return IONIZED_BULB_SHAPE;
    }
}
