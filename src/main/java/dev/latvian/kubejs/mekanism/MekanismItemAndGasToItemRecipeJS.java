package dev.latvian.kubejs.mekanism;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.item.ingredient.IngredientStackJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;
import mekanism.api.recipes.inputs.chemical.GasStackIngredient;

/**
 * @author LatvianModder
 */
public class MekanismItemAndGasToItemRecipeJS extends RecipeJS
{
	public GasStackIngredient inputGas;

	@Override
	public void create(ListJS args)
	{
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());
		inputGas = KubeJSMekanism.parseGas(args.get(2));
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("output")));
		inputItems.add(parseIngredientItem(json.get("itemInput")));
		inputGas = KubeJSMekanism.parseGas(json.get("gasInput"));
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

	@Override
	public JsonElement serializeIngredientStack(IngredientStackJS in)
	{
		JsonObject json = new JsonObject();
		json.add("ingredient", in.ingredient.toJson());

		if (in.getCount() > 1)
		{
			json.addProperty("amount", in.getCount());
		}

		return json;
	}
}