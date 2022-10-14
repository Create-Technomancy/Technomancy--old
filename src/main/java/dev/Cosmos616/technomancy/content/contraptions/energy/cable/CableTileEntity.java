package dev.Cosmos616.technomancy.content.contraptions.energy.cable;

import com.simibubi.create.content.contraptions.components.structureMovement.ITransformableTE;
import com.simibubi.create.content.contraptions.components.structureMovement.StructureTransform;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedTileEntityBehaviour;
import com.simibubi.create.foundation.advancement.AllTriggers;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import dev.Cosmos616.technomancy.foundation.quantum.CompositeEnergyStorage;
import dev.Cosmos616.technomancy.registry.TMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CableTileEntity extends SmartTileEntity implements ITransformableTE {

    protected CompositeEnergyStorage compositeEnergyStorage;
    protected LazyOptional<IEnergyStorage> energyCapability;

    public CableTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyCapability = LazyOptional.empty();
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        behaviours.add(new BracketedTileEntityBehaviour(this, this::canHaveBracket));
//                .withTrigger(state -> AllTriggers.BRACKET_APPLY_TRIGGER.constructTriggerFor(state.getBlock())));
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == CapabilityEnergy.ENERGY) {
            if (compositeEnergyStorage == null)
                CablePropagator.propagateChangedCable(getLevel(), getBlockPos(), getBlockState());
            return energyCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    public void resetNetwork() {
        compositeEnergyStorage = null;
        energyCapability.invalidate();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyCapability.invalidate();
    }

    @Override
    public void transform(StructureTransform transform) {
        BracketedTileEntityBehaviour bracketBehaviour = getBehaviour(BracketedTileEntityBehaviour.TYPE);
        if (bracketBehaviour != null) {
            bracketBehaviour.transformBracket(transform);
        }
    }

    private boolean canHaveBracket(BlockState state) {
        return !(state.getBlock() instanceof EncasedCableBlock);
    }

    /**
     * Checks if the given block has connection on the given side based on its {@link net.minecraft.world.level.block.PipeBlock#PROPERTY_BY_DIRECTION direction property}.
     * @param state The block state to check.
     * @param direction The side to check.
     * @return True if the block has connection on the given side.
     */
    public boolean canFluxToward(BlockState state, Direction direction) {
        return (CableBlock.isCable(state) || state.getBlock() instanceof EncasedCableBlock)
                && state.getValue(CableBlock.PROPERTY_BY_DIRECTION.get(direction));
    }

    public AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {

        // TODO: Clean up this code

        AttachmentTypes attachment = AttachmentTypes.RIM;

        if (!canFluxToward(state, direction)) // if the cable has no connection on this side
            attachment = AttachmentTypes.NONE;

        BlockPos offsetPos = pos.relative(direction);
        BlockState facingState = world.getBlockState(offsetPos);

        if (TMBlocks.ENCASED_CABLE_BLOCK.has(facingState) // if next to an encased cable
                && facingState.getValue(EncasedCableBlock.FACING_TO_PROPERTY_MAP.get(direction.getOpposite())))
            attachment = CableBlock.shouldDrawCasing(world, pos, state) ? AttachmentTypes.GOLD : AttachmentTypes.NORMAL;

        if (CablePropagator.hasEnergyCapability(world, offsetPos, direction.getOpposite())) // if next to a cap provider
            attachment = CableBlock.shouldDrawCasing(world, pos, state) ? AttachmentTypes.GOLD : AttachmentTypes.NORMAL;

        if (attachment == AttachmentTypes.RIM && TMBlocks.ENCASED_CABLE_BLOCK.has(state))
            return AttachmentTypes.RIM;

        if (!CableBlock.isCable(world.getBlockState(offsetPos))) {
            BlockEntity te = world.getBlockEntity(offsetPos);
            if (te instanceof CableTileEntity cable && cable.canFluxToward(world.getBlockState(offsetPos), direction.getOpposite()))
                return CableBlock.shouldDrawCasing(world, pos, state) ? AttachmentTypes.GOLD : AttachmentTypes.NORMAL;
        }

        if (attachment == AttachmentTypes.RIM && !CableBlock.shouldDrawRim(world, pos, state, direction))
        return CableBlock.shouldDrawCasing(world, pos, state) ? AttachmentTypes.GOLD : AttachmentTypes.NORMAL;

        return attachment;
    }

    public enum AttachmentTypes {
        NONE, NORMAL, GOLD, RIM;

        public boolean hasModel() {
            return this != NONE;
        }
    }

}
