package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class MekanismCombiningRecipeJS extends MekanismRecipeJS
{
	@Override
	public void create(ListJS args)
	{
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());
		inputItems.add(parseIngredientItem(args.get(2)).asIngredientStack());
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("output")));
		inputItems.add(parseIngredientItem(json.get("mainInput")).asIngredientStack());
		inputItems.add(parseIngredientItem(json.get("extraInput")).asIngredientStack());
	}

	@Override
	public void serialize()
	{
		if (serializeOutputs)
		{
			json.add("output", outputItems.get(0).toResultJson());
		}

		if (serializeInputs)
		{
			json.add("mainInput", inputItems.get(0).toJson());
			json.add("extraInput", inputItems.get(1).toJson());
		}
	}
}