package motherlode.spelunky;

import motherlode.spelunky.block.PotBlock;
import motherlode.spelunky.client.ExplosiveItemEntityRenderer;
import motherlode.spelunky.entity.ExplosiveItemEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class MotherlodeSpelunkyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register packets.
        ClientSidePacketRegistry.INSTANCE.register(MotherlodeModule.BOMB_SPAWN_PACKET, (ctx, packet) -> spawnExplosiveEntity(ctx, packet, MotherlodeSpelunkyEntities.BOMB_ENTITY));
        ClientSidePacketRegistry.INSTANCE.register(MotherlodeModule.DYNAMITE_SPAWN_PACKET, (ctx, packet) -> spawnExplosiveEntity(ctx, packet, MotherlodeSpelunkyEntities.DYNAMITE_ENTITY));
        ClientSidePacketRegistry.INSTANCE.register(MotherlodeModule.STICKY_DYNAMITE_PACKET, (ctx, packet) -> spawnExplosiveEntity(ctx, packet, MotherlodeSpelunkyEntities.STICKY_DYNAMITE_ENTITY));
        
        // Register entities.
        EntityRendererRegistry.INSTANCE.register(MotherlodeSpelunkyEntities.BOMB_ENTITY, (ctx) -> new ExplosiveItemEntityRenderer(ctx, MotherlodeSpelunkyItems.BOMB));
        EntityRendererRegistry.INSTANCE.register(MotherlodeSpelunkyEntities.DYNAMITE_ENTITY, (ctx) -> new ExplosiveItemEntityRenderer(ctx, MotherlodeSpelunkyItems.DYNAMITE));
        EntityRendererRegistry.INSTANCE.register(MotherlodeSpelunkyEntities.STICKY_DYNAMITE_ENTITY, (ctx) -> new ExplosiveItemEntityRenderer(ctx, MotherlodeSpelunkyItems.DYNAMITE_STICKY));
        
        BlockRenderLayerMap.INSTANCE.putBlock(MotherlodeSpelunkyBlocks.POT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(MotherlodeSpelunkyBlocks.ROPE, RenderLayer.getCutout());
        
        ColorProviderRegistry.BLOCK.register((state, _world, _pos, _tintIndex) -> state.get(PotBlock.COLOR).getColor(), MotherlodeSpelunkyBlocks.POT);
        
        FabricModelPredicateProviderRegistry.register(MotherlodeSpelunkyBlocks.POT.asItem(), new Identifier("pot_pattern"), (itemStack, world, entity, i) -> {
            CompoundTag tag = itemStack.getTag();
            if (tag == null || !tag.contains("BlockStateTag"))
                return 0;
            tag = tag.getCompound("BlockStateTag");
            if (tag == null || !tag.contains("pattern"))
                return 0;
            
            return Integer.parseInt(tag.getString("pattern")) / 100f;
        });
        
        FabricModelPredicateProviderRegistry.register(new Identifier("stack_count"), (itemStack, world, entity, i) -> itemStack.getCount() / 100f);
    }
    
    private void spawnExplosiveEntity(PacketContext ctx, PacketByteBuf packet, EntityType<ExplosiveItemEntity> entityType) {
        ExplosiveItemEntity entity = new ExplosiveItemEntity(entityType, MinecraftClient.getInstance().world);
        
        BlockPos spawnLocation = packet.readBlockPos();
        int entityId = packet.readInt();
        UUID entityUuid = packet.readUuid();
        
        entity.setEntityId(entityId);
        entity.setUuid(entityUuid);
        entity.updatePosition(
                spawnLocation.getX(),
                spawnLocation.getY(),
                spawnLocation.getZ()
        );
        
        ctx.getTaskQueue().execute(() -> MinecraftClient.getInstance().world.addEntity(entityId, entity));
    }
}
