package com.flanks255.simplygraves;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class SGBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SimplyGraves.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SimplyGraves.MODID);

    public static final DeferredBlock<GraveBlock> GRAVESTONE = registerGrave(Grave.WOOD);
    public static final DeferredBlock<GraveBlock> ANDESITE_GRAVESTONE = registerGrave(Grave.ANDESITE);
    public static final DeferredBlock<GraveBlock> BASSALT_GRAVESTONE = registerGrave(Grave.BASSALT);
    public static final DeferredBlock<GraveBlock> CALCITE_GRAVESTONE = registerGrave(Grave.CALCITE);
    public static final DeferredBlock<GraveBlock> DARK_PRISMARINE_GRAVESTONE = registerGrave(Grave.DARK_PRISMARINE);
    public static final DeferredBlock<GraveBlock> DEEPSLATE_GRAVESTONE = registerGrave(Grave.DEEPSLATE);
    public static final DeferredBlock<GraveBlock> DIORITE_GRAVESTONE = registerGrave(Grave.DIORITE);
    public static final DeferredBlock<GraveBlock> DRIPSTONE_GRAVESTONE = registerGrave(Grave.DRIPSTONE);
    public static final DeferredBlock<GraveBlock> GRANITE_GRAVESTONE = registerGrave(Grave.GRANITE);
    public static final DeferredBlock<GraveBlock> STONE_GRAVESTONE = registerGrave(Grave.STONE);
    public static final DeferredBlock<GraveBlock> END_STONE_GRAVESTONE = registerGrave(Grave.END_STONE);
    public static final DeferredBlock<GraveBlock> BLACKSTONE_GRAVESTONE = registerGrave(Grave.BLACKSTONE);

    private static DeferredBlock<GraveBlock> registerGrave(Grave graveType) {
        return BLOCKS.register(graveType.regName, () -> new GraveBlock(graveType));
    }
    public static final List<DeferredBlock<GraveBlock>> GRAVES = ImmutableList.of(
            GRAVESTONE,
            ANDESITE_GRAVESTONE,
            BASSALT_GRAVESTONE,
            CALCITE_GRAVESTONE,
            DARK_PRISMARINE_GRAVESTONE,
            DEEPSLATE_GRAVESTONE,
            DIORITE_GRAVESTONE,
            DRIPSTONE_GRAVESTONE,
            GRANITE_GRAVESTONE,
            STONE_GRAVESTONE,
            END_STONE_GRAVESTONE,
            BLACKSTONE_GRAVESTONE
    );
    public static final Supplier<BlockEntityType<GraveEntity>> GRAVE_ENTITY = BLOCK_ENTITIES.register("grave", () -> BlockEntityType.Builder.of(GraveEntity::new,
            GRAVESTONE.get(),
            ANDESITE_GRAVESTONE.get(),
            BASSALT_GRAVESTONE.get(),
            CALCITE_GRAVESTONE.get(),
            DARK_PRISMARINE_GRAVESTONE.get(),
            DEEPSLATE_GRAVESTONE.get(),
            DIORITE_GRAVESTONE.get(),
            DRIPSTONE_GRAVESTONE.get(),
            GRANITE_GRAVESTONE.get(),
            STONE_GRAVESTONE.get(),
            END_STONE_GRAVESTONE.get(),
            BLACKSTONE_GRAVESTONE.get()
            ).build(null));

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }
}
