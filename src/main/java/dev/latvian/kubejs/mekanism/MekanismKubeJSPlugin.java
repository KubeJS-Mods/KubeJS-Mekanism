package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.mekanism.custom.KubeJSGasBuilder;
import dev.latvian.kubejs.mekanism.custom.KubeJSInfuseTypeBuilder;
import dev.latvian.kubejs.mekanism.custom.KubeJSPigmentBuilder;
import dev.latvian.kubejs.mekanism.custom.KubeJSSlurryBuilder;
import dev.latvian.kubejs.mekanism.recipe.ChemicalDissolutionRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.ChemicalInfusingRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.CombiningRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.CrystallizingRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.EnergyConversionRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.GasConversionRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.ItemAndGasToItemRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.ItemToItemRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.MetallurgicInfusingRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.OxidizingRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.PressurizedReactionRecipeJS;
import dev.latvian.kubejs.mekanism.recipe.SawingRecipeJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeTypesEvent;
import dev.latvian.mods.kubejs.util.UtilsJS;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.slurry.Slurry;
import net.minecraft.resources.ResourceLocation;

public class MekanismKubeJSPlugin extends KubeJSPlugin {
	public static ResourceLocation mekId(String path) {
		return new ResourceLocation(MekanismAPI.MEKANISM_MODID, path);
	}

	// registry builders for all mekanism chemical subtypes
	public static final RegistryObjectBuilderTypes<Gas> GAS = RegistryObjectBuilderTypes.add(UtilsJS.cast(MekanismAPI.gasRegistryName()), Gas.class);
	public static final RegistryObjectBuilderTypes<InfuseType> INFUSE_TYPE = RegistryObjectBuilderTypes.add(UtilsJS.cast(MekanismAPI.infuseTypeRegistryName()), Slurry.class);
	public static final RegistryObjectBuilderTypes<Pigment> PIGMENT = RegistryObjectBuilderTypes.add(UtilsJS.cast(MekanismAPI.pigmentRegistryName()), Pigment.class);
	public static final RegistryObjectBuilderTypes<Slurry> SLURRY = RegistryObjectBuilderTypes.add(UtilsJS.cast(MekanismAPI.slurryRegistryName()), Slurry.class);

	@Override
	public void init() {
		GAS.addType("basic", KubeJSGasBuilder.class, KubeJSGasBuilder::new);
		INFUSE_TYPE.addType("basic", KubeJSInfuseTypeBuilder.class, KubeJSInfuseTypeBuilder::new);
		PIGMENT.addType("basic", KubeJSPigmentBuilder.class, KubeJSPigmentBuilder::new);
		SLURRY.addType("basic", KubeJSSlurryBuilder.Basic.class, KubeJSSlurryBuilder.Basic::new);
		SLURRY.addType("dirty", KubeJSSlurryBuilder.Dirty.class, KubeJSSlurryBuilder.Dirty::new);
		SLURRY.addType("clean", KubeJSSlurryBuilder.Clean.class, KubeJSSlurryBuilder.Clean::new);
	}

	@Override
	public void registerRecipeTypes(RegisterRecipeTypesEvent event) {
		event.register(mekId("crushing"), ItemToItemRecipeJS::new);
		event.register(mekId("enriching"), ItemToItemRecipeJS::new);
		event.register(mekId("smelting"), ItemToItemRecipeJS::new);
		event.register(mekId("chemical_infusing"), ChemicalInfusingRecipeJS::new);
		event.register(mekId("combining"), CombiningRecipeJS::new);
		// separating = new ElectrolysisRecipeSerializer(ElectrolysisIRecipe::new);
		// washing = new FluidSlurryToSlurryRecipeSerializer(FluidSlurryToSlurryIRecipe::new);
		// evaporating = new FluidToFluidRecipeSerializer(FluidToFluidIRecipe::new);
		// activating = new GasToGasRecipeSerializer(ActivatingIRecipe::new);
		// centrifuging = new GasToGasRecipeSerializer(CentrifugingIRecipe::new);
		event.register(mekId("crystallizing"), CrystallizingRecipeJS::new);
		event.register(mekId("dissolution"), ChemicalDissolutionRecipeJS::new);
		event.register(mekId("compressing"), ItemAndGasToItemRecipeJS::new);
		event.register(mekId("purifying"), ItemAndGasToItemRecipeJS::new);
		event.register(mekId("injecting"), ItemAndGasToItemRecipeJS::new);
		// nucleosynthesizing = new NucleosynthesizingRecipeSerializer(NucleosynthesizingIRecipe::new);
		event.register(mekId("energy_conversion"), EnergyConversionRecipeJS::new);
		event.register(mekId("gas_conversion"), GasConversionRecipeJS::new);
		event.register(mekId("oxidizing"), OxidizingRecipeJS::new);
		// infusion_conversion = new ItemStackToInfuseTypeRecipeSerializer(InfusionConversionIRecipe::new);
		event.register(mekId("metallurgic_infusing"), MetallurgicInfusingRecipeJS::new);
		event.register(mekId("reaction"), PressurizedReactionRecipeJS::new);
		// rotary = new RotaryRecipeSerializer(new RotaryIRecipe.Factory());
		event.register(mekId("sawing"), SawingRecipeJS::new);
	}
}
