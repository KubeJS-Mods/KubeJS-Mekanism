package dev.latvian.kubejs.mekanism;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.kubejs.util.MapJS;
import mekanism.api.recipes.inputs.chemical.GasStackIngredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

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
		event.register("mekanism:crushing", MekanismItemToItemRecipeJS::new);
		event.register("mekanism:enriching", MekanismItemToItemRecipeJS::new);
		event.register("mekanism:smelting", MekanismItemToItemRecipeJS::new);
		// chemical_infusing = new ChemicalInfuserRecipeSerializer(ChemicalInfuserIRecipe::new);
		event.register("mekanism:combining", MekanismCombiningRecipeJS::new);
		// separating = new ElectrolysisRecipeSerializer(ElectrolysisIRecipe::new);
		// washing = new FluidSlurryToSlurryRecipeSerializer(FluidSlurryToSlurryIRecipe::new);
		// evaporating = new FluidToFluidRecipeSerializer(FluidToFluidIRecipe::new);
		// activating = new GasToGasRecipeSerializer(ActivatingIRecipe::new);
		// centrifuging = new GasToGasRecipeSerializer(CentrifugingIRecipe::new);
		// crystallizing = new ChemicalCrystallizerRecipeSerializer(ChemicalCrystallizerIRecipe::new);
		// dissolution = new ChemicalDissolutionRecipeSerializer(ChemicalDissolutionIRecipe::new);
		event.register("mekanism:compressing", MekanismItemAndGasToItemRecipeJS::new);
		event.register("mekanism:purifying", MekanismItemAndGasToItemRecipeJS::new);
		event.register("mekanism:injecting", MekanismItemAndGasToItemRecipeJS::new);
		// nucleosynthesizing = new NucleosynthesizingRecipeSerializer(NucleosynthesizingIRecipe::new);
		// energy_conversion = new ItemStackToEnergyRecipeSerializer(EnergyConversionIRecipe::new);
		// gas_conversion = new ItemStackToGasRecipeSerializer(GasConversionIRecipe::new);
		// oxidizing = new ItemStackToGasRecipeSerializer(ChemicalOxidizerIRecipe::new);
		// infusion_conversion = new ItemStackToInfuseTypeRecipeSerializer(InfusionConversionIRecipe::new);
		event.register("mekanism:metallurgic_infusing", MekanismMetallurgicInfusingRecipeJS::new);
		// reaction = new PressurizedReactionRecipeSerializer(PressurizedReactionIRecipe::new);
		// rotary = new RotaryRecipeSerializer(new RotaryIRecipe.Factory());
		event.register("mekanism:sawing", MekanismSawingRecipeJS::new);
	}

	public static GasStackIngredient parseGas(@Nullable Object o)
	{
		if (o instanceof JsonElement)
		{
			return GasStackIngredient.deserialize((JsonElement) o);
		}
		else if (o instanceof CharSequence)
		{
			JsonObject json = new JsonObject();
			json.addProperty("gas", o.toString());
			json.addProperty("amount", 1000);
			return GasStackIngredient.deserialize(json);
		}

		JsonObject json = MapJS.of(o).toJson();

		if (!json.has("amount"))
		{
			json.addProperty("amount", 1000);
		}

		return GasStackIngredient.deserialize(json);
	}
}