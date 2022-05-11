package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.util.ListJS;
import mekanism.api.JsonConstants;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;

/**
 * @author LatvianModder
 */
public class MetallurgicInfusingRecipeJS extends MekanismRecipeJS {
	private ChemicalStackIngredient.InfusionStackIngredient chemicalInput = parseInfusionIngredient("10x mekanism:redstone");

	@Override
	public void create(ListJS args) {
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());
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
		outputItems.add(parseResultItem(json.get(JsonConstants.OUTPUT)));
		inputItems.add(parseIngredientItem(json.get(JsonConstants.ITEM_INPUT)).asIngredientStack());

		if (json.has(JsonConstants.CHEMICAL_INPUT)) {
			chemicalInput = parseInfusionIngredient(json.get(JsonConstants.CHEMICAL_INPUT));
		}
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			json.add(JsonConstants.OUTPUT, outputItems.get(0).toResultJson());
		}

		if (serializeInputs) {
			json.add(JsonConstants.ITEM_INPUT, inputItems.get(0).toJson());
			json.add(JsonConstants.CHEMICAL_INPUT, chemicalInput.serialize());
		}
	}
}
