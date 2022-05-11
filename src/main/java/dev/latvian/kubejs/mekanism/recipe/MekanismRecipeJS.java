package dev.latvian.kubejs.mekanism.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.mod.util.JsonUtils;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public abstract class MekanismRecipeJS extends RecipeJS {
	@Override
	public JsonElement serializeIngredientStack(IngredientStackJS in) {
		JsonObject json = new JsonObject();
		json.add("ingredient", in.ingredient.toJson());

		if (in.getCount() > 1) {
			json.addProperty("amount", in.getCount());
		}

		return json;
	}

	public static ChemicalStackIngredient.GasStackIngredient parseGasIngredient(@Nullable Object o) {
		return parseChemicalIngredient(o, JsonConstants.GAS, 1000, IngredientCreatorAccess.gas());
	}

	public static ChemicalStackIngredient.InfusionStackIngredient parseInfusionIngredient(@Nullable Object o) {
		return parseChemicalIngredient(o, JsonConstants.INFUSE_TYPE, 10, IngredientCreatorAccess.infusion());
	}

	private static <
			C extends Chemical<C>,
			S extends ChemicalStack<C>,
			I extends ChemicalStackIngredient<C, S>
			> I parseChemicalIngredient(@Nullable Object o, String jsonKey, int defaultAmount, IChemicalStackIngredientCreator<C, S, I> creator) {
		// TODO: make a proper wrapper (whenever i feel like it lol)
		return creator.deserialize(wrapChemical(JsonUtils.of(o), jsonKey, defaultAmount));
	}

	private static JsonElement wrapChemical(JsonElement json, String jsonKey, int defaultAmount) {
		if (json.isJsonPrimitive()) {
			var string = json.getAsString().trim();
			var amount = defaultAmount;

			int spaceIndex = string.indexOf(' ');
			if (spaceIndex >= 2 && string.indexOf('x') == spaceIndex - 1) {
				amount = Integer.parseInt(string.substring(0, spaceIndex - 1));
				string = string.substring(spaceIndex + 1);
			}

			var output = new JsonObject();

			if (string.startsWith("#")) {
				output.addProperty("tag", string.substring(1));
			} else {
				output.addProperty(jsonKey, string);
			}

			output.addProperty("amount", amount);
			return output;
		} else if (json.isJsonObject()) {
			var output = json.getAsJsonObject();
			if (!output.has("amount")) {
				output.addProperty("amount", defaultAmount);
			}
			return output;
		} else if (json.isJsonArray()) {
			var output = new JsonArray();
			for (var element : json.getAsJsonArray()) {
				output.add(wrapChemical(element, jsonKey, defaultAmount));
			}
			return output;
		} else {
			throw new IllegalArgumentException("Invalid chemical ingredient: " + json);
		}
	}

	public static GasStack parseGasResult(@Nullable Object o) {
		if (o instanceof JsonObject json) {
			return SerializerHelper.deserializeGas(json);
		} else if (o instanceof CharSequence) {
			JsonObject json = new JsonObject();
			json.addProperty("gas", o.toString());
			json.addProperty("amount", 1000);
			return SerializerHelper.deserializeGas(json);
		}

		JsonObject json = MapJS.of(o).toJson();

		if (!json.has("amount")) {
			json.addProperty("amount", 1000);
		}

		return SerializerHelper.deserializeGas(json);
	}

	public static JsonElement parseChemicalStack(@Nullable Object o) {
		JsonObject json = new JsonObject();
		json.add("output", MapJS.json(o));
		return SerializerHelper.serializeBoxedChemicalStack(BoxedChemicalStack.box(SerializerHelper.getBoxedChemicalStack(json, "output")));
	}

	public static JsonObject serializeGasResult(GasStack stack) {
		return SerializerHelper.serializeGasStack(stack);
	}

	@Override
	public IngredientJS convertReplacedInput(int index, IngredientJS oldIngredient, IngredientJS newIngredient) {
		return newIngredient.asIngredientStack();
	}
}
