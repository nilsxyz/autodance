package net.motionrupf.autodance.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldUtil {
    @SuppressWarnings("Duplicates")
    public static BlockPos findNext(World world, BlockPos from, IBlockState blockState) {
        double currentDistance = Double.MAX_VALUE;

        if(world.getBlockState(from).equals(blockState)) {
            return from;
        }

        BlockPos bestPos = null;
        for(int xOffset = 0; xOffset < 200; xOffset++) {
            for(int yOffset = 0; yOffset < 200; yOffset++) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                double newDistance = newPos.distanceSq(from);

                if(foundBlockState.equals(blockState) && newDistance < currentDistance) {
                    bestPos = newPos;
                    currentDistance = newDistance;
                }
            }
        }

        for(int xOffset = 0; xOffset < 200; xOffset++) {
            for(int yOffset = 0; yOffset > -200; yOffset--) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                double newDistance = newPos.distanceSq(from);

                if(foundBlockState.equals(blockState) && newDistance < currentDistance) {
                    bestPos = newPos;
                    currentDistance = newDistance;
                }
            }
        }

        for(int xOffset = 0; xOffset > -200; xOffset--) {
            for(int yOffset = 0; yOffset < 200; yOffset++) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                double newDistance = newPos.distanceSq(from);

                if(foundBlockState.equals(blockState) && newDistance < currentDistance) {
                    bestPos = newPos;
                    currentDistance = newDistance;
                }
            }
        }

        for(int xOffset = 0; xOffset > -200; xOffset--) {
            for(int yOffset = 0; yOffset > -200; yOffset--) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                double newDistance = newPos.distanceSq(from);

                if(foundBlockState.equals(blockState) && newDistance < currentDistance) {
                    bestPos = newPos;
                    currentDistance = newDistance;
                }
            }
        }

        return bestPos;
    }

    @SuppressWarnings("Duplicates")
    public static BlockPos findRandom(World world, BlockPos from, Random random, boolean secure) {
        List<BlockPos> positions = new ArrayList<>();

        for(int xOffset = 0; xOffset < 5; xOffset++) {
            for(int yOffset = 0; yOffset < 5; yOffset++) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                IBlockState foundUpperBlockState = world.getBlockState(newPos.up());

                if(foundBlockState.getBlock() != Blocks.AIR && foundUpperBlockState.getBlock() == Blocks.AIR &&
                        (!secure || isSecure(world, newPos))) {
                    positions.add(newPos);
                }
            }
        }

        for(int xOffset = 0; xOffset > -5; xOffset--) {
            for(int yOffset = 0; yOffset < 5; yOffset++) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                IBlockState foundUpperBlockState = world.getBlockState(newPos.up());

                if(foundBlockState.getBlock() != Blocks.AIR && foundUpperBlockState.getBlock() == Blocks.AIR &&
                        (!secure || isSecure(world, newPos))) {
                    positions.add(newPos);
                }
            }
        }

        for(int xOffset = 0; xOffset < 5; xOffset++) {
            for(int yOffset = 0; yOffset > -5; yOffset--) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                IBlockState foundUpperBlockState = world.getBlockState(newPos.up());

                if(foundBlockState.getBlock() != Blocks.AIR && foundUpperBlockState.getBlock() == Blocks.AIR &&
                        (!secure || isSecure(world, newPos))) {
                    positions.add(newPos);
                }
            }
        }

        for(int xOffset = 0; xOffset > -5; xOffset--) {
            for(int yOffset = 0; yOffset > -5; yOffset--) {
                BlockPos newPos = from.add(xOffset, 0, yOffset);
                IBlockState foundBlockState = world.getBlockState(newPos);
                IBlockState foundUpperBlockState = world.getBlockState(newPos.up());

                if(foundBlockState.getBlock() != Blocks.AIR && foundUpperBlockState.getBlock() == Blocks.AIR &&
                        (!secure || isSecure(world, newPos))) {
                    positions.add(newPos);
                }
            }
        }

        if(positions.size() < 1) {
            return null;
        }

        int r = random.nextInt(positions.size() * 2);
        if(r > positions.size() - 1) {
            return null;
        } else {
            return positions.get(r);
        }
    }

    public static BlockPos getNextSolidOrOriginalPosOffset(Entity entity, int offset) {
        BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
        World world = entity.getEntityWorld();
        while(world.getBlockState(pos = pos.down()).getBlock() == Blocks.AIR) {
            if(pos.getY() < 0) {
                pos = entity.getPosition();
                return offset > 0 ? pos.up(offset) : (offset < 0 ? pos.down(offset * -1) : pos);
            }
        }

        return pos;
    }

    private static boolean isSecure(World world, BlockPos pos) {
        return world.getBlockState(pos.north()).getBlock() != Blocks.AIR &&
                world.getBlockState(pos.south()).getBlock() != Blocks.AIR &&
                world.getBlockState(pos.east()).getBlock() != Blocks.AIR &&
                world.getBlockState(pos.west()).getBlock() != Blocks.AIR;
    }
}
