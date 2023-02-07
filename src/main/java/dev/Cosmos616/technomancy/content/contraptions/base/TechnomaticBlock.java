package dev.Cosmos616.technomancy.content.contraptions.base;

import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import dev.Cosmos616.technomancy.foundation.energy.IConduct;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TechnomaticBlock extends Block implements IConduct {

    public TechnomaticBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof TechnomaticTileEntity technomaticTileEntity) {
            technomaticTileEntity.preventEmissionUpdate = 0;

            if (oldState.getBlock() != state.getBlock())
                return;
            if (state.hasBlockEntity() != oldState.hasBlockEntity())
                return;
            if (!areStatesMagicallyEquivalent(oldState, state))
                return;

            technomaticTileEntity.preventEmissionUpdate = 2;
        }
    }

    @Override
    public boolean hasConnectionTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return false;
    }

    protected boolean areStatesMagicallyEquivalent(BlockState oldState, BlockState newState) {
        if (oldState.getBlock() != newState.getBlock())
            return false;
        return getConnections(newState) == getConnections(oldState);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState stateIn, LevelAccessor worldIn, BlockPos pos, int flags,
                                              int count) {
        if (worldIn.isClientSide())
            return;

        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof TechnomaticTileEntity))
            return;
        TechnomaticTileEntity tte = (TechnomaticTileEntity) tileEntity;

        if (tte.preventEmissionUpdate > 0)
            return;

        // Remove previous information when block is added
        tte.warnOfMovement();
        tte.clearMagicInformation();
        tte.updateEmission = true;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        AdvancementBehaviour.setPlacedBy(worldIn, pos, placer);
//        if (worldIn.isClientSide)
//            return;
//
//        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
//        if (!(tileEntity instanceof TechnomaticTileEntity tte))
//            return;
//
//        tte.effects.queueEmissionIndicators();
    }

    public float getParticleTargetRadius() {
        return .65f;
    }

    public float getParticleInitialRadius() {
        return .75f;
    }

}