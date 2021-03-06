package motherlode.biomes.world.feature;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import motherlode.biomes.MotherlodeBiomesBlocks;
import motherlode.biomes.block.ReedsBlock;
import com.mojang.serialization.Codec;

public class MarshFeature extends Feature<DefaultFeatureConfig> {
    public MarshFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        boolean b = false;
        BlockPos pos = context.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, context.getOrigin()).down();
        boolean[][] replace = new boolean[24][24];
        if (context.getWorld().getBlockState(pos.up()).isAir()) {
            for (int a = 0; a < 24; a++) {
                for (int c = 0; c < 24; c++) {
                    int x = a - 12;
                    int z = c - 12;
                    BlockPos mpos = pos.add(x, 0, z);
                    if (shouldReplace(context.getWorld(), mpos) && Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) <= 12) {
                        b = true;
                        replace[a][c] = true;
                    }
                }
            }
            for (int a = 0; a < 24; a++) {
                for (int c = 0; c < 24; c++) {
                    int x = a - 12;
                    int z = c - 12;
                    if (replace[a][c]) {
                        BlockPos mpos = pos.add(x, 0, z);
                        int r = context.getRandom().nextInt(9);
                        if (r == 1) {
                            context.getWorld().setBlockState(mpos, MotherlodeBiomesBlocks.WATERPLANT.getDefaultState(), 2);
                        } else if (r == 2) {
                            context.getWorld().setBlockState(mpos, Blocks.SEAGRASS.getDefaultState(), 2);
                        } else if (r == 3) {
                            int s = context.getRandom().nextInt(6);
                            int t = context.getRandom().nextInt(6);
                            Block k;
                            if (s == 1 || s == 2) k = MotherlodeBiomesBlocks.REEDS;
                            else if (s == 3 || s == 4 || s == 5) k = MotherlodeBiomesBlocks.CATTAIL_REEDS;
                            else k = MotherlodeBiomesBlocks.DRY_REEDS;
                            if (t == 1 || t == 2) {
                                context.getWorld().setBlockState(mpos, k.getDefaultState().with(ReedsBlock.TYPE, ReedsBlock.Type.WATERLOGGED), 2);
                            } else if (t == 3 || t == 4 || t == 5) {
                                context.getWorld().setBlockState(mpos, k.getDefaultState().with(ReedsBlock.TYPE, ReedsBlock.Type.BOTTOM), 2);
                                context.getWorld().setBlockState(mpos.up(), k.getDefaultState().with(ReedsBlock.TYPE, ReedsBlock.Type.TOP), 2);
                            } else {
                                context.getWorld().setBlockState(mpos, k.getDefaultState().with(ReedsBlock.TYPE, ReedsBlock.Type.BOTTOM), 2);
                                context.getWorld().setBlockState(mpos.up(), k.getDefaultState().with(ReedsBlock.TYPE, ReedsBlock.Type.MIDDLE), 2);
                                context.getWorld().setBlockState(mpos.up(2), k.getDefaultState().with(ReedsBlock.TYPE, ReedsBlock.Type.TOP), 2);
                            }
                        } else
                            context.getWorld().setBlockState(mpos, Blocks.WATER.getDefaultState().with(Properties.LEVEL_15, 0), 2);
                        if (!context.getWorld().getBlockState(mpos.up()).isAir() && r != 3) {
                            context.getWorld().setBlockState(mpos.up(), Blocks.AIR.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        return b;
    }

    private static boolean shouldReplace(BlockView world, BlockPos pos) {
        boolean b = true;
        if (world.getBlockState(pos).isAir()) b = false;
        if (!world.getBlockState(pos.north()).isSolidBlock(world, pos) && world.getFluidState(pos.north()).getFluid() != Fluids.WATER)
            b = false;
        if (!world.getBlockState(pos.south()).isSolidBlock(world, pos) && world.getFluidState(pos.north()).getFluid() != Fluids.WATER)
            b = false;
        if (!world.getBlockState(pos.east()).isSolidBlock(world, pos) && world.getFluidState(pos.north()).getFluid() != Fluids.WATER)
            b = false;
        if (!world.getBlockState(pos.west()).isSolidBlock(world, pos) && world.getFluidState(pos.north()).getFluid() != Fluids.WATER)
            b = false;
        if (world.getBlockState(pos.up()).isSolidBlock(world, pos)) b = false;
        if (!world.getBlockState(pos.down()).isSolidBlock(world, pos)) b = false;
        return b;
    }
}
