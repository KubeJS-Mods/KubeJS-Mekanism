package dev.latvian.mods.kubejs.mekanism;

import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.mekanism.recipe.component.ChemicalIngredientRecipeComponent;
import dev.latvian.mods.kubejs.mekanism.recipe.component.ChemicalRecipeComponent;
import dev.latvian.mods.kubejs.mekanism.recipe.component.ChemicalStackIngredientRecipeComponent;
import dev.latvian.mods.kubejs.mekanism.recipe.component.ChemicalStackRecipeComponent;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.RegistryObjectStorage;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.DataComponentTypeInfoRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import dev.latvian.mods.rhino.type.TypeInfo;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.datamaps.IMekanismDataMapTypes;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.common.Mekanism;
import mekanism.common.registration.MekanismDeferredHolder;
import mekanism.common.registries.MekanismDataComponents;
import net.minecraft.core.component.DataComponentType;

import java.lang.reflect.Modifier;

public class MekanismKubeJSPlugin implements KubeJSPlugin {
	public static final RegistryObjectStorage<Chemical> CHEMICAL_REGISTRY = RegistryObjectStorage.of(MekanismAPI.CHEMICAL_REGISTRY_NAME);

	@Override
	public void registerBuilderTypes(BuilderTypeRegistry registry) {
		registry.of(MekanismAPI.CHEMICAL_REGISTRY_NAME, reg -> {
			reg.addDefault(KubeChemicalBuilder.Default.class, KubeChemicalBuilder.Default::new);
			reg.add(Mekanism.rl("liquid"), KubeChemicalBuilder.Liquid.class, KubeChemicalBuilder.Liquid::new);
			reg.add(Mekanism.rl("clean_slurry"), KubeChemicalBuilder.CleanSlurry.class, KubeChemicalBuilder.CleanSlurry::new);
			reg.add(Mekanism.rl("dirty_slurry"), KubeChemicalBuilder.DirtySlurry.class, KubeChemicalBuilder.DirtySlurry::new);
			reg.add(Mekanism.rl("infuse_type"), KubeChemicalBuilder.InfuseType.class, KubeChemicalBuilder.InfuseType::new);
			reg.add(Mekanism.rl("pigment"), KubeChemicalBuilder.Pigment.class, KubeChemicalBuilder.Pigment::new);
		});
	}

	@Override
	public void registerBindings(BindingRegistry bindings) {
		bindings.add("MekanismChemical", MekanismChemicalWrapper.class);
	}

	@Override
	public void registerTypeWrappers(TypeWrapperRegistry registry) {
		// registry.register(Chemical.class, MekanismChemicalWrapper::of);
		registry.register(ChemicalStack.class, MekanismChemicalWrapper::stackOf);
		registry.register(ChemicalIngredient.class, MekanismChemicalWrapper::ingredientOf);
		registry.register(ChemicalStackIngredient.class, MekanismChemicalWrapper::stackIngredientOf);
	}

	@Override
	public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
		registry.register(ChemicalRecipeComponent.CHEMICAL);
		registry.register(ChemicalStackRecipeComponent.CHEMICAL_STACK);
		registry.register(ChemicalIngredientRecipeComponent.CHEMICAL_INGREDIENT);
		registry.register(ChemicalStackIngredientRecipeComponent.CHEMICAL_STACK_INGREDIENT);
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

	@Override
	public void generateData(KubeDataGenerator generator) {
		generator.dataMap(IMekanismDataMapTypes.INSTANCE.chemicalSolidTag(), callback -> {
			for (var builder : CHEMICAL_REGISTRY) {
				if (builder instanceof KubeChemicalBuilder b) {
					if (b.ore != null) {
						callback.accept(b.id, b.ore);
					}
				}
			}
		});

		generator.dataMap(IMekanismDataMapTypes.INSTANCE.chemicalFuel(), callback -> {
			for (var builder : CHEMICAL_REGISTRY) {
				if (builder instanceof KubeChemicalBuilder b) {
					if (b.fuel != null) {
						callback.accept(b.id, b.fuel);
					}
				}
			}
		});

		generator.dataMap(IMekanismDataMapTypes.INSTANCE.chemicalRadioactivity(), callback -> {
			for (var builder : CHEMICAL_REGISTRY) {
				if (builder instanceof KubeChemicalBuilder b) {
					if (b.radiation != null) {
						callback.accept(b.id, b.radiation);
					}
				}
			}
		});

		generator.dataMap(IMekanismDataMapTypes.INSTANCE.cooledChemicalCoolant(), callback -> {
			for (var builder : CHEMICAL_REGISTRY) {
				if (builder instanceof KubeChemicalBuilder b) {
					if (b.cooledCoolant != null) {
						callback.accept(b.id, b.cooledCoolant);
					}
				}
			}
		});

		generator.dataMap(IMekanismDataMapTypes.INSTANCE.heatedChemicalCoolant(), callback -> {
			for (var builder : CHEMICAL_REGISTRY) {
				if (builder instanceof KubeChemicalBuilder b) {
					if (b.heatedCoolant != null) {
						callback.accept(b.id, b.heatedCoolant);
					}
				}
			}
		});
	}
}
