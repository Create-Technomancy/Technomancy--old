package com.chazbomb.technomancy.content.contraptions.energy.cable;

import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.utility.Iterate;
import com.chazbomb.technomancy.foundation.energy.AetherTransportBehaviour;
import com.chazbomb.technomancy.registry.TMBlockPartials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CableAttachmentModel extends BakedModelWrapperWithData {

    private static final ModelProperty<CableModelData> CABLE_PROPERTY = new ModelProperty<>();

    public CableAttachmentModel(BakedModel template) {
        super(template);
    }

    @Override
    protected void gatherModelData(ModelDataMap.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, IModelData blockEntityData) {
        //nothing :)
    }

    /**
     * the method had some errors in it so i commented it
     * but it showed it is unused anyway
     * -DrMangoTea
     */

/*
    @Override
    protected ModelDataMap.Builder gatherModelData(ModelDataMap.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state) {
        CableModelData data = new CableModelData();
//        BlockEntity te = world.getBlockEntity(pos);
        AetherTransportBehaviour transport = BlockEntityBehaviour.get(world, pos, AetherTransportBehaviour.TYPE);
        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);

        if (transport != null)
            for (Direction d : Iterate.directions)
//                data.putAttachment(d, ((CableBlockEntity)te).getRenderedRimAttachment(world, pos, state, d));
                data.putAttachment(d, transport.getRenderedRimAttachment(world, pos, state, d));

        if (bracket != null) // Sadly, this won't work without update form Create dev team :(
            data.putBracket(bracket.getBracket());

        data.setEncased(CableBlock.shouldDrawCasing(world, pos, state));
        return builder.withInitial(CABLE_PROPERTY, data);
    }
 */

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, @NotNull Random rand, @NotNull IModelData data) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, data);
        if (data.hasProperty(CABLE_PROPERTY)) {
            CableAttachmentModel.CableModelData cableData = data.getData(CABLE_PROPERTY);
            quads = new ArrayList<>(quads);
            addQuads(quads, state, side, rand, data, cableData);
        }
        return quads;
    }

    private void addQuads(List<BakedQuad> quads, BlockState state, Direction side, Random rand, IModelData data, CableAttachmentModel.CableModelData cableData) {
        BakedModel bracket = cableData.getBracket();
        if (bracket != null)
            quads.addAll(bracket.getQuads(state, side, rand, data));
        for (Direction d : Iterate.directions) {
            AetherTransportBehaviour.AttachmentTypes type = cableData.getAttachment(d);
            for (AetherTransportBehaviour.AttachmentTypes.ComponentPartials partial : type.partials) {
                quads.addAll(TMBlockPartials.CABLE_ATTACHMENTS.get(partial)
                        .get(d)
                        .get()
                        .getQuads(state, side, rand, data));
            }
        }
        if (cableData.isEncased())
            quads.addAll(TMBlockPartials.CABLE_CASING.get()
                    .getQuads(state, side, rand, data));
    }

    private static class CableModelData {
        private AetherTransportBehaviour.AttachmentTypes[] attachments;
        private boolean encased;
        private BakedModel bracket;

        public CableModelData() {
            attachments = new AetherTransportBehaviour.AttachmentTypes[6];
            Arrays.fill(attachments, AetherTransportBehaviour.AttachmentTypes.NONE);
        }

        public void putBracket(BlockState state) {
            if (state != null) {
                this.bracket = Minecraft.getInstance()
                        .getBlockRenderer()
                        .getBlockModel(state);
            }
        }

        public BakedModel getBracket() {
            return bracket;
        }

        public void putAttachment(Direction face, AetherTransportBehaviour.AttachmentTypes rim) {
            attachments[face.get3DDataValue()] = rim;
        }

        public AetherTransportBehaviour.AttachmentTypes getAttachment(Direction face) {
            return attachments[face.get3DDataValue()];
        }

        public void setEncased(boolean encased) {
            this.encased = encased;
        }

        public boolean isEncased() {
            return encased;
        }
    }

}
