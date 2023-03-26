package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.recipe.IngredientMatch;
import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import mekanism.api.JsonConstants;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * @author LatvianModder
 */
public class CombiningRecipeJS extends MekanismRecipeJS {
	public ItemStack output;
	public ItemStackIngredient mainInput;
	public ItemStackIngredient extraInput;

	@Override
	public void create(RecipeArguments args) {
		output = parseItemOutput(args.get(0));
		mainInput = mekStackIngredient(args.get(1));
		extraInput = mekStackIngredient(args.get(2));
	}

	@Override
	public void deserialize() {
		output = parseItemOutput(json.get(JsonConstants.OUTPUT));
		mainInput = mekStackIngredient(json.get(JsonConstants.MAIN_INPUT));
		extraInput = mekStackIngredient(json.get(JsonConstants.EXTRA_INPUT));
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			json.add(JsonConstants.OUTPUT, itemToJson(output));
		}

		if (serializeInputs) {
			json.add(JsonConstants.MAIN_INPUT, mainInput.serialize());
			json.add(JsonConstants.EXTRA_INPUT, extraInput.serialize());
		}
	}

	@Override
	public boolean hasInput(IngredientMatch match) {
		return mekMatchStackIngredient(mainInput, match) || mekMatchStackIngredient(extraInput, match);
	}

	@Override
	public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
		boolean changed = false;

		var newMainIn = mekReplaceStackIngredient(mainInput, match, with, transformer);

		if (mainInput != newMainIn) {
			mainInput = newMainIn;
			changed = true;
		}

		var newExtraIn = mekReplaceStackIngredient(extraInput, match, with, transformer);

		if (extraInput != newExtraIn) {
			extraInput = newExtraIn;
			changed = true;
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
