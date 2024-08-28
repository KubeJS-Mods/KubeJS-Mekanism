package dev.latvian.mods.kubejs.mekanism;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import mekanism.api.MekanismAPI;

public class MekanismKubeJSPlugin implements KubeJSPlugin {
	@Override
	public void registerBuilderTypes(BuilderTypeRegistry registry) {
		registry.of(MekanismAPI.CHEMICAL_REGISTRY_NAME, reg -> {
			reg.addDefault(KubeChemicalBuilder.Default.class, KubeChemicalBuilder.Default::new);
			reg.add("liquid", KubeChemicalBuilder.Liquid.class, KubeChemicalBuilder.Liquid::new);
			reg.add("clean_slurry", KubeChemicalBuilder.CleanSlurry.class, KubeChemicalBuilder.CleanSlurry::new);
			reg.add("dirty_slurry", KubeChemicalBuilder.DirtySlurry.class, KubeChemicalBuilder.DirtySlurry::new);
			reg.add("infuse_type", KubeChemicalBuilder.InfuseType.class, KubeChemicalBuilder.InfuseType::new);
			reg.add("pigment", KubeChemicalBuilder.Pigment.class, KubeChemicalBuilder.Pigment::new);
		});
	}

	@Override
	public void registerRecipeComponents(RecipeComponentFactoryRegistry registry) {
	}

	@Override
	public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
		/*
		registry.namespace(MekanismAPI.MEKANISM_MODID)
			.register("crushing", ItemToItemRecipeSchema.SCHEMA)
			.register("enriching", ItemToItemRecipeSchema.SCHEMA)
			.register("smelting", ItemToItemRecipeSchema.SCHEMA)
			.register("chemical_infusing", ChemicalInfusingRecipeSchema.SCHEMA)
			.register("combining", CombiningRecipeSchema.SCHEMA)
			// separating = new ElectrolysisRecipeSerializer(ElectrolysisIRecipe.SCHEMA)
			// washing = new FluidSlurryToSlurryRecipeSerializer(FluidSlurryToSlurryIRecipe.SCHEMA)
			// evaporating = new FluidToFluidRecipeSerializer(FluidToFluidIRecipe.SCHEMA)
			// activating = new GasToGasRecipeSerializer(ActivatingIRecipe.SCHEMA)
			// centrifuging = new GasToGasRecipeSerializer(CentrifugingIRecipe.SCHEMA)
			.register("crystallizing", CrystallizingRecipeSchema.SCHEMA)
			.register("dissolution", ChemicalDissolutionRecipeSchema.SCHEMA)
			.register("compressing", ItemAndGasToItemRecipeSchema.SCHEMA)
			.register("purifying", ItemAndGasToItemRecipeSchema.SCHEMA)
			.register("injecting", ItemAndGasToItemRecipeSchema.SCHEMA)
			// nucleosynthesizing = new NucleosynthesizingRecipeSerializer(NucleosynthesizingIRecipe.SCHEMA)
			.register("energy_conversion", EnergyConversionRecipeSchema.SCHEMA)
			.register("gas_conversion", GasConversionRecipeSchema.SCHEMA)
			.register("oxidizing", OxidizingRecipeSchema.SCHEMA)
			// infusion_conversion = new ItemStackToInfuseTypeRecipeSerializer(InfusionConversionIRecipe.SCHEMA)
			.register("metallurgic_infusing", MetallurgicInfusingRecipeSchema.SCHEMA)
			.register("reaction", PressurizedReactionRecipeSchema.SCHEMA)
			// rotary = new RotaryRecipeSerializer(new RotaryIRecipe.Factory())
			.register("sawing", SawingRecipeSchema.SCHEMA)
		;
		 */
	}
}
