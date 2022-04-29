package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.util.ListJS;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;

/**
 * @author LatvianModder
 */
public class ChemicalInfusingRecipeJS extends MekanismRecipeJS {
	public GasStack gasOutput;
	public ChemicalStackIngredient.GasStackIngredient leftGasInput;
	public ChemicalStackIngredient.GasStackIngredient rightGasInput;

	@Override
	public void create(ListJS args) {
		gasOutput = parseGasResult(args.get(0));
		leftGasInput = parseGasIngredient(args.get(1));
		rightGasInput = parseGasIngredient(args.get(2));
	}

	@Override
	public void deserialize() {
		gasOutput = parseGasResult(json.get("output"));
		leftGasInput = parseGasIngredient(json.get("leftInput"));
		rightGasInput = parseGasIngredient(json.get("rightInput"));
	}

	public ChemicalInfusingRecipeJS outputGas(Object o) {
		gasOutput = parseGasResult(o);
		serializeOutputs = true;
		save();
		return this;
	}

	public ChemicalInfusingRecipeJS leftInputGas(Object o) {
		leftGasInput = parseGasIngredient(o);
		serializeInputs = true;
		save();
		return this;
	}

	public ChemicalInfusingRecipeJS rightInputGas(Object o) {
		rightGasInput = parseGasIngredient(o);
		serializeInputs = true;
		save();
		return this;
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			json.add("output", serializeGasResult(gasOutput));
		}

		if (serializeInputs) {
			json.add("leftInput", leftGasInput.serialize());
			json.add("rightInput", rightGasInput.serialize());
		}
	}
}
