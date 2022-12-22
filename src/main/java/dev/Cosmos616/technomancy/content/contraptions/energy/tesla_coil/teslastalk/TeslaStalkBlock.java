package dev.Cosmos616.technomancy.content.contraptions.energy.tesla_coil.teslastalk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TeslaStalkBlock extends Block {
    public TeslaStalkBlock(Properties p_49795_) {
        super(p_49795_);
    }
    static final VoxelShape TESLA_STALK_SHAPE = Shapes.or(
     Block.box(5,0,5,11,16,11)
    );

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TESLA_STALK_SHAPE;
    }
}
