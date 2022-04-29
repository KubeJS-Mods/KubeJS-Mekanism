package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.util.ListJS;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;

/**
 * @author LatvianModder
 */
public class ItemAndGasToItemRecipeJS extends MekanismRecipeJS {
	public ChemicalStackIngredient.GasStackIngredient inputGas;

	@Override
	public void create(ListJS args) {
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());
		inputGas = parseGasIngredient(args.get(2));
	}

	public ItemAndGasToItemRecipeJS inputGas(Object o) {
		inputGas = parseGasIngredient(o);
		serializeInputs = true;
		save();
		return this;
	}

	@Override
	public void deserialize() {
		outputItems.add(parseResultItem(json.get("output")));
		inputItems.add(parseIngredientItem(json.get("itemInput")).asIngredientStack());
		inputGas = parseGasIngredient(json.get("gasInput"));
	}

	@Override
	public void serialize() {
		if (serializeInputs) {
			json.add("itemInput", inputItems.get(0).toJson());
			json.add("gasInput", inputGas.serialize());
		}

		if (serializeOutputs) {
			json.add("output", outputItems.get(0).toResultJson());
		}
	}
}
