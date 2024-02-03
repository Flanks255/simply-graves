package com.flanks255.simplygraves.render;

import com.flanks255.simplygraves.SGBlocks;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class EntityRenders {
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(SGBlocks.GRAVE_ENTITY.get(), $ -> new GraveTextRender());
    }
}
