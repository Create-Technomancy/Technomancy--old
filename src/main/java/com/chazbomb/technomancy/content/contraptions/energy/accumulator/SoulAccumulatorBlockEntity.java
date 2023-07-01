package com.chazbomb.technomancy.content.contraptions.energy.accumulator;


import com.chazbomb.technomancy.registry.TMFluids;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

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

public class SoulAccumulatorBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
	public SoulAccumulatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		setLazyTickRate(20);
	}
	
	private SmartFluidTankBehaviour tank;
	
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
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
