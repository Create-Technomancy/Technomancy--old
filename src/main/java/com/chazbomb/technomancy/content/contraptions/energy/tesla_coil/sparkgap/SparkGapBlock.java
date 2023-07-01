package com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.sparkgap;


import com.chazbomb.technomancy.registry.TMBlockEntities;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SparkGapBlock extends KineticBlock implements IBE<SparkGapBlockEntity>, ICogWheel {
    public SparkGapBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<SparkGapBlockEntity> getBlockEntityClass() {
        return SparkGapBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SparkGapBlockEntity> getBlockEntityType() {
        return TMBlockEntities.SPARK_GAP.get();
    }

    static final VoxelShape SPARK_GAP_SHAPE = Shapes.or(
            Block.box(0,0,0,16,6,16),
            Block.box(2, 6, 2, 14, 10, 14),
            Block.box(0,10,0,16,16,16)
    );

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SPARK_GAP_SHAPE;
    }
}



//among_us