package dev.Cosmos616.technomancy.content.contraptions.components.laser;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import dev.Cosmos616.technomancy.foundation.energy.SoulEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaserTileEntity extends SmartTileEntity implements IHaveGoggleInformation {
	protected LazyOptional<IEnergyStorage> lazyEnergy;
	protected SoulEnergyStorage internalCapacity;
	
	public LaserTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.internalCapacity = new SoulEnergyStorage(getCapacity());
		this.lazyEnergy = LazyOptional.of(() -> internalCapacity);
		this.setLazyTickRate(12);
	}
	
	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviours) { }
	
	protected int redstoneInput = 0;
	public void updateSignal(int input) {
		this.redstoneInput = input;
	}
	public boolean hasSignal() {
		return this.redstoneInput > 0;
	}
	
	protected boolean running = false;
	
	protected int beamTick = 0;
	@Override
	public void tick() {
		super.tick();
		if (this.level.isClientSide())
			return;
		
		if (!(this.running = this.internalCapacity.getEnergyStored() > getConsumptionRate() && this.redstoneInput > 0))
			return;
		
		if (this.beamTick++ < 4)
			return;
		this.beamTick = 0;
		this.tickBeam();
	}
	
	protected float beamDistance = 0.0f;
	public float getBeamDistance() {
		return this.beamDistance;
	}
	
	@Override
	public void lazyTick() {
		if (this.level.isClientSide())
			return;
		sendData();
		
		// Consume energy
		if (this.running)
//			this.internalCapacity.consumeEnergy(getConsumptionRate());;
		this.internalCapacity.extractEnergy(getConsumptionRate(), false); //???
	}
	
	public void tickBeam() {
		Direction facing = this.getBlockState().getValue(DirectionalBlock.FACING);
		int fDist = 0;
		for (int dist = 1; dist <= getMaxLaserDistance(); dist++) {
			BlockPos testPos = this.worldPosition.relative(facing, dist);
			BlockState hit = this.level.getBlockState(testPos);
			if ((hit.isAir() || !hit.canOcclude()) && dist != getMaxLaserDistance()) continue;
			fDist = dist;
			break;
		}
		if (fDist != this.beamDistance) {
			this.beamDistance = fDist;
			sendData();
		}
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		if (this.internalCapacity != null)
			this.internalCapacity.setEnergy(tag.contains("StoredEnergy") ? tag.getInt("StoredEnergy") : 0);
		this.beamDistance = tag.contains("BeamDistance") ? tag.getFloat("BeamDistance") : 0.0f;
		this.redstoneInput = tag.contains("RedstoneLevel") ? tag.getInt("RedstoneLevel") : 0;
		this.running = tag.contains("Running") && tag.getBoolean("Running");
		updateRenderBoundingBox();
	}
	
	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		if (this.internalCapacity != null)
			tag.putInt("StoredEnergy", this.internalCapacity.getEnergyStored());
		tag.putFloat("BeamDistance", this.beamDistance);
		tag.putInt("RedstoneLevel", redstoneInput);
		tag.putBoolean("Running", running);
		super.write(tag, clientPacket);
	}
	
	@OnlyIn(Dist.CLIENT)
	public AABB updateRenderBoundingBox() {
		Direction facing = this.getBlockState().getValue(DirectionalBlock.FACING);
		return (renderBoundingBox = new AABB(this.worldPosition, this.worldPosition.relative(facing, (int) getBeamDistance())));
	}
	
	protected AABB renderBoundingBox;
	@Override
	public AABB getRenderBoundingBox() {
		return renderBoundingBox == null ? updateRenderBoundingBox() : renderBoundingBox;
	}
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityEnergy.ENERGY && side.getAxis() != this.getBlockState().getValue(LaserBlock.FACING).getAxis())
			return this.lazyEnergy.cast();
		return super.getCapability(cap, side);
	}
	
	// Config
	// todo add all as part of config
	public static int getCapacity() {
		return 64;
	}
	
	public static int getConsumptionRate() { // Consumption per 20 ticks
		return 4;
	}
	
	public static float getMaxLaserDistance() { // In Blocks
		return 32.0f;
	}
}
