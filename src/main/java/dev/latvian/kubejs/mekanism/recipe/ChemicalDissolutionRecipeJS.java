package dev.latvian.kubejs.mekanism.recipe;


import dev.latvian.mods.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class ChemicalDissolutionRecipeJS extends MekanismRecipeJS {
	@Override
	public void create(ListJS args) {
		json.add("output", parseChemicalStack(args.get(0)));
		json.add("gasInput", parseGasIngredient(args.get(1)).serialize());
		inputItems.add(parseIngredientItem(args.get(2)).asIngredientStack());
	}

	public ChemicalDissolutionRecipeJS inputGas(Object o) {
		json.add("gasInput", parseGasIngredient(o).serialize());
		serializeInputs = true;
		save();
		return this;
	}

	public ChemicalDissolutionRecipeJS outputChemical(Object o) {
		json.add("output", parseChemicalStack(o));
		save();
		return this;
	}

	@Override
	public void deserialize() {
		inputItems.add(parseIngredientItem(json.get("itemInput")).asIngredientStack());
	}

	@Override
	public void serialize() {
		if (serializeInputs) {
			json.add("itemInput", inputItems.get(0).toJson());
		}
	}
}
