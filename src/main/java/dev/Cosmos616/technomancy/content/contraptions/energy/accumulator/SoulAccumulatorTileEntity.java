package dev.Cosmos616.technomancy.content.contraptions.energy.accumulator;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import dev.Cosmos616.technomancy.registry.TMFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulAccumulatorTileEntity extends SmartTileEntity implements IHaveGoggleInformation {
	public SoulAccumulatorTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		setLazyTickRate(20);
	}
	
	private SmartFluidTankBehaviour tank;
	
	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviours) {
		tank = SmartFluidTankBehaviour.single(this, 1000);
		tank.forbidInsertion();
		behaviours.add(tank);
	}
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (isFluidHandlerCap(cap) && (side == null || side.getAxis().isHorizontal()))
			return tank.getCapability().cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public void lazyTick() {
		if (!checkValidBiome())
			return;
		SmartFluidTank tankHandler = tank.getPrimaryHandler();
		FluidStack soulFluid = new FluidStack(FluidHelper.convertToStill(TMFluids.LIQUID_SOULS.get()), getGenerationRate());
		tankHandler.fill(soulFluid, IFluidHandler.FluidAction.EXECUTE);
	}
	
	protected boolean isValidBiome;
	protected boolean checkValidBiome() {
		if (isValidBiome)
			return true;
		if (this.level != null)
			return isValidBiome = this.level.getBiome(this.getBlockPos()).is(Biomes.SOUL_SAND_VALLEY);
		return false;
	}
	
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return containedFluidTooltip(tooltip, isPlayerSneaking,
				tank.getCapability().cast());
	}
	
	// Settings
	public int getGenerationRate() {
		return 32;
	}
}
