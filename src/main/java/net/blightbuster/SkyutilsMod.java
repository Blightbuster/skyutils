package net.blightbuster;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blightbuster.kiln.KilnBlock;
import net.blightbuster.kiln.KilnBlockEntity;
import net.blightbuster.kiln.KilnScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SkyutilsMod implements ModInitializer {

    public static SkyutilsConfig config;

    public static final Identifier KILN = new Identifier("skyutils", "kiln");
    public static final Identifier CONDENSER = new Identifier("skyutils", "condenser");
    public static final Identifier CHARCOAL_BLOCK_ID = new Identifier("skyutils", "charcoal_block");

    public static final Block CHARCOAL_BLOCK = new Block(
            Settings.of(Material.STONE, MapColor.BLACK).strength(5.0F, 6.0F));
    public static final BlockItem CHARCOAL_BLOCK_ITEM = new BlockItem(CHARCOAL_BLOCK,
            new Item.Settings().group(ItemGroup.MISC));
    public static final Item DIAMOND_NUGGET = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Item WOODCHIPS = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Item PEBBLE = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Item RAW_CRUCIBLE = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Crucible CRUCIBLE = new Crucible(Fluids.EMPTY, new Item.Settings().group(ItemGroup.MISC));
    public static final Crucible WATER_CRUCIBLE = new Crucible(Fluids.WATER,
            new Item.Settings().group(ItemGroup.MISC).maxCount(1));
    public static final Crucible LAVA_CRUCIBLE = new Crucible(Fluids.LAVA,
            new Item.Settings().group(ItemGroup.MISC).maxCount(1));
    public static final Hammer WOODEN_HAMMER = new Hammer(ToolMaterials.WOOD, 6, -2.8F,
            (new Item.Settings()).group(ItemGroup.TOOLS));
    public static final Hammer STONE_HAMMER = new Hammer(ToolMaterials.STONE, 6, -2.8F,
            (new Item.Settings()).group(ItemGroup.TOOLS));
    public static final Hammer IRON_HAMMER = new Hammer(ToolMaterials.IRON, 6, -2.8F,
            (new Item.Settings()).group(ItemGroup.TOOLS));
    public static final Hammer DIAMOND_HAMMER = new Hammer(ToolMaterials.DIAMOND, 6, -2.8F,
            (new Item.Settings()).group(ItemGroup.TOOLS));
    public static final Hammer NETHERITE_HAMMER = new Hammer(ToolMaterials.NETHERITE, 6, -2.8F,
            (new Item.Settings()).group(ItemGroup.TOOLS));

    // KILN

    public static BlockEntityType<KilnBlockEntity> KILN_ENTITY_TYPE;

    public static final Block KILN_BLOCK = new KilnBlock(Settings.of(Material.STONE).strength(3.5F, 3.5F));
    public static final BlockEntityType<KilnBlockEntity> KILN_ENTITY = FabricBlockEntityTypeBuilder
            .create(KilnBlockEntity::new, KILN_BLOCK).build(null);
    public static final BlockItem KILN_BLOCK_ITEM = new BlockItem(KILN_BLOCK,
            new Item.Settings().group(ItemGroup.REDSTONE));
    public static final ScreenHandlerType<KilnScreenHandler> KILN_SCREEN_HANDLER = ScreenHandlerRegistry
            .registerSimple(KILN, KilnScreenHandler::new);

    // CONDENSER
    public static final Block CONDENSER_BLOCK = new CondenserBlock();
    public static BlockEntityType<CondenserEntity> CONDENSER_ENTITY = FabricBlockEntityTypeBuilder
            .create(CondenserEntity::new, CONDENSER_BLOCK).build(null);
    public static final BlockItem CONDENSER_BLOCK_ITEM = new BlockItem(CONDENSER_BLOCK,
            new Item.Settings().group(ItemGroup.MISC));

    public static BlockPos spwn;

    @Override
    public void onInitialize() {

        load_config();

        // items
        Registry.register(Registry.ITEM, new Identifier("skyutils", "wooden_hammer"), WOODEN_HAMMER);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "stone_hammer"), STONE_HAMMER);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "iron_hammer"), IRON_HAMMER);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "diamond_hammer"), DIAMOND_HAMMER);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "netherite_hammer"), NETHERITE_HAMMER);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "woodchips"), WOODCHIPS);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "pebble"), PEBBLE);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "raw_crucible"), RAW_CRUCIBLE);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "crucible"), CRUCIBLE);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "water_crucible"), WATER_CRUCIBLE);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "lava_crucible"), LAVA_CRUCIBLE);
        Registry.register(Registry.ITEM, new Identifier("skyutils", "diamond_nugget"), DIAMOND_NUGGET);

        // blocks

        // charcoal block
        Registry.register(Registry.BLOCK, CHARCOAL_BLOCK_ID, CHARCOAL_BLOCK);
        Registry.register(Registry.ITEM, CHARCOAL_BLOCK_ID, CHARCOAL_BLOCK_ITEM);

        // kiln
        Registry.register(Registry.BLOCK, KILN, KILN_BLOCK);
        Registry.register(Registry.ITEM, KILN, KILN_BLOCK_ITEM);
        KILN_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, KILN, KILN_ENTITY);

        // condenser
        Registry.register(Registry.BLOCK, CONDENSER, CONDENSER_BLOCK);
        Registry.register(Registry.ITEM, CONDENSER, CONDENSER_BLOCK_ITEM);
        CONDENSER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CONDENSER, CONDENSER_ENTITY);

    }

    private void load_config() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toString(), "skyutils.json");
        try (FileReader reader = new FileReader(configFile)) {
            config = new Gson().fromJson(reader, SkyutilsConfig.class);
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
            } catch (IOException e2) {
                System.out.println("Failed to update config file!");
            }
            System.out.println("Config loaded!");

        } catch (IOException e) {
            System.out.println("No config found, generating!");
            config = new SkyutilsConfig();
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
            } catch (IOException e2) {
                System.out.println("Failed to generate config file!");
            }
        }
    }
}