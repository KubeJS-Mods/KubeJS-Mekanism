package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.util.ListJS;
import mekanism.api.recipes.inputs.chemical.GasStackIngredient;

/**
 * @author LatvianModder
 */
public class MekanismItemAndGasToItemRecipeJS extends MekanismRecipeJS
{
	public GasStackIngredient inputGas;

	@Override
	public void create(ListJS args)
	{
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());
		inputGas = parseGas(args.get(2));
	}

	public MekanismItemAndGasToItemRecipeJS inputGas(Object o)
	{
		inputGas = parseGas(o);
		serializeInputs = true;
		save();
		return this;
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("output")));
		inputItems.add(parseIngredientItem(json.get("itemInput")));
		inputGas = parseGas(json.get("gasInput"));
	}

	@Override
	public void serialize()
	{
		if (serializeInputs)
		{
			json.add("itemInput", inputItems.get(0).toJson());
			json.add("gasInput", inputGas.serialize());
		}

		if (serializeOutputs)
		{
			json.add("output", outputItems.get(0).toResultJson());
		}
	}
}