package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.item.ItemStackJS;
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
public class SawingRecipeJS extends MekanismRecipeJS { // SawmillRecipe
	private ItemStackIngredient input;
	private ItemStack mainOutput = ItemStack.EMPTY;
	private ItemStack secondaryOutput = ItemStack.EMPTY;

	@Override
	public void create(RecipeArguments args) {
		mainOutput = ItemStackJS.of(args.get(0)); // allow empty
		input = mekStackIngredient(args.get(1));

		if (args.size() >= 3) {
			secondaryOutput = parseItemOutput(args.get(2));
		}
	}

	@Override
	public void deserialize() {
		input = mekStackIngredient(json.get(JsonConstants.INPUT));

		if (json.has(JsonConstants.MAIN_OUTPUT)) {
			mainOutput = parseItemOutput(json.get(JsonConstants.MAIN_OUTPUT));
		}

		if (json.has(JsonConstants.SECONDARY_OUTPUT)) {
			secondaryOutput = parseItemOutput(json.get(JsonConstants.SECONDARY_OUTPUT));

			if (json.has(JsonConstants.SECONDARY_CHANCE)) {
				secondaryOutput = secondaryOutput.kjs$withChance(json.get(JsonConstants.SECONDARY_CHANCE).getAsDouble());
			}
		}
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			if (!mainOutput.isEmpty()) {
				json.add(JsonConstants.MAIN_OUTPUT, itemToJson(mainOutput));
			}

			if (!secondaryOutput.isEmpty()) {
				var chance = secondaryOutput.kjs$getChance();

				if (Double.isNaN(chance)) {
					json.addProperty(JsonConstants.SECONDARY_CHANCE, chance);
					json.add(JsonConstants.SECONDARY_OUTPUT, itemToJson(secondaryOutput.kjs$withChance(Double.NaN)));
				} else {
					json.addProperty(JsonConstants.SECONDARY_CHANCE, 1.0D);
					json.add(JsonConstants.SECONDARY_OUTPUT, itemToJson(secondaryOutput));
				}
			}
		}

		if (serializeInputs) {
			json.add(JsonConstants.INPUT, input.serialize());
		}
	}

	@Override
	public boolean hasInput(IngredientMatch match) {
		return mekMatchStackIngredient(input, match);
	}

	@Override
	public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
		var newIn = mekReplaceStackIngredient(input, match, with, transformer);

		if (input != newIn) {
			input = newIn;
			return true;
		}

		return false;
	}

	@Override
	public boolean hasOutput(IngredientMatch match) {
		return !mainOutput.isEmpty() && match.contains(mainOutput) || !secondaryOutput.isEmpty() && match.contains(secondaryOutput);
	}

	@Override
	public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
		boolean changed = false;

		if (!mainOutput.isEmpty() && match.contains(mainOutput)) {
			mainOutput = transformer.transform(this, match, mainOutput, with);
			changed = true;
		}

		if (!secondaryOutput.isEmpty() && match.contains(secondaryOutput)) {
			secondaryOutput = transformer.transform(this, match, secondaryOutput, with);
			changed = true;
		}

		return changed;
	}
}
