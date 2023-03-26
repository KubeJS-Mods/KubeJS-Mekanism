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
public class MetallurgicInfusingRecipeJS extends MekanismRecipeJS {
	public ItemStack output;
	public ItemStackIngredient input;
	private ChemicalStackIngredient.InfusionStackIngredient chemicalInput;

	@Override
	public void create(RecipeArguments args) {
		output = parseItemOutput(args.get(0));
		input = mekStackIngredient(args.get(1));

		if (args.size() >= 3) {
			chemicalInput = parseInfusionIngredient(args.get(2));
		}
	}

	public MetallurgicInfusingRecipeJS infuseType(Object o) {
		chemicalInput = parseInfusionIngredient(o);
		return this;
	}

	@Override
	public void deserialize() {
		output = parseItemOutput(json.get(JsonConstants.OUTPUT));
		input = mekStackIngredient(json.get(JsonConstants.ITEM_INPUT));

		if (json.has(JsonConstants.CHEMICAL_INPUT)) {
			chemicalInput = parseInfusionIngredient(json.get(JsonConstants.CHEMICAL_INPUT));
		}
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			json.add(JsonConstants.OUTPUT, itemToJson(output));
		}

		if (serializeInputs) {
			json.add(JsonConstants.ITEM_INPUT, input.serialize());

			if (chemicalInput == null) {
				json.add(JsonConstants.CHEMICAL_INPUT, parseInfusionIngredient("10x mekanism:redstone").serialize());
			} else {
				json.add(JsonConstants.CHEMICAL_INPUT, chemicalInput.serialize());
			}
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