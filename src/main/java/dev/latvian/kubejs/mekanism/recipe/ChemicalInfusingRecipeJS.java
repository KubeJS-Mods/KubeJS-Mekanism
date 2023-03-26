package dev.latvian.kubejs.mekanism.recipe;

import dev.latvian.mods.kubejs.recipe.IngredientMatch;
import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * @author LatvianModder
 */
public class ChemicalInfusingRecipeJS extends MekanismRecipeJS {
	public GasStack gasOutput;
	public ChemicalStackIngredient.GasStackIngredient leftGasInput;
	public ChemicalStackIngredient.GasStackIngredient rightGasInput;

	@Override
	public void create(RecipeArguments args) {
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
		return false;
	}

	@Override
	public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
		return false;
	}
}
