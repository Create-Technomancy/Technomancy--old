package com.chazbomb.technomancy.content.contraptions.energy.spectre_coil;
/*
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;

public class SpectreCoilConnectivityHandler {

    public static void formCoils(SpectreCoilTileEntity te) {
        SpectreCoilConnectivityHandler.CoilSearchCache cache = new SpectreCoilConnectivityHandler.CoilSearchCache();
        List<SpectreCoilTileEntity> frontier = new ArrayList<>();
        frontier.add(te);
        formCoils(te.getType(), te.getLevel(), cache, frontier);
    }

    private static void formCoils(BlockEntityType<?> type, BlockGetter world, SpectreCoilConnectivityHandler.CoilSearchCache cache,
                                  List<SpectreCoilTileEntity> frontier) {
        PriorityQueue<Pair<Integer, SpectreCoilTileEntity>> creationQueue = makeCreationQueue();
        Set<BlockPos> visited = new HashSet<>();

        while (!frontier.isEmpty()) {
            SpectreCoilTileEntity coil = frontier.remove(0);
            BlockPos coilPos = coil.getBlockPos();
            if (visited.contains(coilPos))
                continue;

            visited.add(coilPos);

            int amount = tryToFormNewCoil(coil, cache, true);
            if (amount > 1)
                creationQueue.add(Pair.of(amount, coil));

            BlockPos next = coilPos.relative(Direction.DOWN);

            if (!visited.contains(next)) {
                SpectreCoilTileEntity nextCoil = coilAt(type, world, next);
                if (nextCoil != null && !nextCoil.isRemoved())
                    frontier.add(nextCoil);
            }
        }

        visited.clear();

        while (!creationQueue.isEmpty()) {
            Pair<Integer, SpectreCoilTileEntity> next = creationQueue.poll();
            SpectreCoilTileEntity toCreate = next.getValue();
            if (visited.contains(toCreate.getBlockPos()))
                continue;
            visited.add(toCreate.getBlockPos());
            tryToFormNewCoil(toCreate, cache, false);
        }

    }

    public static void splitCoil(SpectreCoilTileEntity te) {
        splitCoilAndInvalidate(te, null, false);
    }

    private static int tryToFormNewCoil(SpectreCoilTileEntity te, SpectreCoilConnectivityHandler.CoilSearchCache cache, boolean simulate) {
        if (!te.isController())
            return 0;
        int amount = tryToFormNewCoilStack(te, cache, true);

        if (!simulate) {
            if (te.height == amount)
                return amount;
            splitCoilAndInvalidate(te, cache, false);
            tryToFormNewCoilStack(te, cache, false);
            te.updateConnectivity = false;
            te.height = amount;

            BlockState state = te.getBlockState();
            if (SpectreCoilBlock.isCoil(state)) {
                state = state.setValue(SpectreCoilBlock.BOTTOM, true);
                state = state.setValue(SpectreCoilBlock.TOP, te.height == 1);
                te.updateShape();
                te.getLevel().setBlock(te.getBlockPos(), state, 22);
            }
            te.updateShape();
            te.setChanged();
        }

        return amount;
    }

    private static int tryToFormNewCoilStack(SpectreCoilTileEntity te, SpectreCoilConnectivityHandler.CoilSearchCache cache,
                                               boolean simulate) {
        int amount = 0;
        int height = 0;
        BlockEntityType<?> type = te.getType();
        Level world = te.getLevel();
        BlockPos origin = te.getBlockPos();

        for (int yOffset = 0; yOffset < 32; yOffset++) {

            BlockPos pos = origin.offset(0, yOffset, 0);
            Optional<SpectreCoilTileEntity> coil = cache.getOrCache(type, world, pos);
            if (coil.isEmpty())
                break;
            amount++;
            height++;
        }

        if (simulate)
            return amount;

        for (int yOffset = 0; yOffset < height; yOffset++) {
            BlockPos pos = origin.offset(0, yOffset, 0);
            SpectreCoilTileEntity coil = coilAt(type, world, pos);
            if (coil == te)
                continue;

            splitCoilAndInvalidate(coil, cache, false);
            coil.setController(origin);
            coil.updateConnectivity = false;
            cache.put(pos, te);

            BlockState state = world.getBlockState(pos);
            if (!SpectreCoilBlock.isCoil(state))
                continue;
            state = state.setValue(SpectreCoilBlock.BOTTOM, yOffset == 0);
            state = state.setValue(SpectreCoilBlock.TOP, yOffset == height - 1);
            world.setBlock(pos, state, 22);
        }
        te.updateShape();
        return amount;
    }

    private static void splitCoilAndInvalidate(SpectreCoilTileEntity te, @Nullable SpectreCoilConnectivityHandler.CoilSearchCache cache,
                                               boolean tryReconnect) {
        // tryReconnect helps whenever only few tanks have been removed
        te = te.getControllerTE();
        if (te == null)
            return;

        int height = te.height;
        Level world = te.getLevel();
        BlockPos origin = te.getBlockPos();
        List<SpectreCoilTileEntity> frontier = new ArrayList<>();

        for (int yOffset = 0; yOffset < height; yOffset++) {

            BlockPos pos = origin.offset(0, yOffset, 0);
            SpectreCoilTileEntity coilAt = coilAt(te.getType(), world, pos);
            if (coilAt == null)
                continue;
            if (!coilAt.getController().equals(origin))
                continue;
            coilAt.removeController();
            coilAt.updateShape();

            if (tryReconnect) {
                frontier.add(coilAt);
                coilAt.updateConnectivity = false;
            }
            if (cache != null)
                cache.put(pos, coilAt);
        }

        if (tryReconnect)
            formCoils(te.getType(), world, cache == null ? new SpectreCoilConnectivityHandler.CoilSearchCache() : cache, frontier);
    }

    private static PriorityQueue<Pair<Integer, SpectreCoilTileEntity>> makeCreationQueue() {
        return new PriorityQueue<>(new Comparator<Pair<Integer, SpectreCoilTileEntity>>() {
            @Override
            public int compare(Pair<Integer, SpectreCoilTileEntity> o1, Pair<Integer, SpectreCoilTileEntity> o2) {
                return o2.getKey() - o1.getKey();
            }
        });
    }

    @Nullable
    public static SpectreCoilTileEntity coilAt(BlockEntityType<?> type, BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof SpectreCoilTileEntity && te.getType() == type)
            return (SpectreCoilTileEntity) te;
        return null;
    }

    private static class CoilSearchCache {
        Map<BlockPos, Optional<SpectreCoilTileEntity>> controllerMap;

        public CoilSearchCache() {
            controllerMap = new HashMap<>();
        }

        void put(BlockPos pos, SpectreCoilTileEntity target) {
            controllerMap.put(pos, Optional.of(target));
        }

        void putEmpty(BlockPos pos) {
            controllerMap.put(pos, Optional.empty());
        }

        boolean hasVisited(BlockPos pos) {
            return controllerMap.containsKey(pos);
        }

        Optional<SpectreCoilTileEntity> getOrCache(BlockEntityType<?> type, BlockGetter world, BlockPos pos) {
            if (hasVisited(pos))
                return controllerMap.get(pos);
            SpectreCoilTileEntity coilAt = coilAt(type, world, pos);
            if (coilAt == null) {
                putEmpty(pos);
                return Optional.empty();
            }
            SpectreCoilTileEntity controller = coilAt.getControllerTE();
            if (controller == null) {
                putEmpty(pos);
                return Optional.empty();
            }
            put(pos, controller);
            return Optional.of(controller);
        }

    }

    public static boolean isConnected(BlockGetter world, BlockPos tankPos, BlockPos otherTankPos) {
        BlockEntity te1 = world.getBlockEntity(tankPos);
        BlockEntity te2 = world.getBlockEntity(otherTankPos);
        if (!(te1 instanceof SpectreCoilTileEntity) || !(te2 instanceof SpectreCoilTileEntity))
            return false;
        return ((SpectreCoilTileEntity) te1).getController()
                .equals(((SpectreCoilTileEntity) te2).getController());
    }

}
*/