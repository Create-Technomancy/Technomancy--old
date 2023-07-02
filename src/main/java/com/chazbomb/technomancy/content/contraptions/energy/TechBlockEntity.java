package com.chazbomb.technomancy.content.contraptions.energy;

/*
public abstract class TechTileEntity extends SmartTileEntity {

    protected SoulEnergyStorage SEStorage;
    protected LazyOptional<IEnergyStorage> energyCapability;
    protected final int CAPACITY, MAX_IN, MAX_OUT;
    private boolean init = true;

    public TechTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int CAPACITY, int MAX_IN, int MAX_OUT) {
        super(type, pos, state);
        SEStorage = new SoulEnergyStorage(CAPACITY);
        this.CAPACITY = CAPACITY;
        this.MAX_IN = MAX_IN;
        this.MAX_OUT = MAX_OUT;
        energyCapability = LazyOptional.of(() -> SEStorage);
        setLazyTickRate(20);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == CapabilityEnergy.ENERGY)
            return energyCapability.cast();
        return super.getCapability(cap, side);
    }

//    public abstract boolean isEnergyInput(Direction side);
//
//    public abstract boolean isEnergyOutput(Direction side);

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        SEStorage.deserializeNBT(compound);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        SEStorage.serializeNBT();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyCapability.invalidate();
    }

    @Override
    public void tick() {
        super.tick();
        if(init)
            init();
        init = false;
    }

    public void init() {
        updateCache();
    }

    public void updateCache() {
        if(level.isClientSide())
            return;
        for(Direction side : Direction.values()) {
            BlockEntity te = level.getBlockEntity(worldPosition.relative(side));
            if(te == null) {
                setCache(side, LazyOptional.empty());
                continue;
            }
            LazyOptional<IEnergyStorage> le = te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
            setCache(side, le);
        }
    }

    private LazyOptional<IEnergyStorage> capUp = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> capDown = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> capNorth = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> capEast = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> capSouth = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> capWest = LazyOptional.empty();

    public void setCache(Direction side, LazyOptional<IEnergyStorage> storage) {
        switch (side) {
            case DOWN -> capDown = storage;
            case EAST -> capEast = storage;
            case NORTH -> capNorth = storage;
            case SOUTH -> capSouth = storage;
            case UP -> capUp = storage;
            case WEST -> capWest = storage;
        }
    }

    public IEnergyStorage getCachedEnergy(Direction side) {
        return switch (side) {
            case DOWN -> capDown.orElse(null);
            case EAST -> capEast.orElse(null);
            case NORTH -> capNorth.orElse(null);
            case SOUTH -> capSouth.orElse(null);
            case UP -> capUp.orElse(null);
            case WEST -> capWest.orElse(null);
        };
    }
}
*/