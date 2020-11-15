package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author LatvianModder
 */
@Mod(KubeJSMekanism.MOD_ID)
@Mod.EventBusSubscriber(modid = KubeJSMekanism.MOD_ID)
public class KubeJSMekanism
{
	public static final String MOD_ID = "kubejs_mekanism";

	@SubscribeEvent
	public static void registerRecipeHandlers(RegisterRecipeHandlersEvent event)
	{
		event.register("mekanism:crushing", MekanismMachineRecipeJS::new);
		event.register("mekanism:enriching", MekanismMachineRecipeJS::new);
		event.register("mekanism:purifying", () -> new MekanismMachineRecipeJS("itemInput", "output"));
		event.register("mekanism:injecting", () -> new MekanismMachineRecipeJS("itemInput", "output"));
		event.register("mekanism:metallurgic_infusing", MekanismMetallurgicInfusingRecipeJS::new);
		event.register("mekanism:sawing", () -> new MekanismMachineRecipeJS("input", "mainOutput"));
		event.register("mekanism:combining", MekanismCombiningRecipeJS::new);
	}
}