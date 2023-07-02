package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.content.contraptions.energy.cable.CableBlock;
import com.chazbomb.technomancy.content.contraptions.energy.cable.EncasedCableBlock;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pointing;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class TMBlockStateGen {

    public static <P extends EncasedCableBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> encasedCable() {
        return (c, p) -> {
            ModelFile flat = AssetLookup.partialBaseModel(c, p, "flat");
            MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());
            for (Direction d : Iterate.directions) {
                int verticalAngle = d == Direction.UP ? 90 : d == Direction.DOWN ? -90 : 0;
                builder.part()
                        .modelFile(flat)
                        .rotationX(verticalAngle)
                        .rotationY((int) (d.toYRot() + (d.getAxis()
                                .isVertical() ? 90 : 0)) % 360)
                        .addModel()
                        .end();
            }
        };
    }

    public static <P extends CableBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> cable() {
        return (c, p) -> {
            String path = "block/" + c.getName();

            String LU = "lu";
            String RU = "ru";
            String LD = "ld";
            String RD = "rd";
            String LR = "lr";
            String UD = "ud";
            String NONE = "none";

            List<String> orientations = ImmutableList.of(LU, RU, LD, RD, LR, UD, NONE);
            // why not couples?
            Map<String, Pair<Integer, Integer>> uvs = ImmutableMap.<String, Pair<Integer, Integer>>builder() // uv coordinates as if texture was 16x16
                    .put(LU, Pair.of(3, 13))
                    .put(RU, Pair.of(0, 13))
                    .put(LD, Pair.of(3, 10))
                    .put(RD, Pair.of(0, 10))
                    .put(LR, Pair.of(9, 10))
                    .put(UD, Pair.of(6, 13))
                    .put(NONE, Pair.of(6, 10))
                    .build();

            Map<Direction.Axis, ResourceLocation> coreTemplates = new IdentityHashMap<>();
            Map<Pair<String, Direction.Axis>, ModelFile> coreModels = new HashMap<>();

            for (Direction.Axis axis : Iterate.axes)
                coreTemplates.put(axis, p.modLoc(path + "/core_" + axis.getSerializedName()));

            for (Direction.Axis axis : Iterate.axes) {
                ResourceLocation parent = coreTemplates.get(axis);
                for (String s : orientations) {
                    Pair<String, Direction.Axis> key = Pair.of(s, axis);
                    String modelName = path + "/" + s + "_" + axis.getSerializedName();
                    coreModels.put(key, p.models()
                            .withExistingParent(modelName, parent)
                            .element()
                            .from(5, 5, 5)
                            .to(11, 11, 11)
                            .face(Direction.get(Direction.AxisDirection.POSITIVE, axis))
                            .end()
                            .face(Direction.get(Direction.AxisDirection.NEGATIVE, axis))
                            .end()
                            .faces((d, builder) -> {
                                Pair<Integer, Integer> pair = uvs.get(s);
                                float u = pair.getKey();
                                float v = pair.getValue();
                                if (d == Direction.UP) {
                                    float v1 = (u <= 3) ? v == 10 ? 13 : 10 : v;
                                    builder.uvs(u, v1, u + 3, v1 + 3);
                                } else if (d.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                                    float u1 = (u <= 3) ? u == 0 ? 3 : 0 : u;
                                    builder.uvs(u1, v, u1 + 3, v + 3);
                                } else {
                                    builder.uvs(u, v, u + 3, v + 3);
                                }
                                builder.texture("#0");
                            })
                            .end());
                }
            }

            MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());
            for (Direction.Axis axis : Iterate.axes) {
                putPart(coreModels, builder, axis, LU, true, false, true, false);
                putPart(coreModels, builder, axis, RU, true, false, false, true);
                putPart(coreModels, builder, axis, LD, false, true, true, false);
                putPart(coreModels, builder, axis, RD, false, true, false, true);
                putPart(coreModels, builder, axis, UD, true, true, false, false);
                putPart(coreModels, builder, axis, UD, true, false, false, false);
                putPart(coreModels, builder, axis, UD, false, true, false, false);
                putPart(coreModels, builder, axis, LR, false, false, true, true);
                putPart(coreModels, builder, axis, LR, false, false, true, false);
                putPart(coreModels, builder, axis, LR, false, false, false, true);
                putPart(coreModels, builder, axis, NONE, false, false, false, false);
            }
        };
    }

    private static void putPart(Map<Pair<String, Direction.Axis>, ModelFile> coreModels, MultiPartBlockStateBuilder builder,
                                Direction.Axis axis, String s, boolean up, boolean down, boolean left, boolean right) {
        Direction positiveAxis = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        Map<Direction, BooleanProperty> propertyMap = CableBlock.PROPERTY_BY_DIRECTION;
        builder.part()
                .modelFile(coreModels.get(Pair.of(s, axis)))
                .addModel()
                .condition(propertyMap.get(Pointing.UP.getCombinedDirection(positiveAxis)), up)
                .condition(propertyMap.get(Pointing.LEFT.getCombinedDirection(positiveAxis)), left)
                .condition(propertyMap.get(Pointing.RIGHT.getCombinedDirection(positiveAxis)), right)
                .condition(propertyMap.get(Pointing.DOWN.getCombinedDirection(positiveAxis)), down)
                .end();
    }


}
