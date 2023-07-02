package com.chazbomb.technomancy.content.contraptions.energy.cable;

import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;

import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.chazbomb.technomancy.foundation.energy.AetherTransportBehaviour;
import com.chazbomb.technomancy.registry.TMBlocks;
import com.chazbomb.technomancy.registry.TMCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CableBlockEntity extends SmartBlockEntity implements ITransformableBlockEntity {

    protected StandardAetherTransportBehaviour aether;

    public CableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        aether = new StandardAetherTransportBehaviour(this);
        behaviours.add(aether);
//        behaviours.add(new BracketedTileEntityBehaviour(this, this::canHaveBracket));
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == TMCapabilities.AETHER) {
            if(aether.canFluxToward(this.getBlockState(), side.getOpposite()))
                return aether.getCapability().cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void transform(StructureTransform transform) {
        BracketedBlockEntityBehaviour bracketBehaviour = getBehaviour(BracketedBlockEntityBehaviour.TYPE);
        if (bracketBehaviour != null) {
            bracketBehaviour.transformBracket(transform);
        }
    }

    private boolean canHaveBracket(BlockState state) {
        return !(state.getBlock() instanceof EncasedCableBlock);
    }

    class StandardAetherTransportBehaviour extends AetherTransportBehaviour {

        public StandardAetherTransportBehaviour(SmartBlockEntity te) {
            super(te);
        }

        public boolean canFluxToward(BlockState state, Direction direction) {
            return (CableBlock.isCable(state) || state.getBlock() instanceof EncasedCableBlock)
                && state.getValue(CableBlock.PROPERTY_BY_DIRECTION.get(direction));
        }

        @Override
        public AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
            AttachmentTypes attachment = super.getRenderedRimAttachment(world, pos, state, direction);

            BlockPos offsetPos = pos.relative(direction);
            BlockState otherState = world.getBlockState(offsetPos);

            if (attachment == AttachmentTypes.RIM
                    && !CableBlock.isCable(otherState)
                    && !TMBlocks.ENCASED_CABLE_BLOCK.has(otherState)) {
                AetherTransportBehaviour cableBehaviour = BlockEntityBehaviour.get(world, offsetPos, AetherTransportBehaviour.TYPE);
                if (cableBehaviour != null)
                    if (cableBehaviour.canFluxToward(otherState, direction.getOpposite()))
                        return CableBlock.shouldDrawCasing(level, pos, state) ? AttachmentTypes.GOLDEN : AttachmentTypes.NORMAL;
            }

            if (attachment == AttachmentTypes.RIM && !CableBlock.shouldDrawRim(world, pos, state, direction))
                return CableBlock.shouldDrawCasing(level, pos, state) ? AttachmentTypes.GOLDEN : AttachmentTypes.NORMAL;
            if (attachment == AttachmentTypes.NONE && state.getValue(CableBlock.PROPERTY_BY_DIRECTION.get(direction)))
                return CableBlock.shouldDrawCasing(level, pos, state) ? AttachmentTypes.GOLDEN : AttachmentTypes.NORMAL;
            return attachment;
        }

    }

}
