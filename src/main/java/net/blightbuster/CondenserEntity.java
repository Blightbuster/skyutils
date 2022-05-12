package net.blightbuster;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class CondenserEntity extends BlockEntity {

    // Store the current value of the number
    private int time = 0;
    // private int time_limit = 0;
    private int level = 0;

    public CondenserEntity(BlockPos pos, BlockState state) {
        super(SkyutilsMod.CONDENSER_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CondenserEntity blockEntity) {
        if (!world.isClient) {

            int time_limit = 2400;
            Biome biome = world.getBiome(pos).value();
            float temperature = biome.getTemperature();
            boolean raining = world.isRaining();

            if (temperature >= 0.95f) {
                time_limit = time_limit * 2;
            }
            if (biome.getPrecipitation() == Biome.Precipitation.RAIN && raining) {
                time_limit = (int) (time_limit * 0.05);

            }

            int d = time_limit / 7;

            if (state.getBlock() instanceof CondenserBlock) {
                CondenserBlock block = (CondenserBlock) state.getBlock();

                if (blockEntity.getTime() > time_limit) {
                    blockEntity.setTime(0);
                    if (blockEntity.getTime() % d == 0 && blockEntity.getLevel() < 7) {
                        blockEntity.incLevel();
                        block.incLevel(world, pos, state);

                    }
                }
                blockEntity.incTime();
                blockEntity.markDirty();
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int l) {
        level = l;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int t) {
        time = t;
    }

    public void incLevel() {
        if (level < 7)
            level++;
    }

    public void incTime() {
        time++;
    }

    public void empty() {
        level = 0;
        time = 0;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        // Save the current value of the number to the tag
        tag.putInt("number", time);
        tag.putInt("level", level);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        time = tag.getInt("number");
        level = tag.getInt("level");
    }
}