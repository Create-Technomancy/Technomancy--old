package dev.Cosmos616.technomancy.content.contraptions.energy.spectre_coil;

import com.simibubi.create.foundation.block.ITE;
import dev.Cosmos616.technomancy.registry.TMTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.ParametersAreNonnullByDefault;
/*
@ParametersAreNonnullByDefault
public class SpectreCoilBlock extends Block implements ITE<SpectreCoilTileEntity> {

    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    public SpectreCoilBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(TOP, true)
                .setValue(BOTTOM, true));
    }

    public static boolean isCoil(BlockState state) {
        return state.getBlock() instanceof SpectreCoilBlock;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock())
            return;
        if (moved)
            return;
        withTileEntityDo(world, pos, SpectreCoilTileEntity::updateConnectivity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TOP, BOTTOM);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity te = level.getBlockEntity(pos);
            if (!(te instanceof SpectreCoilTileEntity coil))
                return;
            level.removeBlockEntity(pos);
            SpectreCoilConnectivityHandler.splitCoil(coil);
        }
    }

    @Override
    public Class<SpectreCoilTileEntity> getTileEntityClass() {
        return SpectreCoilTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends SpectreCoilTileEntity> getTileEntityType() {
        return TMTileEntities.SPECTRE_COIL.get();
    }
}
*/