package com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.coil_topload;


import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoilToploadBlock extends Block implements IWrenchable {


    public CoilToploadBlock(Properties p_49795_) {super(p_49795_);}
    static final VoxelShape COIL_TOPLOAD = Shapes.or(
            Block.box(5,0,5,11,16,11)
    );

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return COIL_TOPLOAD;
    }
}

