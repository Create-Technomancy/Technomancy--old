package dev.Cosmos616.technomancy.content.contraptions.aether.cable;

import com.simibubi.create.content.contraptions.components.structureMovement.ITransformableTE;
import com.simibubi.create.content.contraptions.components.structureMovement.StructureTransform;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedTileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import dev.Cosmos616.technomancy.foundation.aether.AetherNetworkElement;
import dev.Cosmos616.technomancy.foundation.energy.AetherTransportBehaviour;
import dev.Cosmos616.technomancy.registry.TMBlocks;
import dev.Cosmos616.technomancy.registry.TMCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CableBlockEntity extends AetherNetworkElement implements ITransformableTE {

    public CableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
//        behaviours.add(new BracketedTileEntityBehaviour(this, this::canHaveBracket));
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

    class StandardAetherTransportBehaviour extends AetherTransportBehaviour {

        public StandardAetherTransportBehaviour(SmartTileEntity te) {
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
                AetherTransportBehaviour cableBehaviour = TileEntityBehaviour.get(world, offsetPos, AetherTransportBehaviour.TYPE);
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
