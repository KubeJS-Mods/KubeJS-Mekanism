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
		event.register("mekanism:crushing", ItemToItemRecipeJS::new);
		event.register("mekanism:enriching", ItemToItemRecipeJS::new);
		event.register("mekanism:smelting", ItemToItemRecipeJS::new);
		event.register("mekanism:chemical_infusing", ChemicalInfusingRecipeJS::new);
		event.register("mekanism:combining", CombiningRecipeJS::new);
		// separating = new ElectrolysisRecipeSerializer(ElectrolysisIRecipe::new);
		// washing = new FluidSlurryToSlurryRecipeSerializer(FluidSlurryToSlurryIRecipe::new);
		// evaporating = new FluidToFluidRecipeSerializer(FluidToFluidIRecipe::new);
		// activating = new GasToGasRecipeSerializer(ActivatingIRecipe::new);
		// centrifuging = new GasToGasRecipeSerializer(CentrifugingIRecipe::new);
		event.register("mekanism:crystallizing", CrystallizingRecipeJS::new);
		// dissolution = new ChemicalDissolutionRecipeSerializer(ChemicalDissolutionIRecipe::new);
		event.register("mekanism:compressing", ItemAndGasToItemRecipeJS::new);
		event.register("mekanism:purifying", ItemAndGasToItemRecipeJS::new);
		event.register("mekanism:injecting", ItemAndGasToItemRecipeJS::new);
		// nucleosynthesizing = new NucleosynthesizingRecipeSerializer(NucleosynthesizingIRecipe::new);
		event.register("mekanism:energy_conversion", EnergyConversionRecipeJS::new);
		event.register("mekanism:gas_conversion", GasConversionRecipeJS::new);
		event.register("mekanism:oxidizing", OxidizingRecipeJS::new);
		// infusion_conversion = new ItemStackToInfuseTypeRecipeSerializer(InfusionConversionIRecipe::new);
		event.register("mekanism:metallurgic_infusing", MetallurgicInfusingRecipeJS::new);
		event.register("mekanism:reaction", PressurizedReactionRecipeJS::new);
		// rotary = new RotaryRecipeSerializer(new RotaryIRecipe.Factory());
		event.register("mekanism:sawing", SawingRecipeJS::new);
	}
}