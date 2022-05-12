package net.blightbuster;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CondenserBlock extends HorizontalFacingBlock implements BlockEntityProvider, FluidDrainable {
    public static final IntProperty LEVEL;

    static {
        LEVEL = IntProperty.of("level", 0, 7);
    }

    public CondenserBlock() {
        super(Settings.of(Material.WOOD).nonOpaque());
        setDefaultState(this.stateManager.getDefaultState().with(LEVEL, 0).with(FACING, Direction.NORTH));
    }

    @Nullable
    static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType,
                                                                                         BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (@Nullable BlockEntityTicker<A>) ticker : null;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
                                                                  BlockEntityType<T> type) {
        return !world.isClient ? checkType(type, SkyutilsMod.CONDENSER_ENTITY, CondenserEntity::tick) : null;
    }

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Fluids.WATER.getBucketFillSound();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(LEVEL, Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CondenserEntity(pos, state);
    }

    public void setLevel(World world, BlockPos pos, BlockState state, int level) {

        world.setBlockState(pos, state.with(LEVEL, MathHelper.clamp(level, 0, 7)), 2);
        world.updateNeighbors(pos, this);
    }

    public void incLevel(World world, BlockPos pos, BlockState state) {
        int level = state.get(LEVEL) + 1;
        world.setBlockState(pos, state.with(LEVEL, MathHelper.clamp(level, 0, 7)), 2);
        world.updateNeighbors(pos, this);
    }

    public int getLevel(BlockState state) {
        return state.get(LEVEL);
    }

    @Override
    public ItemStack tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
        if (!world.isClient()) {
            int i = state.get(LEVEL);
            if (i == 7) {
                this.setLevel((World) world, pos, state, 0);
                CondenserEntity e = (CondenserEntity) world.getBlockEntity(pos);
                if (e != null) {
                    e.empty();
                }

                ((World) world).playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 1f, true);
                return new ItemStack(SkyutilsMod.WATER_CRUCIBLE);
            }
        }
        return ItemStack.EMPTY;
    }
}
