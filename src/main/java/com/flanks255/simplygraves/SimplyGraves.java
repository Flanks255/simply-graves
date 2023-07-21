package com.flanks255.simplygraves;

import com.flanks255.simplygraves.WSD.GraveStorage;
import com.flanks255.simplygraves.commands.SGCommands;
import com.flanks255.simplygraves.config.CommonConfig;
import com.flanks255.simplygraves.data.Generator;
import com.flanks255.simplygraves.render.EntityRenders;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;


@Mod(SimplyGraves.MODID)
public class SimplyGraves
{
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "simplygraves";

    public static final TagKey<Item> NO_GRAVE = TagKey.create(Registries.ITEM, new ResourceLocation("simplygraves", "no_grave"));

    public static final TagKey<Block> FTBCHUNKS = TagKey.create(Registries.BLOCK, new ResourceLocation("ftbchunks", "interact_whitelist"));
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimplyGraves.MODID);

    public SimplyGraves()
    {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        SGBlocks.init(bus);
        ITEMS.register(bus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_CONFIG);

        bus.addListener(this::setup);
        bus.addListener(Generator::gatherData);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(EntityRenders::registerEntityRenderers);
        }

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, DropEvent::Event);
        MinecraftForge.EVENT_BUS.addListener(this::commandsRegister);
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    private void commandsRegister(RegisterCommandsEvent event) {
        SGCommands.register(event.getDispatcher());
    }

    public static Set<String> getUUIDSuggestions() {
        GraveStorage graveStorage = GraveStorage.get();
        Set<String> list = new HashSet<>();

        graveStorage.getData().forEach((uuid, graveData) -> list.add(uuid.toString()));

        if (list.isEmpty())
            list.add("[No graves]");

        return list;
    }
}
