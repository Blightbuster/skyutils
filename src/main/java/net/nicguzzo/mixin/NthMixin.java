package net.nicguzzo.mixin;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.TheNetherDimension;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;
import net.nicguzzo.SkyutilsMod;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(TheNetherDimension.class)
public abstract class NthMixin extends Dimension {

    public NthMixin(World world, DimensionType type,float f) {
        super(world, type,f);
    }

    @Inject(method = "createChunkGenerator()Lnet/minecraft/world/gen/chunk/ChunkGenerator;", at = @At("HEAD"), cancellable = true)
    private void createSkyBlockGenerator(CallbackInfoReturnable<ChunkGenerator<? extends ChunkGeneratorConfig>> cir) {
        LevelGeneratorType type = this.world.getLevelProperties().getGeneratorType();
        if (type == SkyutilsMod.SKB_LEVEL_GENERATOR_TYPE) {
            cir.setReturnValue(SkyutilsMod.createNthChunkGenerator(this.world));
            cir.cancel();
        }
    }
}
