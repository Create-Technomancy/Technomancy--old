package dev.Cosmos616.technomancy.foundation.energy;

import com.simibubi.create.foundation.utility.Iterate;
import dev.Cosmos616.technomancy.content.contraptions.base.TechnomaticTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;
import java.util.List;

public class EmissionPropagator {

    private static final int MAX_FLICKER_SCORE = 128;

    /**
     * Determines the change in emission rate between two attached magic entities.
     *
     * @param from ???
     * @param to ???
     * @return ???
     */
    private static float getEmissionRateModifier(TechnomaticTileEntity from, TechnomaticTileEntity to) {
        //TODO: Add math and rules of energy transmission.
        return 1;
    }

    /**
     * Insert the added position to the magic network.
     *
     * @param worldIn ???
     * @param pos ???
     */
    public static void handleAdded(Level worldIn, BlockPos pos, TechnomaticTileEntity addedTE) {
        if (worldIn.isClientSide)
            return;
        if (!worldIn.isLoaded(pos))
            return;
        propagateNewSource(addedTE);
    }

    /**
     * Search for sourceless networks attached to the given entity and update them.
     *
     * @param currentTE ???
     */
    private static void propagateNewSource(TechnomaticTileEntity currentTE) {
        // ???
    }

    /**
     * Remove the given entity from the network.
     *
     * @param worldIn ???
     * @param pos ???
     * @param removedTE ???
     */
    public static void handleRemoved(Level worldIn, BlockPos pos, TechnomaticTileEntity removedTE) {
        if (worldIn.isClientSide)
            return;
        if (removedTE == null)
            return;
        if (removedTE.getTheoreticalEmission() == 0)
            return;

        for (BlockPos neighbourPos : getPotentialNeighbourLocations(removedTE)) {
            BlockState neighbourState = worldIn.getBlockState(neighbourPos);
            if (!(neighbourState.getBlock() instanceof IConduct))
                continue;
            BlockEntity tileEntity = worldIn.getBlockEntity(neighbourPos);
            if (!(tileEntity instanceof final TechnomaticTileEntity neighbourTE))
                continue;

            if (!neighbourTE.hasSource() || !neighbourTE.source.equals(pos))
                continue;

            propagateMissingSource(neighbourTE);
        }

    }

    /**
     * Clear the entire subnetwork depending on the given entity and find a new source
     *
     * @param updateTE ???
     */
    private static void propagateMissingSource(TechnomaticTileEntity updateTE) {
        final Level world = updateTE.getLevel();

        List<TechnomaticTileEntity> potentialNewSources = new LinkedList<>();
        List<BlockPos> frontier = new LinkedList<>();
        frontier.add(updateTE.getBlockPos());
        BlockPos missingSource = updateTE.hasSource() ? updateTE.source : null;

        while (!frontier.isEmpty()) {
            final BlockPos pos = frontier.remove(0);
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (!(tileEntity instanceof final TechnomaticTileEntity currentTE))
                continue;

            currentTE.removeSource();
            currentTE.sendData();

            for (TechnomaticTileEntity neighbourTE : getConnectedNeighbours(currentTE)) {
                if (neighbourTE.getBlockPos()
                        .equals(missingSource))
                    continue;
                if (!neighbourTE.hasSource())
                    continue;

                if (!neighbourTE.source.equals(pos)) {
                    potentialNewSources.add(neighbourTE);
                    continue;
                }

                if (neighbourTE.isSource())
                    potentialNewSources.add(neighbourTE);

                frontier.add(neighbourTE.getBlockPos());
            }
        }

        for (TechnomaticTileEntity newSource : potentialNewSources) {
            if (newSource.hasSource() || newSource.isSource()) {
                propagateNewSource(newSource);
                return;
            }
        }
    }

    private static TechnomaticTileEntity findConnectedNeighbour(TechnomaticTileEntity currentTE, BlockPos neighbourPos) {
        BlockState neighbourState = currentTE.getLevel()
                .getBlockState(neighbourPos);
        if (!(neighbourState.getBlock() instanceof IConduct))
            return null;
        if (!neighbourState.hasBlockEntity())
            return null;
        BlockEntity neighbourTE = currentTE.getLevel()
                .getBlockEntity(neighbourPos);
        if (!(neighbourTE instanceof TechnomaticTileEntity neighbourTTE))
            return null;
        if (!(neighbourTTE.getBlockState()
                .getBlock() instanceof IConduct))
            return null;
        if (!isConnected(currentTE, neighbourTTE) && !isConnected(neighbourTTE, currentTE))
            return null;
        return neighbourTTE;
    }

    public static boolean isConnected(TechnomaticTileEntity from, TechnomaticTileEntity to) {
        final BlockState stateFrom = from.getBlockState();
        final BlockState stateTo = to.getBlockState();
        return true;
//        return isLargeCogToSpeedController(stateFrom, stateTo, to.getBlockPos().subtract(from.getBlockPos()))
//                || getRotationSpeedModifier(from, to) != 0
//                || from.isCustomConnection(to, stateFrom, stateTo);
    }

    private static List<TechnomaticTileEntity> getConnectedNeighbours(TechnomaticTileEntity te) {
        List<TechnomaticTileEntity> neighbours = new LinkedList<>();
        for (BlockPos neighbourPos : getPotentialNeighbourLocations(te)) {
            final TechnomaticTileEntity neighbourTE = findConnectedNeighbour(te, neighbourPos);
            if (neighbourTE == null)
                continue;

            neighbours.add(neighbourTE);
        }
        return neighbours;
    }

    private static List<BlockPos> getPotentialNeighbourLocations(TechnomaticTileEntity te) {
        List<BlockPos> neighbours = new LinkedList<>();

        if (!te.getLevel()
                .isAreaLoaded(te.getBlockPos(), 1))
            return neighbours;

        for (Direction facing : Iterate.directions)
            neighbours.add(te.getBlockPos()
                    .relative(facing));

        BlockState blockState = te.getBlockState();
        if (!(blockState.getBlock() instanceof IConduct))
            return neighbours;
        IConduct block = (IConduct) blockState.getBlock();
        return te.addPropagationLocations(block, blockState, neighbours);
    }

}
