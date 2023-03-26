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
public class ChemicalDissolutionRecipeJS extends MekanismRecipeJS {
	public ItemStackIngredient itemInput;

	@Override
	public void create(RecipeArguments args) {
		json.add(JsonConstants.OUTPUT, parseChemicalStack(args.get(0)));
		json.add(JsonConstants.CHEMICAL_INPUT, parseGasIngredient(args.get(1)).serialize());
		itemInput = mekStackIngredient(args.get(2));
	}

	public ChemicalDissolutionRecipeJS chemicalInput(Object o) {
		json.add(JsonConstants.CHEMICAL_INPUT, parseGasIngredient(o).serialize());
		serializeInputs = true;
		save();
		return this;
	}

	public ChemicalDissolutionRecipeJS chemicalOutput(Object o) {
		json.add(JsonConstants.OUTPUT, parseChemicalStack(o));
		save();
		return this;
	}

	@Override
	public void deserialize() {
		itemInput = mekStackIngredient(json.get(JsonConstants.ITEM_INPUT));
	}

	@Override
	public void serialize() {
		if (serializeInputs) {
			json.add(JsonConstants.ITEM_INPUT, itemInput.serialize());
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
		return false;
	}

	@Override
	public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
		return false;
	}
}
