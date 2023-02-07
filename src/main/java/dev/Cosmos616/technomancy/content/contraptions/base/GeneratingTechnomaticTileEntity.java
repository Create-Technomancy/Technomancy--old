package dev.Cosmos616.technomancy.content.contraptions.base;

import com.simibubi.create.foundation.utility.Lang;
import dev.Cosmos616.technomancy.foundation.energy.IConduct;
import dev.Cosmos616.technomancy.foundation.energy.MagicNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class GeneratingTechnomaticTileEntity extends TechnomaticTileEntity {

    public boolean reActivateSource;

    public GeneratingTechnomaticTileEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    protected void notifyLoadCapacityChange(float capacity) {
        getOrCreateNetwork().updateCapacityFor(this, capacity);
    }

    @Override
    public void removeSource() {
        if (hasSource() && isSource())
            reActivateSource = true;
        super.removeSource();
    }

    @Override
    public void setSource(BlockPos source) {
        super.setSource(source);
        BlockEntity tileEntity = level.getBlockEntity(source);
        if (!(tileEntity instanceof TechnomaticTileEntity))
            return;
        TechnomaticTileEntity sourceTe = (TechnomaticTileEntity) tileEntity;
        if (reActivateSource && Math.abs(sourceTe.getEmission()) >= Math.abs(getGeneratedEmission()))
            reActivateSource = false;
    }

    @Override
    public void tick() {
        super.tick();
        if (reActivateSource) {
            updateGeneratedEmission();
            reActivateSource = false;
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (!IConduct.Load.isEnabled())
            return added;

        float loadBase = calculateAddedLoadCapacity();
        if (Mth.equal(loadBase, 0))
            return added;

        Lang.translate("gui.goggles.generator_stats")
                .forGoggles(tooltip);
        Lang.translate("tooltip.capacityProvided")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        float emisssion = getGeneratedEmission();
        if (emisssion != getGeneratedEmission() && emisssion != 0)
            loadBase *= getGeneratedEmission() / emisssion;
        emisssion = Math.abs(emisssion);

        float loadTotal = loadBase * emisssion;

        Lang.number(loadTotal)
                .translate("generic.unit.load")
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.translate("gui.goggles.at_current_emission")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

        return true;
    }

    public void updateGeneratedEmission() {
        float speed = getGeneratedEmission();
        float prevEmission = this.emission;

        if (level.isClientSide)
            return;

        if (prevEmission != speed) {
            if (!hasSource()) {
                IConduct.EmissionLevel levelBefore = IConduct.EmissionLevel.of(this.emission);
                IConduct.EmissionLevel levelafter = IConduct.EmissionLevel.of(emission);
//                if (levelBefore != levelafter)
//                    effects.queueRotationIndicators();
            }

            applyNewEmission(prevEmission, emission);
        }

        if (hasNetwork() && speed != 0) {
            MagicNetwork network = getOrCreateNetwork();
            notifyLoadCapacityChange(calculateAddedLoadCapacity());
            getOrCreateNetwork().updateLoadFor(this, calculateLoadApplied());
            network.updateLoad();
        }

        onEmissionChanged(prevEmission);
        sendData();
    }

    public void applyNewEmission(float prevEmission, float emission) {

        // Emission changed to 0
        if (emission == 0) {
            if (hasSource()) {
                notifyLoadCapacityChange(0);
                getOrCreateNetwork().updateCapacityFor(this, calculateLoadApplied());
                return;
            }
            detachMagics();
            setEmission(0);
            setNetwork(null);
            return;
        }

        // Now emitting - create a new Network
        if (prevEmission == 0) {
            setEmission(emission);
            setNetwork(createNetworkId());
            attachMagics();
            return;
        }

        // Change emission when overpowered by other generator
        if (hasSource()) {

            // Staying below Overpowered emission
            if (Math.abs(prevEmission) >= Math.abs(emission)) {
                if (Math.signum(prevEmission) != Math.signum(emission))
                    level.destroyBlock(worldPosition, true);
                return;
            }

            // Faster than attached network -> become the new source
            detachMagics();
            setEmission(emission);
            source = null;
            setNetwork(createNetworkId());
            attachMagics();
            return;
        }

        // Reapply source
        detachMagics();
        setEmission(emission);
        attachMagics();
    }

    public Long createNetworkId() {
        return worldPosition.asLong();
    }
}
