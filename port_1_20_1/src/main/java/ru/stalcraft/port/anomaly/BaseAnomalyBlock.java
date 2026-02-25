package ru.stalcraft.port.anomaly;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.stalcraft.port.registry.ModBlockEntities;

public class BaseAnomalyBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final VoxelShape SHAPE = Shapes.box(0.05D, 0.0D, 0.05D, 0.95D, 0.90D, 0.95D);

    private final AnomalyType anomalyType;

    public BaseAnomalyBlock(AnomalyType anomalyType) {
        super(Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(1.5F, 6.0F)
            .sound(SoundType.GLASS)
            .noOcclusion());
        this.anomalyType = anomalyType;
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    public AnomalyType anomalyType() {
        return anomalyType;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BaseAnomalyBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ANOMALY.get(),
            level.isClientSide ? BaseAnomalyBlockEntity::clientTick : BaseAnomalyBlockEntity::serverTick);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BaseAnomalyBlockEntity anomaly) {
            anomaly.requestImmediateCheck(entity);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BaseAnomalyBlockEntity anomaly) {
            anomaly.requestImmediateCheck(entity);
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable net.minecraft.world.entity.LivingEntity placer,
        net.minecraft.world.item.ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof BaseAnomalyBlockEntity anomaly) {
            anomaly.setLastActivator(serverPlayer.getUUID().toString());
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return false;
    }
}
