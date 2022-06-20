package dev.Cosmos616.technomancy.content.contraptions.energy.cable;

import com.simibubi.create.content.contraptions.relays.elementary.BracketedTileEntityBehaviour;
import com.simibubi.create.foundation.block.connected.BakedModelWrapperWithData;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import dev.Cosmos616.technomancy.registry.TMBlockPartials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    private static final ModelProperty<CableAttachmentModel.CableModelData> CABLE_PROPERTY = new ModelProperty<>();

    public CableAttachmentModel(BakedModel template) {
        super(template);
    }

    @Override
    protected ModelDataMap.Builder gatherModelData(ModelDataMap.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state) {
        CableModelData data = new CableModelData();
        BlockEntity te = world.getBlockEntity(pos);
        BracketedTileEntityBehaviour bracket = TileEntityBehaviour.get(world, pos, BracketedTileEntityBehaviour.TYPE);

        if (te != null)
            for (Direction d : Iterate.directions)
                data.putRim(d, ((CableTileEntity)te).getRenderedRimAttachment(world, pos, state, d));

        if (bracket != null) // Sadly, this won't work without update form Create dev team :(
            data.putBracket(bracket.getBracket());

        data.setEncased(CableBlock.shouldDrawCasing(world, pos, state));
        return builder.withInitial(CABLE_PROPERTY, data);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, @NotNull Random rand, @NotNull IModelData data) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, data);
        if (data.hasProperty(CABLE_PROPERTY)) {
            quads = new ArrayList<>(quads);
            addQuads(quads, state, side, rand, data, data.getData(CABLE_PROPERTY));
        }
        return quads;
    }

    private void addQuads(List<BakedQuad> quads, BlockState state, Direction side, Random rand, IModelData data, CableAttachmentModel.CableModelData pipeData) {
        for (Direction d : Iterate.directions)
            if (pipeData.hasRim(d))
                quads.addAll(TMBlockPartials.CABLE_ATTACHMENTS.get(pipeData.getRim(d))
                        .get(d)
                        .get()
                        .getQuads(state, side, rand, data));
        if (pipeData.isEncased())
            quads.addAll(TMBlockPartials.CABLE_CASING.get()
                    .getQuads(state, side, rand, data));
        BakedModel bracket = pipeData.getBracket();
        if (bracket != null)
            quads.addAll(bracket.getQuads(state, side, rand, data));
    }

    private static class CableModelData {
        CableTileEntity.AttachmentTypes[] rims;
        boolean encased;
        BakedModel bracket;

        public CableModelData() {
            rims = new CableTileEntity.AttachmentTypes[6];
            Arrays.fill(rims, CableTileEntity.AttachmentTypes.NONE);
        }

        public void putBracket(BlockState state) {
            this.bracket = Minecraft.getInstance()
                    .getBlockRenderer()
                    .getBlockModel(state);
        }

        public BakedModel getBracket() {
            return bracket;
        }

        public void putRim(Direction face, CableTileEntity.AttachmentTypes rim) {
            rims[face.get3DDataValue()] = rim;
        }

        public void setEncased(boolean encased) {
            this.encased = encased;
        }

        public boolean hasRim(Direction face) {
            return rims[face.get3DDataValue()] != CableTileEntity.AttachmentTypes.NONE;
        }

        public CableTileEntity.AttachmentTypes getRim(Direction face) {
            return rims[face.get3DDataValue()];
        }

        public boolean isEncased() {
            return encased;
        }
    }

}
