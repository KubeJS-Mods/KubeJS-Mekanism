package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.recipe.IngredientMatch;
import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import mekanism.api.JsonConstants;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * @author LatvianModder
 */
public class OxidizingRecipeJS extends MekanismRecipeJS {
	public ItemStackIngredient input;

	@Override
	public void create(RecipeArguments args) {
		throw new RecipeExceptionJS("Creation not supported yet!");
	}

	@Override
	public void deserialize() {
		input = mekStackIngredient(json.get(JsonConstants.INPUT));
	}

	@Override
	public void serialize() {
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
		return false;
	}

	@Override
	public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
		return false;
	}
}
