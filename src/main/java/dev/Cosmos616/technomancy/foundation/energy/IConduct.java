package dev.Cosmos616.technomancy.foundation.energy;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface IConduct extends IWrenchable {

    enum EmissionLevel {
        NONE(ChatFormatting.DARK_GRAY, 0x000000, 0),
        LOW(ChatFormatting.GREEN, 0x22FF22, 10),
        MEDIUM(ChatFormatting.AQUA, 0x0084FF, 20),
        HIGH(ChatFormatting.LIGHT_PURPLE, 0xFF55FF, 30);

        private final ChatFormatting textColor;
        private final int color;
        private final int particleSpeed;

        EmissionLevel(ChatFormatting textColor, int color, int particleSpeed) {
            this.textColor = textColor;
            this.color = color;
            this.particleSpeed = particleSpeed;
        }

        public ChatFormatting getTextColor() {
            return textColor;
        }

        public int getColor() {
            return color;
        }

        public int getParticleSpeed() {
            return particleSpeed;
        }

        public float getEmissionValue() {
            switch (this) {
                case HIGH:
                    return AllConfigs.SERVER.kinetics.fastSpeed.get()
                            .floatValue();
                case MEDIUM:
                    return AllConfigs.SERVER.kinetics.mediumSpeed.get()
                            .floatValue();
                case LOW:
                    return 1;
                case NONE:
                default:
                    return 0;
            }
        }

        public static EmissionLevel of(float emission) {
            emission = Math.abs(emission);

            if (emission >= AllConfigs.SERVER.kinetics.fastSpeed.get())
                return HIGH;
            if (emission >= AllConfigs.SERVER.kinetics.mediumSpeed.get())
                return MEDIUM;
            if (emission >= 1)
                return LOW;
            return NONE;
        }

        public static LangBuilder getFormattedEmissionText(float emission, boolean overloaded) {
            EmissionLevel emissionLevel = of(emission);
            LangBuilder builder = Lang.text(ItemDescription.makeProgressBar(3, emissionLevel.ordinal()));

            builder.translate("tooltip.emissionRequirement." + Lang.asId(emissionLevel.name()))
                    .space()
                    .text("(")
                    .add(Lang.number(Math.abs(emission)))
                    .space()
                    .translate("generic.unit.bhz")
                    .text(")")
                    .space();

            if (overloaded)
                builder.style(ChatFormatting.DARK_GRAY)
                        .style(ChatFormatting.STRIKETHROUGH);
            else
                builder.style(emissionLevel.getTextColor());

            return builder;
        }

    }

    enum QuantaLevel {
        NONE(ChatFormatting.DARK_GRAY, 0x000000),
        LOW(ChatFormatting.GREEN, 0x22FF22),
        MEDIUM(ChatFormatting.AQUA, 0x0084FF),
        HIGH(ChatFormatting.LIGHT_PURPLE, 0xFF55FF);

        private final ChatFormatting textColor;
        private final int color;

        QuantaLevel(ChatFormatting textColor, int color) {
            this.textColor = textColor;
            this.color = color;
        }

        public ChatFormatting getTextColor() {
            return textColor;
        }

        public int getColor() {
            return color;
        }

        public float getQuantaValue() {
            switch (this) {
                case HIGH:
                    return AllConfigs.SERVER.kinetics.fastSpeed.get()
                            .floatValue();
                case MEDIUM:
                    return AllConfigs.SERVER.kinetics.mediumSpeed.get()
                            .floatValue();
                case LOW:
                    return 1;
                case NONE:
                default:
                    return 0;
            }
        }

        public static QuantaLevel of(float quanta) {
            quanta = Math.abs(quanta);

            if (quanta >= AllConfigs.SERVER.kinetics.fastSpeed.get())
                return HIGH;
            if (quanta >= AllConfigs.SERVER.kinetics.mediumSpeed.get())
                return MEDIUM;
            if (quanta >= 1)
                return LOW;
            return NONE;
        }

        public static LangBuilder getFormattedQuantaText(float quanta, boolean overloaded) {
            QuantaLevel quantaLevel = of(quanta);
            LangBuilder builder = Lang.text(ItemDescription.makeProgressBar(3, quantaLevel.ordinal()));

            builder.translate("tooltip.quantaRequirement." + Lang.asId(quantaLevel.name()))
                    .space()
                    .text("(")
                    .add(Lang.number(Math.abs(quanta)))
                    .space()
                    .translate("generic.unit.qpb")
                    .text(")")
                    .space();

            if (overloaded)
                builder.style(ChatFormatting.DARK_GRAY)
                        .style(ChatFormatting.STRIKETHROUGH);
            else
                builder.style(quantaLevel.getTextColor());

            return builder;
        }

    }

    enum Load {
        LOW(ChatFormatting.YELLOW, ChatFormatting.GREEN),
        MEDIUM(ChatFormatting.GOLD, ChatFormatting.YELLOW),
        HIGH(ChatFormatting.RED, ChatFormatting.GOLD),
        OVERSTRESSED(ChatFormatting.RED, ChatFormatting.RED);

        private final ChatFormatting absoluteColor;
        private final ChatFormatting relativeColor;

        Load(ChatFormatting absoluteColor, ChatFormatting relativeColor) {
            this.absoluteColor = absoluteColor;
            this.relativeColor = relativeColor;
        }

        public ChatFormatting getAbsoluteColor() {
            return absoluteColor;
        }

        public ChatFormatting getRelativeColor() {
            return relativeColor;
        }

        public static Load of(double loadPercent) {
            if (loadPercent > 1)
                return Load.OVERSTRESSED;
            if (loadPercent > .75d)
                return Load.HIGH;
            if (loadPercent > .5d)
                return Load.MEDIUM;
            return Load.LOW;
        }

        public static boolean isEnabled() {
            return !AllConfigs.SERVER.kinetics.disableStress.get();
        }

        public static LangBuilder getFormattedLoadText(double loadPercent) {
            Load loadLevel = of(loadPercent);
            return Lang.text(ItemDescription.makeProgressBar(3, Math.min(loadLevel.ordinal() + 1, 3)))
                    .translate("tooltip.load." + Lang.asId(loadLevel.name()))
                    .text(String.format(" (%s%%) ", (int) (loadPercent * 100)))
                    .style(loadLevel.getRelativeColor());
        }
    }

    boolean hasConnectionTowards(LevelReader world, BlockPos pos, BlockState state, Direction face);

    //TODO: What should identify connections?
    byte getConnections(BlockState state);

    default EmissionLevel getMinimumRequiredEmissionLevel() {
        return EmissionLevel.NONE;
    }

    default QuantaLevel getMinimumRequiredQuantaLevel(){
        return QuantaLevel.NONE;
    }

}
