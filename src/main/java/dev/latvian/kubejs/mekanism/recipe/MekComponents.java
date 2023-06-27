package dev.latvian.kubejs.mekanism.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentWithParent;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.mod.util.JsonUtils;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;

import javax.annotation.Nullable;

public interface MekComponents {
	private static <C extends Chemical<C>, S extends ChemicalStack<C>, I extends ChemicalStackIngredient<C, S>> I parseChemicalIngredient(@Nullable Object o, String jsonKey, int defaultAmount, IChemicalStackIngredientCreator<C, S, I> creator) {
		// TODO: make a proper wrapper
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

	RecipeComponent<ChemicalStackIngredient.GasStackIngredient> GAS_INPUT = new RecipeComponent<>() {
		@Override
		public ComponentRole role() {
			return ComponentRole.INPUT;
		}

		@Override
		public Class<?> componentClass() {
			return ChemicalStackIngredient.GasStackIngredient.class;
		}

		@Override
		public JsonElement write(RecipeJS recipe, ChemicalStackIngredient.GasStackIngredient value) {
			return value.serialize();
		}

		@Override
		public ChemicalStackIngredient.GasStackIngredient read(RecipeJS recipe, Object from) {
			if (from instanceof ChemicalStackIngredient.GasStackIngredient i) {
				return i;
			} else {
				return parseChemicalIngredient(from, JsonConstants.GAS, 1000, IngredientCreatorAccess.gas());
			}
		}
	};

	RecipeComponent<ChemicalStackIngredient.InfusionStackIngredient> INFUSION_INPUT = new RecipeComponent<>() {
		@Override
		public ComponentRole role() {
			return ComponentRole.INPUT;
		}

		@Override
		public Class<?> componentClass() {
			return ChemicalStackIngredient.InfusionStackIngredient.class;
		}

		@Override
		public JsonElement write(RecipeJS recipe, ChemicalStackIngredient.InfusionStackIngredient value) {
			return value.serialize();
		}

		@Override
		public ChemicalStackIngredient.InfusionStackIngredient read(RecipeJS recipe, Object from) {
			if (from instanceof ChemicalStackIngredient.InfusionStackIngredient i) {
				return i;
			} else {
				return parseChemicalIngredient(from, JsonConstants.INFUSE_TYPE, 10, IngredientCreatorAccess.infusion());
			}
		}
	};

	RecipeComponent<GasStack> GAS_OUTPUT = new RecipeComponent<>() {
		@Override
		public Class<?> componentClass() {
			return GasStack.class;
		}

		@Override
		public JsonElement write(RecipeJS recipe, GasStack value) {
			return SerializerHelper.serializeGasStack(value);
		}

		@Override
		public GasStack read(RecipeJS recipe, Object from) {
			if (from instanceof GasStack s) {
				return s;
			} else if (from instanceof JsonObject json) {
				return SerializerHelper.deserializeGas(json);
			} else if (from instanceof CharSequence) {
				JsonObject json = new JsonObject();
				json.addProperty("gas", from.toString());
				json.addProperty("amount", 1000);
				return SerializerHelper.deserializeGas(json);
			} else {
				JsonObject json = MapJS.json(from);

				if (!json.has("amount")) {
					json.addProperty("amount", 1000);
				}

				return SerializerHelper.deserializeGas(json);
			}
		}
	};

	RecipeComponent<OutputItem> SECONDARY_OUTPUT_COMPONENT = new RecipeComponentWithParent<>() {
		@Override
		public RecipeComponent<OutputItem> parentComponent() {
			return ItemComponents.OUTPUT;
		}

		@Override
		public void writeToJson(RecipeComponentValue<OutputItem> value, JsonObject json) {
			ItemComponents.OUTPUT.writeToJson(value, json);

			if (value.value.hasChance()) {
				json.addProperty(JsonConstants.SECONDARY_CHANCE, value.value.getChance());
			}
		}

		@Override
		public OutputItem readFromJson(RecipeJS recipe, RecipeKey<OutputItem> key, JsonObject json) {
			var item = ItemComponents.OUTPUT.readFromJson(recipe, key, json);

			if (item != null && json.has(JsonConstants.SECONDARY_CHANCE)) {
				item = item.withChance(json.get(JsonConstants.SECONDARY_CHANCE).getAsDouble());
			}

			return item;
		}
	};
}
