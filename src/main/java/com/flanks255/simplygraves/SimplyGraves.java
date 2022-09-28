package com.flanks255.simplygraves;

import com.flanks255.simplygraves.WSD.GraveStorage;
import com.flanks255.simplygraves.commands.SGCommands;
import com.flanks255.simplygraves.config.CommonConfig;
import com.flanks255.simplygraves.data.Generator;
import com.flanks255.simplygraves.render.EntityRenders;
import com.flanks255.simplygraves.render.GraveTextRender;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.ExplosionEvent;
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

        MinecraftForge.EVENT_BUS.addListener(DropEvent::Event);
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
