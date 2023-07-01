package com.chazbomb.technomancy.foundation.energy;


import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public abstract class AetherTransportBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<AetherTransportBehaviour> TYPE = new BehaviourType<>();

    protected CompositeAetherStorage aether;
    protected LazyOptional<IAetherStorage> capability;

    public AetherTransportBehaviour(SmartBlockEntity te) {
        super(te);
        this.capability = LazyOptional.empty();
    }

    public void setStorage(IAetherStorage... pools) {
        this.resetNetwork();
        aether = new CompositeAetherStorage(pools);
        capability = LazyOptional.of(() -> aether);
    }

//    @Override
//    public void tick() {
//        super.tick();
//    }

    /**
     *  idk doesnt work pls fix
     *  -DrMangoTea
     */
    /*
    @Override
    public void remove() {
        super.remove();
        capability.invalidate();
    }

     */

    public void resetNetwork() {
        aether = null;
        capability.invalidate();
    }

    public CompositeAetherStorage getHandler() {
        return aether;
    }

    public LazyOptional<IAetherStorage> getCapability() {
        return capability;
    }

    public abstract boolean canFluxToward(BlockState state, Direction direction);

    public AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        if (!canFluxToward(state, direction))
            return AttachmentTypes.NONE;
        return AttachmentTypes.RIM;
    }

    public enum AttachmentTypes {
        NONE,
        NORMAL(ComponentPartials.NORMAL),
        GOLDEN(ComponentPartials.GOLDEN),
        RIM(ComponentPartials.RIM_CONNECTOR, ComponentPartials.RIM),
        PARTIAL_RIM(ComponentPartials.RIM);

        public final AttachmentTypes.ComponentPartials[] partials;

        AttachmentTypes(AttachmentTypes.ComponentPartials... partials) {
            this.partials = partials;
        }

        public AttachmentTypes withoutConnector() {
            if (this == AttachmentTypes.RIM)
                return AttachmentTypes.PARTIAL_RIM;
            return this;
        }

        public boolean hasModel() {
            return this != NONE;
        }

        public enum ComponentPartials {
            NORMAL, GOLDEN, RIM_CONNECTOR, RIM;
        }
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

}
