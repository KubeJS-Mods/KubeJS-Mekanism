package dev.latvian.kubejs.mekanism;

import com.google.gson.JsonArray;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class MekanismItemToItemRecipeJS extends MekanismRecipeJS
{
	public final String inputName;
	public final String outputName;

	public MekanismItemToItemRecipeJS(String in, String out)
	{
		inputName = in;
		outputName = out;
	}

	public MekanismItemToItemRecipeJS()
	{
		this("input", "output");
	}

	@Override
	public void create(ListJS args)
	{
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.addAll(parseIngredientItemStackList(args.get(1)));
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get(outputName)));
		inputItems.addAll(parseIngredientItemStackList(json.get(inputName)));
	}

	@Override
	public void serialize()
	{
		if (serializeInputs)
		{
			if (inputItems.size() == 1)
			{
				json.add(inputName, inputItems.get(0).toJson());
			}
			else
			{
				JsonArray inputArray = new JsonArray();

				for (IngredientJS i : inputItems)
				{
					inputArray.add(i.toJson());
				}

				json.add(inputName, inputArray);
			}
		}

		if (serializeOutputs)
		{
			json.add(outputName, outputItems.get(0).toResultJson());
		}
	}
}