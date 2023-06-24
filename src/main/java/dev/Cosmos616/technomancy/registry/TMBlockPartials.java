package dev.Cosmos616.technomancy.registry;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.contraptions.aether.battery.BatteryBlock;
import dev.Cosmos616.technomancy.foundation.LEGACYenergy.AetherTransportBehaviour;
import net.minecraft.core.Direction;

import java.util.*;

public class TMBlockPartials {

    public static final PartialModel
        CABLE_CASING = block("cable/casing"),
        LASER_BEAM_INNER = block("laser/beam_inner"),
        LASER_BEAM_OUTER = block("laser/beam_outer"),
        SPARK_GAP_COG = block("spark_gap/spark_gap_cog"),
        SOUL_BURNER_PARTIAL = block("soul_burner/soul_burner_partial");

    public static Map<BatteryBlock.Shape, PartialModel> BATTERY_TOP = new HashMap<>();
    public static Map<BatteryBlock.Shape, PartialModel> BATTERY_BOTTOM = new HashMap<>();
    
    public static Map<BatteryBlock.Shape, PartialModel> CREATIVE_BATTERY_TOP = new HashMap<>();
    public static Map<BatteryBlock.Shape, PartialModel> CREATIVE_BATTERY_BOTTOM = new HashMap<>();
    
    public static List<Map<BatteryBlock.Shape, PartialModel>> BATTERY_BLOCK_CHARGE_SHAPES = new ArrayList<>();
    public static Map<BatteryBlock.Shape, PartialModel> CREATIVE_BATTERY_BLOCK_SHAPES = new HashMap<>();

    static {
        for (BatteryBlock.Shape shape : BatteryBlock.Shape.values()) {
            BATTERY_TOP.put(shape, block("battery/end/top_" +
                shape.getSerializedName().toLowerCase()));
            BATTERY_BOTTOM.put(shape, block("battery/end/bottom_" +
                shape.getSerializedName().toLowerCase()));
            
            CREATIVE_BATTERY_TOP.put(shape, block("battery/end/top_" +
                shape.getSerializedName().toLowerCase() + "_creative"));
            CREATIVE_BATTERY_BOTTOM.put(shape, block("battery/end/bottom_" +
                shape.getSerializedName().toLowerCase() + "_creative"));
    
            CREATIVE_BATTERY_BLOCK_SHAPES.put(shape, block("battery/" +
                shape.getSerializedName().toLowerCase() + "_creative"));
        }
        
        for (int i = 0; i < 9; i++) {
            Map<BatteryBlock.Shape, PartialModel> entry = new HashMap<>();
    
            for (BatteryBlock.Shape shape : BatteryBlock.Shape.values()) {
                entry.put(shape, block("battery/" +
                    shape.getSerializedName().toLowerCase() + "_" + i));
            }
            
            BATTERY_BLOCK_CHARGE_SHAPES.add(entry);
        }
    }
    
    public static final Map<AetherTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> CABLE_ATTACHMENTS =
            new EnumMap<>(AetherTransportBehaviour.AttachmentTypes.ComponentPartials.class);

    static {
        for (AetherTransportBehaviour.AttachmentTypes.ComponentPartials type : AetherTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
//            if (!type.hasModel())
//                continue;
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("cable/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            CABLE_ATTACHMENTS.put(type, map);
        }
    }

    private static PartialModel block(String path) {
        return new PartialModel(Technomancy.asResource("block/" + path));
    }

    public static void init() {
        // init static fields
    }

}
