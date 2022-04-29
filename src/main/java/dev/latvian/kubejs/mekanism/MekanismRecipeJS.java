package dev.latvian.kubejs.mekanism;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.MapJS;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.common.recipe.ingredient.chemical.ChemicalIngredientDeserializer;

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

	public static ChemicalStackIngredient.GasStackIngredient parseGasIngrdient(@Nullable Object o) {
		if (o instanceof JsonElement) {
			return ChemicalIngredientDeserializer.GAS.deserialize((JsonElement) o);
		} else if (o instanceof CharSequence) {
			JsonObject json = new JsonObject();
			json.addProperty("gas", o.toString());
			json.addProperty("amount", 1000);
			return ChemicalIngredientDeserializer.GAS.deserialize(json);
		}

		JsonObject json = MapJS.of(o).toJson();

		if (!json.has("amount")) {
			json.addProperty("amount", 1000);
		}

		return ChemicalIngredientDeserializer.GAS.deserialize(json);
	}

	public static GasStack parseGasResult(@Nullable Object o) {
		if (o instanceof JsonObject) {
			return SerializerHelper.deserializeGas((JsonObject) o);
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
		json.add("output", MapJS.of(o).toJson());
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
