package com.fredoseep;

import me.voidxwalker.autoreset.api.seedprovider.AtumWaitingScreen;
import me.voidxwalker.autoreset.api.seedprovider.SeedProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.ChunkRandom;

import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;

import net.minecraft.world.gen.feature.BuriedTreasureFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class TreasureSeedProvider implements SeedProvider {
    private final Random random = new Random();

    @Override
    public CompletableFuture<String> requestSeed() {
        return CompletableFuture.supplyAsync(() -> {
            long seed;
            while (true) {
                seed = random.nextLong();
                if (checkSeed(seed)) {
                    return String.valueOf(seed);
                }
            }
        });
    }

    private boolean checkSeed(long seed) {
        VanillaLayeredBiomeSource biomeSource = new VanillaLayeredBiomeSource(seed, false, false);
        ChunkGeneratorType type = ChunkGeneratorType.Preset.OVERWORLD.getChunkGeneratorType();
        SurfaceChunkGenerator generator = new SurfaceChunkGenerator(biomeSource, seed, type);
        ChunkRandom chunkRandom = new ChunkRandom();
        BuriedTreasureFeatureConfig config = new BuriedTreasureFeatureConfig(0.01F);

        List<Biome> spawnBiomes = biomeSource.getSpawnBiomes();
        BlockPos spawnPos = biomeSource.locateBiome(0, 64, 0, 256, spawnBiomes, new Random(seed));
        ChunkPos spawnChunk = (spawnPos == null) ? new ChunkPos(0, 0) : new ChunkPos(spawnPos);

        int radius = 5;

        for (int x = spawnChunk.x - radius; x <= spawnChunk.x + radius; x++) {
            for (int z = spawnChunk.z - radius; z <= spawnChunk.z + radius; z++) {


                Biome biome = biomeSource.getBiomeForNoiseGen((x << 2) + 2, 0, (z << 2) + 2);


                if (biome.getCategory() != Biome.Category.BEACH) {
                    continue;
                }

                // 4. 检查随机概率
                if (StructureFeature.BURIED_TREASURE.shouldStartAt(
                        generator,
                        biomeSource,
                        seed,
                        chunkRandom,
                        x, z,
                        biome,
                        new ChunkPos(x, z),
                        config
                )) {
                    return true;
                }
            }
        }
        return false;
    }



    @Override
    public boolean shouldShowSeed() {
        return false;
    }

    @Override
    public Optional<AtumWaitingScreen> getWaitingScreen() {
        return Optional.of(new TreasureWaitingScreen(new LiteralText("bt seed")));
    }

    @Override
    public void onFail(@Nullable Throwable ex) {
        System.err.println("种子筛选出错: " + ex.getMessage());
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("种子筛选器出错！"));
        }
    }
}