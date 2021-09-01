package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;

public class MekanismKubeJSPlugin extends KubeJSPlugin {
	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
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
		event.register("mekanism:dissolution", ChemicalDissolutionRecipeJS::new);
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
