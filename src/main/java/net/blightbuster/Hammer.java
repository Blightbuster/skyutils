package net.blightbuster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.stat.Stats;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class Hammer extends MiningToolItem {
    private static final Item[] DIRT_DROPS = new Item[]{Items.OAK_SAPLING,
            Items.ACACIA_SAPLING,
            Items.SPRUCE_SAPLING,
            Items.JUNGLE_SAPLING,
            Items.DARK_OAK_SAPLING,
            Items.BIRCH_SAPLING,
            Items.PUMPKIN_SEEDS,
            Items.MELON_SEEDS,
            Items.BEETROOT_SEEDS,
            Items.COCOA_BEANS,
            Items.SWEET_BERRIES,
            Items.BAMBOO,
            Items.SUGAR_CANE,
            Items.AZALEA};

    public Hammer(ToolMaterial material, int attackDamage, float attackSpeed, Item.Settings settings) {
        super((float) attackDamage, attackSpeed, material, null, settings);
    }

    private static boolean isLucky(float chance) {
        return Math.random() < Math.min(chance, 0.95f);
    }

    private static boolean isLucky(float chance, float bonusMul) {
        return Math.random() < Math.min(chance * (1 + bonusMul), 0.95f);
    }

    // Pick random weighted arg
    private static <T> T rdm(float arg1Weight, T arg1, T arg2) {
        return isLucky(arg1Weight) ? arg1 : arg2;
    }

    // Pick random arg
    private static <T> T rdm(T arg1, T arg2) {
        return isLucky(0.5f) ? arg1 : arg2;
    }

    // Pack random arg
    private static <T> T rdm(T arg1, T arg2, T arg3) {
        return isLucky(0.33333f) ? arg1 : rdm(arg2, arg3);
    }

    public static boolean remapDrop(World world, PlayerEntity player, BlockPos pos, BlockState state) {

        Identifier identifier = Registry.BLOCK.getId(state.getBlock());
        String path = identifier.getPath();

        float bonusMul = 0.0f;
        if (!player.getInventory().main.isEmpty()) {
            ItemStack tool = player.getInventory().getMainHandStack();
            int i = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);

            bonusMul += i;
        }

        if (path.endsWith("_log")) path = "_log";

        ItemStack droppedItems = switch (path) {
            case "cobblestone", "stone" -> new ItemStack(Items.GRAVEL);
            case "gravel" -> new ItemStack(rdm(0.8f, Items.SAND, Items.RED_SAND));
            case "sand" -> new ItemStack(Items.CLAY_BALL, 4);
            case "red_sand" -> new ItemStack(Items.CLAY_BALL, 3);
            case "_log" -> new ItemStack(SkyutilsMod.WOODCHIPS, 4);
            case "dirt", "podzol", "grass_block" -> new ItemStack(SkyutilsMod.PEBBLE, 4);
            case "moss_block", "rooted_dirt" -> new ItemStack(Items.DIRT);
            case "coal_block", "charcoal_block" -> {
                Item item = path.equals("coal_block") ? Items.COAL : Items.CHARCOAL;
                int lucky = isLucky(0.3f, bonusMul) ? 0 : 1;
                yield new ItemStack(item, 7 + lucky);
            }
            case "quartz_block" -> new ItemStack(Items.QUARTZ, 4);
            default -> null;
        };

        ItemStack droppedLuckyItems = switch (path) {
            case "gravel" -> isLucky(0.3f, bonusMul) ? new ItemStack(Items.IRON_NUGGET) : null;
            case "sand" -> {
                if (!isLucky(0.1f, bonusMul)) yield null;
                yield new ItemStack(rdm(Items.CACTUS, Items.KELP));
            }
            case "red_sand" -> isLucky(0.3f) ? new ItemStack(Items.REDSTONE) : null;
            case "podzol" -> {
                if (!isLucky(0.2f, bonusMul)) yield null;
                yield new ItemStack(rdm(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM));
            }
            case "grass_block" -> {
                if (!isLucky(0.2f, bonusMul)) yield null;
                yield new ItemStack(DIRT_DROPS[world.random.nextInt(DIRT_DROPS.length)]);
            }
            case "moss_block" -> isLucky(0.2f, bonusMul) ? new ItemStack(rdm(Items.GLOW_BERRIES, Items.SMALL_DRIPLEAF)) : null;
            case "rooted_dirt" -> isLucky(0.2f, bonusMul) ? new ItemStack(Items.POINTED_DRIPSTONE) : null;
            case "coal_block", "charcoal_block" -> {
                if (!isLucky(0.1f)) yield null;
                yield new ItemStack(rdm(0.7f, Items.QUARTZ, SkyutilsMod.DIAMOND_NUGGET));
            }
            case "netherrack" -> {
                if (isLucky(0.01f, bonusMul)) yield new ItemStack(Items.NETHERITE_SCRAP);
                if (isLucky(0.1f, bonusMul)) yield new ItemStack(Items.NETHER_WART);
                if (isLucky(0.2f, bonusMul)) {
                    yield new ItemStack(rdm(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS));
                }
                yield null;
            }
            default -> null;
        };

        if (droppedItems != null || droppedLuckyItems != null) {
            player.incrementStat(Stats.MINED.getOrCreateStat(state.getBlock()));
            player.addExhaustion(0.005F);
        }

        if (droppedItems != null) Block.dropStack(world, pos, droppedItems);
        if (droppedLuckyItems != null) Block.dropStack(world, pos, droppedLuckyItems);
        return true;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return isSuitableFor(state) ? this.miningSpeed : 1.0f;
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        Block block = state.getBlock();
        List effectiveBlocks = List.of(Blocks.STONE, Blocks.COBBLESTONE, Blocks.SAND, Blocks.RED_SAND, Blocks.GRAVEL, Blocks.GRASS_BLOCK, Blocks.PODZOL, SkyutilsMod.CHARCOAL_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.NETHERRACK, Blocks.COAL_BLOCK, Blocks.MOSS_BLOCK);
        return effectiveBlocks.contains(block) || state.isIn(BlockTags.LOGS);
    }
}