package com.flanks255.simplygraves;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class SGBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimplyGraves.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SimplyGraves.MODID);

    public static final RegistryObject<GraveBlock> GRAVESTONE = registerGrave(Grave.WOOD);
    public static final RegistryObject<GraveBlock> ANDESITE_GRAVESTONE = registerGrave(Grave.ANDESITE);
    public static final RegistryObject<GraveBlock> BASSALT_GRAVESTONE = registerGrave(Grave.BASSALT);
    public static final RegistryObject<GraveBlock> CALCITE_GRAVESTONE = registerGrave(Grave.CALCITE);
    public static final RegistryObject<GraveBlock> DARK_PRISMARINE_GRAVESTONE = registerGrave(Grave.DARK_PRISMARINE);
    public static final RegistryObject<GraveBlock> DEEPSLATE_GRAVESTONE = registerGrave(Grave.DEEPSLATE);
    public static final RegistryObject<GraveBlock> DIORITE_GRAVESTONE = registerGrave(Grave.DIORITE);
    public static final RegistryObject<GraveBlock> DRIPSTONE_GRAVESTONE = registerGrave(Grave.DRIPSTONE);
    public static final RegistryObject<GraveBlock> GRANITE_GRAVESTONE = registerGrave(Grave.GRANITE);
    public static final RegistryObject<GraveBlock> STONE_GRAVESTONE = registerGrave(Grave.STONE);
    public static final RegistryObject<GraveBlock> END_STONE_GRAVESTONE = registerGrave(Grave.END_STONE);
    public static final RegistryObject<GraveBlock> BLACKSTONE_GRAVESTONE = registerGrave(Grave.BLACKSTONE);

    private static RegistryObject<GraveBlock> registerGrave(Grave graveType) {
        return BLOCKS.register(graveType.regName, () -> new GraveBlock(graveType));
    }
    public static final List<RegistryObject<GraveBlock>> GRAVES = ImmutableList.of(
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
    public static final RegistryObject<BlockEntityType<GraveEntity>> GRAVE_ENTITY = BLOCK_ENTITIES.register("grave", () -> BlockEntityType.Builder.of(GraveEntity::new,
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
