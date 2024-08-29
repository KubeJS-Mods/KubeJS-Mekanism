package dev.latvian.mods.kubejs.mekanism;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactoryRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.DataComponentTypeInfoRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import dev.latvian.mods.rhino.type.TypeInfo;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.common.registration.MekanismDeferredHolder;
import mekanism.common.registries.MekanismDataComponents;
import net.minecraft.core.component.DataComponentType;

import java.lang.reflect.Modifier;

public class MekanismKubeJSPlugin implements KubeJSPlugin {
	@Override
	public void registerBuilderTypes(BuilderTypeRegistry registry) {
		registry.of(MekanismAPI.CHEMICAL_REGISTRY_NAME, reg -> {
			reg.addDefault(KubeChemicalBuilder.Default.class, KubeChemicalBuilder.Default::new);
			reg.add("liquid", KubeChemicalBuilder.Liquid.class, KubeChemicalBuilder.Liquid::new);
			reg.add("clean_slurry", KubeChemicalBuilder.CleanSlurry.class, KubeChemicalBuilder.CleanSlurry::new);
			reg.add("dirty_slurry", KubeChemicalBuilder.DirtySlurry.class, KubeChemicalBuilder.DirtySlurry::new);
			reg.add("infuse_type", KubeChemicalBuilder.InfuseType.class, KubeChemicalBuilder.InfuseType::new);
			reg.add("pigment", KubeChemicalBuilder.Pigment.class, KubeChemicalBuilder.Pigment::new);
		});
	}

	@Override
	public void registerBindings(BindingRegistry bindings) {
		bindings.add("MekanismChemical", MekanismChemicalWrapper.class);
	}

	@Override
	public void registerTypeWrappers(TypeWrapperRegistry registry) {
		registry.register(Chemical.class, MekanismChemicalWrapper::of);
		registry.register(ChemicalStack.class, MekanismChemicalWrapper::wrapStack);
		registry.register(ChemicalIngredient.class, MekanismChemicalWrapper::wrapIngredient);
		registry.register(ChemicalStackIngredient.class, MekanismChemicalWrapper::wrapStackIngredient);
	}

	@Override
	public void registerRecipeComponents(RecipeComponentFactoryRegistry registry) {
		registry.register(ChemicalRecipeComponents.CHEMICAL);
		registry.register(ChemicalRecipeComponents.CHEMICAL_STACK);
		registry.register(ChemicalRecipeComponents.CHEMICAL_INGREDIENT);
		registry.register(ChemicalRecipeComponents.CHEMICAL_STACK_INGREDIENT);
	}

	@Override
	public void registerDataComponentTypeDescriptions(DataComponentTypeInfoRegistry registry) {
		try {
			var dctt = TypeInfo.of(DataComponentType.class);

			for (var field : MekanismDataComponents.class.getDeclaredFields()) {
				if (field.getType() == MekanismDeferredHolder.class
					&& Modifier.isPublic(field.getModifiers())
					&& Modifier.isStatic(field.getModifiers())
				) {
					var t = TypeInfo.of(field.getGenericType()).param(1);

					if (t.is(dctt)) {
						var t2 = t.param(0);
						var d = (DataComponentType<?>) (((MekanismDeferredHolder) field.get(null)).get());
						registry.register(d, t2);
					}
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
}
