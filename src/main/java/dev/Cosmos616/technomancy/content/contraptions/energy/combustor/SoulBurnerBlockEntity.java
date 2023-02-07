package dev.Cosmos616.technomancy.content.contraptions.energy.combustor;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import dev.Cosmos616.technomancy.content.contraptions.base.GeneratingTechnomaticTileEntity;
import dev.Cosmos616.technomancy.foundation.energy.SoulEnergyTileBehavior;
import dev.Cosmos616.technomancy.registry.TMCapabilities;
import dev.Cosmos616.technomancy.registry.TMFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulBurnerBlockEntity extends GeneratingTechnomaticTileEntity implements IHaveGoggleInformation {

	public SoulBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		setLazyTickRate(20);
	}
	
	protected SmartFluidTankBehaviour tank;
//	protected SoulEnergyTileBehavior energy;
	
	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		// Tanks
		tank = SmartFluidTankBehaviour.single(this, 1000);
		behaviours.add(tank);
		// Energy
//		energy = new SoulEnergyTileBehavior(this, 1024);
//		behaviours.add(energy.forbidInsertion());
	}

	@Override
	public AABB getRenderBoundingBox() {
		return super.getRenderBoundingBox().expandTowards(new Vec3(0, 3, 0));
	}

//	@Override
//	protected void read(CompoundTag compound, boolean clientPacket) {
//		super.read(compound, clientPacket);
//		if (compound.contains("Flows")) {
//			for (Direction d : Iterate.directions)
//				setFlow(d, compound.getCompound("Flows")
//						.getFloat(d.getSerializedName()));
//		}
//	}
//
//	@Override
//	public void write(CompoundTag compound, boolean clientPacket) {
//		CompoundTag flows = new CompoundTag();
//		for (Direction d : Iterate.directions)
//			flows.putFloat(d.getSerializedName(), this.flows.get(d));
//		compound.put("Flows", flows);
//
//		super.write(compound, clientPacket);
//	}

//	@NotNull
//	@Override
//	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//		if (side == null)
//			return super.getCapability(cap, null);
//
//		if (isFluidHandlerCap(cap) && side.getAxis().isHorizontal())
//			return tank.getCapability().cast();
//		if (cap == TMCapabilities.SOUL_ENERGY && side.equals(Direction.UP))
//			return energy.getCapability().cast();
//
//		return super.getCapability(cap, side);
//	}
	
//	private static final FluidStack comparedStack = new FluidStack(FluidHelper.convertToStill(TMFluids.LIQUID_SOULS.get()), 1);
//	@Override
//	public void lazyTick() {
//		super.lazyTick();
//
//		SmartFluidTank fluidHandler = tank.getPrimaryHandler();
//		if (fluidHandler.isEmpty() || !fluidHandler.getFluid().containsFluid(comparedStack) || fluidHandler.getFluidAmount() < 300
//				|| energy.getCapacity() == energy.getStoredSouls())
//			return;
//		fluidHandler.drain(90, IFluidHandler.FluidAction.EXECUTE);
//		energy.generateSouls(30);
//	}
	
//	@Override
//	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//		boolean fluid = containedFluidTooltip(tooltip, isPlayerSneaking, this.tank.getCapability().cast());
//		if (fluid)
//			tooltip.add(Components.immutableEmpty());
//		return energy.containedEnergyTooltip(tooltip, isPlayerSneaking);
//	}

	@Override
	public float getGeneratedEmission() {
//		float emission = 0;
//		for (Float f : flows.values())
//			emission += f;
//		if (emission != 0)
			//TODO: replace with config
//			emission += TMConfigs.SERVER.magics.soulBurnerBaseEmission.get() * Math.signum(emission);
//			emission += 4 * Math.signum(emission);
		return 64f;
	}
}
