package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.recipe.IngredientMatch;
import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import mekanism.api.JsonConstants;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * @author LatvianModder
 */
public class ItemAndChemicalToItemRecipeJS extends MekanismRecipeJS {
	public ItemStack output;
	public ItemStackIngredient itemInput;
	public ChemicalStackIngredient<?, ?> chemicalInput;

	@Override
	public void create(RecipeArguments args) {
		output = parseItemOutput(args.get(0));
		itemInput = mekStackIngredient(args.get(1));
		chemicalInput = parseChemicalIngredient(args.get(2));
	}

	public ItemAndChemicalToItemRecipeJS chemicalInput(Object o) {
		chemicalInput = parseChemicalIngredient(o);
		serializeInputs = true;
		save();
		return this;
	}

	@Override
	public void deserialize() {
		output = parseItemOutput(json.get(JsonConstants.OUTPUT));
		itemInput = mekStackIngredient(json.get(JsonConstants.ITEM_INPUT));
		chemicalInput = parseChemicalIngredient(json.get(JsonConstants.CHEMICAL_INPUT));
	}

	@Override
	public void serialize() {
		if (serializeInputs) {
			json.add(JsonConstants.ITEM_INPUT, itemInput.serialize());
			json.add(JsonConstants.CHEMICAL_INPUT, chemicalInput.serialize());
		}

		if (serializeOutputs) {
			json.add(JsonConstants.OUTPUT, itemToJson(output));
		}
	}

	@Override
	public boolean hasInput(IngredientMatch match) {
		return mekMatchStackIngredient(itemInput, match);
	}

	@Override
	public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
		var newIn = mekReplaceStackIngredient(itemInput, match, with, transformer);

		if (itemInput != newIn) {
			itemInput = newIn;
			return true;
		}

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
