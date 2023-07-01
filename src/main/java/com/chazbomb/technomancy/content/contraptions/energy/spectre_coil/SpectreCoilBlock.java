package com.chazbomb.technomancy.content.contraptions.energy.spectre_coil;

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