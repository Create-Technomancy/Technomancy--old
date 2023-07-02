package com.chazbomb.technomancy.content.contraptions.components.combustor;


import com.chazbomb.technomancy.content.contraptions.energy.cable.CableBlock;
import com.chazbomb.technomancy.content.contraptions.energy.cable.CablePropagator;
import com.chazbomb.technomancy.foundation.energy.AetherStorage;
import com.chazbomb.technomancy.foundation.energy.AetherStorageBehavior;
import com.chazbomb.technomancy.foundation.energy.IAetherStorage;
import com.chazbomb.technomancy.registry.TMCapabilities;
import com.chazbomb.technomancy.registry.TMFluids;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoulBurnerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

	protected SmartFluidTankBehaviour tank;
	protected AetherStorageBehavior storage;

	public SoulBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		setLazyTickRate(10);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		// Tanks
		tank = SmartFluidTankBehaviour.single(this, 1000);
		behaviours.add(tank);
		// Energy
		storage = AetherStorageBehavior.generating(this, 1000, this::onAetherChanged);
		behaviours.add(storage);
	}

//	@Override
//	public AABB getRenderBoundingBox() {
//		return super.getRenderBoundingBox().expandTowards(new Vec3(0, 3, 0));
//	}

//	Is this needed?
//	@Override
//	public void write(CompoundTag compound, boolean clientPacket) {
//
//
//		super.write(compound, clientPacket);
//	}
//
//	@Override
//	protected void read(CompoundTag compound, boolean clientPacket) {
//		super.read(compound, clientPacket);
//
//	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (isFluidHandlerCap(cap) && side != Direction.UP)
			return tank.getCapability().cast();

		if (cap == TMCapabilities.AETHER && side != Direction.UP)
			return storage.getCapability().cast();

		return super.getCapability(cap, side);
	}

	private static final FluidStack burnerFuel = new FluidStack(FluidHelper.convertToStill(TMFluids.LIQUID_SOULS.get()), 1);

	@Override
	public void lazyTick() {
		super.lazyTick();

		SmartFluidTank fluidHandler = tank.getPrimaryHandler();
		AetherStorage aetherHandler = storage.getHandler();

		// convert
		if(!fluidHandler.isEmpty()
				&& fluidHandler.getFluid().containsFluid(burnerFuel)
				&& fluidHandler.getFluidAmount() >= 100
				&& aetherHandler.getSpace() >= 20) {
			fluidHandler.drain(100, IFluidHandler.FluidAction.EXECUTE);
			aetherHandler.generateAether(20, false);
		}

		// distribute
		Map<Direction, LazyOptional<IAetherStorage>> connections = new HashMap<>();

		for(Direction dir : Iterate.directions) {
//			if(dir == Direction.UP) continue;
			BlockEntity neighbourTE = level.getBlockEntity(this.getBlockPos().relative(dir));
			if(neighbourTE == null) continue;
			if(CableBlock.isCable(neighbourTE.getBlockState())) CablePropagator.propagateChangedCable(level, neighbourTE.getBlockPos(), neighbourTE.getBlockState());
			LazyOptional<IAetherStorage> cap = neighbourTE.getCapability(TMCapabilities.AETHER, dir.getOpposite());
			if(!cap.isPresent() || connections.containsValue(cap)) continue;
			connections.put(dir, cap);
		}

		if(!connections.isEmpty()) {
			connections.forEach((direction, energyStorage) -> {
				energyStorage.ifPresent(storage -> {
					int maxPush = aetherHandler.extractAether(20, true);
					if(maxPush == 0) return;
					int transferred = storage.receiveAether(maxPush, false);
					if(transferred == 0) return;
					aetherHandler.extractAether(transferred, false);
					notifyUpdate();
				});
			});
		}
	}

	protected void onAetherChanged(int aether) {
		if (!hasLevel())
			return;

		if (!level.isClientSide) {
			setChanged();
			sendData();
		}
	}
	
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		boolean fluid = containedFluidTooltip(tooltip, isPlayerSneaking,
				getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
		if (fluid)
			tooltip.add(Components.immutableEmpty());
		return AetherStorageBehavior.containedAetherTooltip(tooltip, storage.getCapability());
	}
}
