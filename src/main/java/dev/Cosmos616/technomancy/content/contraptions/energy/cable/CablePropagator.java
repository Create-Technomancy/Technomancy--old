package dev.Cosmos616.technomancy.content.contraptions.energy.cable;

import com.simibubi.create.content.contraptions.fluids.pipes.AxisPipeBlock;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import dev.Cosmos616.technomancy.foundation.energy.CompositeEnergyStorage;
import dev.Cosmos616.technomancy.registry.TMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CablePropagator {

    public static void propagateChangedCable(LevelAccessor world, BlockPos cablePos, BlockState cableState) {
        List<BlockPos> frontier = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Set<Pair<BlockEntity, Direction>> holders = new HashSet<>();
        Set<Pair<CableBlockEntity, BlockEntity>> terminals = new HashSet<>();

        frontier.add(cablePos);

        // Visit all connected cables to update their network
        while (!frontier.isEmpty()) {
            BlockPos currentPos = frontier.remove(0);
            if (visited.contains(currentPos))
                continue;
            visited.add(currentPos);

            BlockState currentState = currentPos.equals(cablePos) ? cableState : world.getBlockState(currentPos);
            CableBlockEntity cable = getCable(world, currentPos);
            if (cable == null)
                continue;

            for (Direction direction : getCableConnections(currentState, cable)) {
                BlockPos target = currentPos.relative(direction);
                if (!world.isAreaLoaded(target, 0))
                    continue;

                BlockEntity tileEntity = world.getBlockEntity(target);
                BlockState targetState = world.getBlockState(target);
                if (tileEntity != null && !(tileEntity instanceof CableBlockEntity) && tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).isPresent()) { // if cap is present
                    holders.add(Pair.of(tileEntity, direction.getOpposite()));
                    terminals.add(Pair.of(cable, tileEntity));
                    continue;
                }
                if (visited.contains(target))
                    continue;
                CableBlockEntity targetCable = getCable(world, target);
                if (targetCable == null)
                    continue;
                if (targetCable.canFluxToward(targetState, direction.getOpposite()))
                    frontier.add(target);
            }
        }

        terminals.forEach(pair -> {
            CableBlockEntity cable = pair.getFirst();
            BlockEntity holder = pair.getSecond();
            List<IEnergyStorage> energyStorages = new ArrayList<>();
            holders.forEach(holderPair -> {
                if (holderPair.getFirst() == holder)
                    return;
                IEnergyStorage ies = holderPair.getFirst().getCapability(CapabilityEnergy.ENERGY, holderPair.getSecond()).orElse(null);
                if (ies != null) // if cap cast returns null, skip
                    energyStorages.add(ies);
            });
            cable.compositeEnergyStorage = new CompositeEnergyStorage(energyStorages.toArray(new IEnergyStorage[0]));
            cable.energyCapability = LazyOptional.of(() -> cable.compositeEnergyStorage);
        });

    }

    public static void resetAffectedCableNetworks(Level world, BlockPos start) {
        List<BlockPos> frontier = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        frontier.add(start);

        while (!frontier.isEmpty()) {
            BlockPos pos = frontier.remove(0);
            if (visited.contains(pos))
                continue;
            visited.add(pos);
            CableBlockEntity cable = getCable(world, pos);
            if (cable == null)
                continue;
            cable.resetNetwork();

            for (Direction d : Iterate.directions) {
                BlockPos target = pos.relative(d);
                if (visited.contains(target))
                    continue;
                if (cable.canFluxToward(world.getBlockState(target),d))
                    frontier.add(target);
            }
        }
    }

    public static Direction validateNeighbourChange(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos neighborPos, boolean isMoving) {
        if (world.isClientSide)
            return null;
        if (world.getBlockState(neighborPos).getBlock() instanceof AxisPipeBlock)
            return null;
        if (getStraightCableAxis(state) == null && !TMBlocks.ENCASED_CABLE_BLOCK.has(state))
            return null;
        for (Direction d : Iterate.directions) {
            if (!pos.relative(d)
                    .equals(neighborPos))
                continue;
            return d;
        }
        return null;
    }

    public static CableBlockEntity getCable(BlockGetter reader, BlockPos pos) {
        BlockEntity te = reader.getBlockEntity(pos);
        return te instanceof CableBlockEntity ? (CableBlockEntity) te : null;
    }

    public static boolean isOpenEnd(BlockGetter reader, BlockPos pos, Direction side) {
        BlockPos connectedPos = pos.relative(side);
        BlockState connectedState = reader.getBlockState(connectedPos);
        CableBlockEntity cable = CablePropagator.getCable(reader, connectedPos);
        if (cable != null && cable.canFluxToward(connectedState, side.getOpposite()))
            return false;
        if (hasEnergyCapability(reader, connectedPos, side.getOpposite()))
            return false;
        return true;
    }

    public static List<Direction> getCableConnections(BlockState state, CableBlockEntity cable) {
        List<Direction> list = new ArrayList<>();
        for (Direction d : Iterate.directions)
            if (cable.canFluxToward(state, d))
                list.add(d);
        return list;
    }

    public static boolean hasEnergyCapability(BlockGetter world, BlockPos pos, Direction side) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        return tileEntity != null && tileEntity.getCapability(CapabilityEnergy.ENERGY, side)
                .isPresent();
    }

    @Nullable
    public static Direction.Axis getStraightCableAxis(BlockState state) {
        if (state.getBlock() instanceof AxisPipeBlock)
            return state.getValue(AxisPipeBlock.AXIS);
        if (!CableBlock.isCable(state))
            return null;
        Direction.Axis axisFound = null;
        int connections = 0;
        for (Direction.Axis axis : Iterate.axes) {
            Direction d1 = Direction.get(Direction.AxisDirection.NEGATIVE, axis);
            Direction d2 = Direction.get(Direction.AxisDirection.POSITIVE, axis);
            boolean openAt1 = CableBlock.isOpenAt(state, d1);
            boolean openAt2 = CableBlock.isOpenAt(state, d2);
            if (openAt1)
                connections++;
            if (openAt2)
                connections++;
            if (openAt1 && openAt2)
                if (axisFound != null)
                    return null;
                else
                    axisFound = axis;
        }
        return connections == 2 ? axisFound : null;
    }

}
