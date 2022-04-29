package dev.latvian.kubejs.mekanism;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraft.resources.ResourceLocation;

public class MekanismKubeJSPlugin extends KubeJSPlugin {
	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.register(new ResourceLocation("mekanism", "crushing"), ItemToItemRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "enriching"), ItemToItemRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "smelting"), ItemToItemRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "chemical_infusing"), ChemicalInfusingRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "combining"), CombiningRecipeJS::new);
		// separating = new ElectrolysisRecipeSerializer(ElectrolysisIRecipe::new);
		// washing = new FluidSlurryToSlurryRecipeSerializer(FluidSlurryToSlurryIRecipe::new);
		// evaporating = new FluidToFluidRecipeSerializer(FluidToFluidIRecipe::new);
		// activating = new GasToGasRecipeSerializer(ActivatingIRecipe::new);
		// centrifuging = new GasToGasRecipeSerializer(CentrifugingIRecipe::new);
		event.register(new ResourceLocation("mekanism", "crystallizing"), CrystallizingRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "dissolution"), ChemicalDissolutionRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "compressing"), ItemAndGasToItemRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "purifying"), ItemAndGasToItemRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "injecting"), ItemAndGasToItemRecipeJS::new);
		// nucleosynthesizing = new NucleosynthesizingRecipeSerializer(NucleosynthesizingIRecipe::new);
		event.register(new ResourceLocation("mekanism", "energy_conversion"), EnergyConversionRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "gas_conversion"), GasConversionRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "oxidizing"), OxidizingRecipeJS::new);
		// infusion_conversion = new ItemStackToInfuseTypeRecipeSerializer(InfusionConversionIRecipe::new);
		event.register(new ResourceLocation("mekanism", "metallurgic_infusing"), MetallurgicInfusingRecipeJS::new);
		event.register(new ResourceLocation("mekanism", "reaction"), PressurizedReactionRecipeJS::new);
		// rotary = new RotaryRecipeSerializer(new RotaryIRecipe.Factory());
		event.register(new ResourceLocation("mekanism", "sawing"), SawingRecipeJS::new);
	}
}
