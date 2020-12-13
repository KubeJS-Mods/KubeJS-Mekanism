package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.util.ListJS;
import mekanism.api.recipes.inputs.chemical.GasStackIngredient;

/**
 * @author LatvianModder
 */
public class ItemAndGasToItemRecipeJS extends MekanismRecipeJS
{
	public GasStackIngredient inputGas;

	@Override
	public void create(ListJS args)
	{
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());
		inputGas = parseGasIngrdient(args.get(2));
	}

	public ItemAndGasToItemRecipeJS inputGas(Object o)
	{
		inputGas = parseGasIngrdient(o);
		serializeInputs = true;
		save();
		return this;
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("output")));
		inputItems.add(parseIngredientItem(json.get("itemInput")).asIngredientStack());
		inputGas = parseGasIngrdient(json.get("gasInput"));
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