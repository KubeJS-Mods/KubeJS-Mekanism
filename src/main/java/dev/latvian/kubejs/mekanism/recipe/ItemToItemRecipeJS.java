package dev.latvian.kubejs.mekanism.recipe;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.recipe.IngredientMatch;
import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import mekanism.api.JsonConstants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemToItemRecipeJS extends MekanismRecipeJS {
	public ItemStack output;
	public List<Ingredient> input;

	@Override
	public void create(RecipeArguments args) {
		output = parseItemOutput(args.get(0));
		input = parseItemInputList(args.get(1));
	}

	@Override
	public void deserialize() {
		output = parseItemOutput(json.get(JsonConstants.OUTPUT));
		input = parseItemInputList(json.get(JsonConstants.INPUT));
	}

	@Override
	public void serialize() {
		if (serializeInputs) {
			if (input.size() == 1) {
				json.add(JsonConstants.INPUT, input.get(0).toJson());
			} else {
				var inputArray = new JsonArray();

				for (var i : input) {
					inputArray.add(i.toJson());
				}

				json.add(JsonConstants.INPUT, inputArray);
			}
		}

		if (serializeOutputs) {
			json.add(JsonConstants.OUTPUT, itemToJson(output));
		}
	}

	@Override
	public boolean hasInput(IngredientMatch match) {
		for (var in : input) {
			if (match.contains(in)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
		boolean changed = false;

		for (int i = 0; i < input.size(); i++) {
			if (match.contains(input.get(i))) {
				input.set(i, transformer.transform(this, match, input.get(i), with));
				changed = true;
			}
		}

		return changed;
	}

	@Override
	public boolean hasOutput(IngredientMatch match) {
		return match.contains(output);
	}

	@Override
	public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
		if (match.contains(output)) {
			output = transformer.transform(this, match, output, with);
			return true;
		}

		return false;
	}
}
