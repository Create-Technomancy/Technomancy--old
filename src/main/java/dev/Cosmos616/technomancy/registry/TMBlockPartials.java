package dev.Cosmos616.technomancy.registry;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.contraptions.energy.cable.CableTileEntity;
import net.minecraft.core.Direction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TMBlockPartials {

    public static final PartialModel
            CABLE_CASING = block("cable/casing"),
            LASER_BEAM_INNER = block("laser/beam_inner"),
            LASER_BEAM_OUTER = block("laser/beam_outer"),

    SPARK_GAP_COG = block("spark_gap/spark_gap_cog");




    public static final Map<CableTileEntity.AttachmentTypes, Map<Direction, PartialModel>> CABLE_ATTACHMENTS =
            new EnumMap<>(CableTileEntity.AttachmentTypes.class);

    static {
        for (CableTileEntity.AttachmentTypes type : CableTileEntity.AttachmentTypes.values()) {
            if (!type.hasModel())
                continue;
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("cable/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            CABLE_ATTACHMENTS.put(type, map);
        }
    }

    private static PartialModel block(String path) {
        return new PartialModel(Technomancy.TMLoc("block/" + path));
    }

    public static void init() {
        // init static fields
    }

}
