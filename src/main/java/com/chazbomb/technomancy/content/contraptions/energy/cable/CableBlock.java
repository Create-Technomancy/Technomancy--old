package com.chazbomb.technomancy.content.contraptions.energy.cable;

import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.chazbomb.technomancy.foundation.energy.AetherTransportBehaviour;
import com.chazbomb.technomancy.registry.TMBlocks;
import com.chazbomb.technomancy.registry.TMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

public class CableBlock extends PipeBlock
    implements SimpleWaterloggedBlock, /*IWrenchableWithBracket, */IBE<CableBlockEntity>, IWrenchable {

    public CableBlock(Properties properties) {
        super(3 / 16f, properties);
        this.registerDefaultState(super.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

//    @Override
//    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
//        tryRemoveBracket(context);
//        return InteractionResult.SUCCESS;
//    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!TMBlocks.ZIRCONIUM_CASING.isIn(player.getItemInHand(hand)))
            return InteractionResult.PASS;
        if (world.isClientSide)
            return InteractionResult.SUCCESS;

        world.setBlockAndUpdate(pos, EncasedCableBlock.transferSixWayProperties(state, TMBlocks.ENCASED_CABLE_BLOCK.getDefaultState()));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean blockTypeChanged = state.getBlock() != newState.getBlock();

        if (blockTypeChanged && !world.isClientSide)
            CablePropagator.propagateChangedCable(world, pos, state);

//        if (state != newState && !isMoving)
//            removeBracket(world, pos, true).ifPresent(stack -> Block.popResource(world, pos, stack));

        if (state.hasBlockEntity() && (blockTypeChanged || !newState.hasBlockEntity()))
            world.removeBlockEntity(pos);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (world.isClientSide)
            return;
        if (state != oldState)
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos neighborPos, boolean isMoving) {
        DebugPackets.sendNeighborsUpdatePacket(world, pos);
        CablePropagator.resetAffectedCableNetworks(world, pos);
        Direction d = CablePropagator.validateNeighbourChange(state, world, pos, otherBlock, neighborPos, isMoving);
        if (d == null)
            return;
        if (!isOpenAt(state, d))
            return;
        world.scheduleTick(pos, this, 1, TickPriority.HIGH);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random r) {
        CablePropagator.propagateChangedCable(world, pos, state); // idk, refresh network connections?
    }

    public static boolean isCable(BlockState state) {
        return state.getBlock() instanceof CableBlock;
    }

    // check if can branch to neighbor - i.e. pipe<->tank
    public static boolean canConnectTo(BlockAndTintGetter world, BlockPos neighbourPos, BlockState neighbour, Direction direction) {
        if (CablePropagator.hasAetherCapability(world, neighbourPos, direction.getOpposite()))
            return true;
        AetherTransportBehaviour transport = BlockEntityBehaviour.get(world, neighbourPos, AetherTransportBehaviour.TYPE);
        BracketedBlockEntityBehaviour bracket =
                BlockEntityBehaviour.get(world, neighbourPos, BracketedBlockEntityBehaviour.TYPE);
        if (isCable(neighbour))
            return bracket == null || !bracket.isBracketPresent()
                    || CablePropagator.getStraightCableAxis(neighbour) == direction.getAxis();
        if (transport == null)
            return false;
        return transport.canFluxToward(neighbour, direction.getOpposite());
    }

    public static boolean shouldDrawRim(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        BlockPos offsetPos = pos.relative(direction);
        BlockState facingState = world.getBlockState(offsetPos);
        if (facingState.getBlock() instanceof EncasedCableBlock)
            return true;
        if (!isCable(facingState))
            return true;
        if (!canConnectTo(world, offsetPos, facingState, direction))
            return true;
        return false;
    }

    // if there's a branch to that side
    public static boolean isOpenAt(BlockState state, Direction direction) {
        return state.getValue(PROPERTY_BY_DIRECTION.get(direction));
    }

//    public static boolean isCornerOrEndCable(BlockAndTintGetter world, BlockPos pos, BlockState state) {
//        return isCable(state) // is cable
//                && CablePropagator.getStraightCableAxis(state) != null // is not straight (incl. T-junction)
//                && !shouldDrawCasing(world, pos, state); // no casing
//    }

    public static boolean shouldDrawCasing(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        if (!isCable(state))
            return false;
        for (Direction.Axis axis : Iterate.axes) {
            int connections = 0;
            for (Direction direction : Iterate.directions)
                if (direction.getAxis() != axis && isOpenAt(state, direction))
                    connections++;
            if (connections > 2)
                return true;
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState FluidState = context.getLevel()
                .getFluidState(context.getClickedPos());
        return updateBlockState(defaultBlockState(), context.getNearestLookingDirection(), null, context.getLevel(),
                context.getClickedPos()).setValue(BlockStateProperties.WATERLOGGED,
                FluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED))
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        if (isOpenAt(state, direction) && neighbourState.hasProperty(BlockStateProperties.WATERLOGGED))
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);
        return updateBlockState(state, direction, direction.getOpposite(), world, pos);
    }

    public BlockState updateBlockState(BlockState state, Direction preferredDirection, @Nullable Direction ignore, BlockAndTintGetter world, BlockPos pos) {

        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (bracket != null && bracket.isBracketPresent())
            return state;

        BlockState prevState = state;
        int prevStateSides = (int) Arrays.stream(Iterate.directions)
                .map(PROPERTY_BY_DIRECTION::get)
                .filter(prevState::getValue)
                .count();

        // Update sides that are not ignored
        for (Direction d : Iterate.directions)
            if (d != ignore) {
                boolean shouldConnect = canConnectTo(world, pos.relative(d), world.getBlockState(pos.relative(d)), d);
                state = state.setValue(PROPERTY_BY_DIRECTION.get(d), shouldConnect);
            }

        // See if it has enough connections
        Direction connectedDirection = null;
        for (Direction d : Iterate.directions) {
            if (isOpenAt(state, d)) {
                if (connectedDirection != null)
                    return state;
                connectedDirection = d;
            }
        }

        // Add opposite end if only one connection
        if (connectedDirection != null)
            return state.setValue(PROPERTY_BY_DIRECTION.get(connectedDirection.getOpposite()), true);

        // If we can't connect to anything and weren't connected before, do nothing
        if (prevStateSides == 2)
            return prevState;

        // Use preferred
        return state.setValue(PROPERTY_BY_DIRECTION.get(preferredDirection), true)
                .setValue(PROPERTY_BY_DIRECTION.get(preferredDirection.getOpposite()), true);
    }

//    @Override
//    public Optional<ItemStack> removeBracket(BlockGetter world, BlockPos pos, boolean inOnReplacedContext) {
//        BracketedTileEntityBehaviour behaviour =
//            BracketedTileEntityBehaviour.get(world, pos, BracketedTileEntityBehaviour.TYPE);
//        if (behaviour == null)
//            return Optional.empty();
//        BlockState bracket = behaviour.removeBracket(inOnReplacedContext);
//        if (bracket == null)
//            return Optional.empty();
//        return Optional.of(new ItemStack(bracket.getBlock()));
//    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<CableBlockEntity> getBlockEntityClass() {
        return CableBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CableBlockEntity> getBlockEntityType() {
        return TMBlockEntities.CABLE.get();
    }

}
