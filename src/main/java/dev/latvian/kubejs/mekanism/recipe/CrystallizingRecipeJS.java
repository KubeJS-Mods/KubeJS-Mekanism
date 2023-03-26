package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.recipe.IngredientMatch;
import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import mekanism.api.JsonConstants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * @author LatvianModder
 */
public class CrystallizingRecipeJS extends MekanismRecipeJS {
	public ItemStack output;

	@Override
	public void create(RecipeArguments args) {
		throw new RecipeExceptionJS("Creation not supported yet!");
	}

	@Override
	public void deserialize() {
		output = parseItemOutput(json.get(JsonConstants.OUTPUT));
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			json.add(JsonConstants.OUTPUT, itemToJson(output));
		}
	}

	@Override
	public boolean hasInput(IngredientMatch match) {
		return false;
	}

	@Override
	public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
		return false;
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
