package net.zharok01.coralinesystems;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.zharok01.coralinesystems.client.PhotoModeScreen;
import net.zharok01.coralinesystems.client.PhotoModeUtils;
import org.slf4j.Logger;

import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CoralineSystems.MOD_ID)
public class CoralineSystems {
    public static final String MOD_ID = "coralinesystems";
    public static final Logger LOGGER = LogUtils.getLogger();

	private static final List<String> usePauseScreenWorkarround = List.of(
		"NostalgicPauseScreen"
	);

    public CoralineSystems() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
		PhotoModeUtils.init();
	}

	@SubscribeEvent
	public void screenEventHandler(ScreenEvent.Init.Pre event) {
		Screen screen = event.getScreen();
		if (screen instanceof PauseScreen || usePauseScreenWorkarround.contains(screen.getClass().getSimpleName())) {
			screen.addRenderableWidget(Button.builder(Component.translatable("gui.photomode"), (button) -> {
				Minecraft.getInstance().setScreen(new PhotoModeScreen(Component.literal("")));
			}).pos(screen.width / 2 - 48, 8).width(98).build());
		}
	}

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}