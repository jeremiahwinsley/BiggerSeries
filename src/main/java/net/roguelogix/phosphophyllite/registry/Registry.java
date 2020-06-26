package net.roguelogix.phosphophyllite.registry;

import com.google.common.collect.ImmutableSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.roguelogix.phosphophyllite.config.PhosphophylliteConfig;
import net.roguelogix.phosphophyllite.multiblock.generic.MultiblockBakedModel;
import org.reflections.Reflections;

public class Registry {

    private static final HashMap<String, HashSet<Block>> blocksRegistered = new HashMap<>();
    private static final HashMap<Class<?>, HashSet<Block>> tileEntityBlocksToRegister = new HashMap<>();

    public static synchronized void registerBlocks(
            final RegistryEvent.Register<Block> blockRegistryEvent) {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        Reflections ref = new Reflections(callerPackage);
        Set<Class<?>> blocks = ref.getTypesAnnotatedWith(RegisterBlock.class);
        HashSet<Block> blocksRegistered = Registry.blocksRegistered
                .computeIfAbsent(modNamespace, k -> new HashSet<>());
        for (Class<?> block : blocks) {
            try {
                Constructor<?> constructor = block.getConstructor();
                constructor.setAccessible(true);
                Object newObject = constructor.newInstance();
                if (!(newObject instanceof Block)) {
                    // the fuck you doing
                    //todo print error
                    continue;
                }

                Block newBlock = (Block) newObject;
                RegisterBlock blockAnnotation = block.getAnnotation(RegisterBlock.class);
                newBlock.setRegistryName(modNamespace + ":" + blockAnnotation.name());
                blockRegistryEvent.getRegistry().register(newBlock);

                for (Field declaredField : block.getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(RegisterBlock.Instance.class)) {
                        declaredField.set(null, newBlock);
                    }
                }

                if (blockAnnotation.registerItem()) {
                    blocksRegistered.add(newBlock);
                }

                if (blockAnnotation.tileEntityClass() != RegisterBlock.class) {
                    tileEntityBlocksToRegister
                            .computeIfAbsent(blockAnnotation.tileEntityClass(), k -> new HashSet<>())
                            .add(newBlock);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


    public static synchronized void registerItems(
            final RegistryEvent.Register<Item> itemRegistryEvent) {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        Reflections ref = new Reflections(callerPackage);
        Set<Class<?>> items = ref.getTypesAnnotatedWith(RegisterItem.class);
        HashSet<Block> blocksRegistered = Registry.blocksRegistered.get(modNamespace);

        Set<Class<?>> modClass = ref.getTypesAnnotatedWith(Mod.class);

        String groupName = modNamespace;
        if(modClass.size() == 1){
            groupName = modClass.iterator().next().getName();
        }

        ItemGroup group;
        {
            Block block;
            Set<Class<?>> createiveTabBlock = ref.getTypesAnnotatedWith(CreativeTabBlock.class);
            Iterator<Class<?>> iterator = createiveTabBlock.iterator();
            Block b1 = Blocks.STONE;
            if (iterator.hasNext()) {
                Class<?> blockClass = iterator.next();
                for (Field declaredField : blockClass.getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(RegisterBlock.Instance.class)) {
                        try {
                            b1 = (Block) declaredField.get(null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            block = b1;
            group = new ItemGroup(groupName) {
                @Override
                public ItemStack createIcon() {
                    return new ItemStack(ForgeRegistries.ITEMS.getValue(block.getRegistryName()));
                }
            };
        }

        for (Block block : blocksRegistered) {
            assert block.getClass().isAnnotationPresent(RegisterBlock.class);
//            RegisterBlock blockAnnotation = block.getClass().getAnnotation(RegisterBlock.class);
            //noinspection ConstantConditions
            itemRegistryEvent.getRegistry().register(
                    new BlockItem(block, new Item.Properties().group(group)).setRegistryName(block.getRegistryName()));
        }

        for (Class<?> item : items) {
            try {
                Constructor<?> constructor = item.getConstructor(Item.Properties.class);
                constructor.setAccessible(true);
                Object newObject = constructor.newInstance(new Item.Properties().group(group));
                if (!(newObject instanceof Item)) {
                    // the fuck you doing
                    //todo print error
                    continue;
                }

                Item newItem = (Item) newObject;
                RegisterItem itemAnnotation = item.getAnnotation(RegisterItem.class);
                newItem.setRegistryName(modNamespace + ":" + itemAnnotation.name());
                itemRegistryEvent.getRegistry().register(newItem);

                for (Field declaredField : item.getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(RegisterItem.Instance.class)) {
                        declaredField.set(null, newItem);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void registerContainers(RegistryEvent.Register<ContainerType<?>> containerTypeRegistryEvent) {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        Reflections ref = new Reflections(callerPackage);
        Set<Class<?>> containers = ref.getTypesAnnotatedWith(RegisterContainer.class);

        for (Class<?> container : containers) {
            try {
                Constructor<?> constructor = container.getConstructor(int.class, BlockPos.class, PlayerEntity.class);
                constructor.setAccessible(true);
                Object newObject = constructor.newInstance(/* Need Paremeters Here, Cannot be Null */);
                if (!(newObject instanceof Container)) {
                    // interesting...
                    //todo print error
                    continue;
                }

                Container containerInstance = (Container) newObject;
                RegisterContainer containerAnnotation = container.getAnnotation(RegisterContainer.class);
                ContainerType<?> containerType = IForgeContainerType.create(((windowId, inv, data) -> containerInstance));
                containerType.setRegistryName(modNamespace + ":" + containerAnnotation.name());
                containerTypeRegistryEvent.getRegistry().register(containerType);

                for (Field declaredField : container.getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(RegisterContainer.Instance.class)) {
                        declaredField.set(null, containerType);
                    }
                }
            } catch (NullPointerException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void registerTileEntities(
            RegistryEvent.Register<TileEntityType<?>> tileEntityTypeRegistryEvent) {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        Reflections ref = new Reflections(callerPackage);
        Set<Class<?>> tileEntities = ref.getTypesAnnotatedWith(RegisterTileEntity.class);

//        tileEntityTypeRegistryEvent.getRegistry().register();

        for (Class<?> tileEntity : tileEntities) {

            RegisterTileEntity tileEntityAnnotation = tileEntity.getAnnotation(RegisterTileEntity.class);

            HashSet<Block> blocks = tileEntityBlocksToRegister
                    .computeIfAbsent(tileEntity, k -> new HashSet<>());
            if (blocks.isEmpty()) {
                continue;
            }
            try {
                Constructor<?> tileConstructor = tileEntity.getConstructor();
                tileConstructor.setAccessible(true);
                Supplier<? extends TileEntity> supplier = () -> {
                    try {
                        return (TileEntity) tileConstructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                };
                @SuppressWarnings("rawtypes") Constructor<TileEntityType.Builder> constructor = TileEntityType.Builder.class
                        .getDeclaredConstructor(Supplier.class, Set.class);
                constructor.setAccessible(true);
                @SuppressWarnings({"unchecked",
                        "ConstantConditions"}) TileEntityType<?> tileEntityType = constructor
                        .newInstance(supplier, ImmutableSet.copyOf(blocks)).build(null);
                tileEntityType.setRegistryName(modNamespace + ":" + tileEntityAnnotation.name());
                tileEntityTypeRegistryEvent.getRegistry().register(tileEntityType);
                for (Field field : tileEntity.getFields()) {
                    if (field.isAnnotationPresent(RegisterTileEntity.Type.class)) {
                        field.set(null, tileEntityType);
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    public static synchronized void onClientSetup(FMLClientSetupEvent clientSetupEvent) {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        HashSet<Block> blocksRegistered = Registry.blocksRegistered.get(modNamespace);
        for (Block block : blocksRegistered) {
            if (!block.getDefaultState().isSolid()) {
                for (Method declaredMethod : block.getClass().getDeclaredMethods()) {
                    if (declaredMethod.isAnnotationPresent(RegisterBlock.RenderLayer.class)) {
                        try {
                            declaredMethod.setAccessible(true);
                            Object returned = declaredMethod.invoke(block);
                            if (returned instanceof RenderType) {
                                RenderTypeLookup.setRenderLayer(block, (RenderType) returned);
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static final HashMap<Block, IBakedModel> bakedModelsToRegister = new HashMap<>();

    public static synchronized void registerBakedModel(Block block, IBakedModel model) {
        bakedModelsToRegister.put(block, model);
    }

    public static synchronized void onModelBake(ModelBakeEvent event) {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        HashSet<Block> blocksRegistered = Registry.blocksRegistered.get(modNamespace);
        if (blocksRegistered == null) {
            return;
        }
        for (Block block : blocksRegistered) {
            IBakedModel model = bakedModelsToRegister.get(block);
            if (model != null) {
                event.getModelRegistry().put(new ModelResourceLocation(block.getRegistryName(), ""), model);
            }
        }
    }

    public static synchronized void onTextureStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            return;
        }
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        HashSet<Block> blocksRegistered = Registry.blocksRegistered.get(modNamespace);
        if (blocksRegistered == null) {
            return;
        }
        HashSet<ResourceLocation> sprites = new HashSet<>();
        for (Block block : blocksRegistered) {
            IBakedModel model = bakedModelsToRegister.get(block);
            if (model instanceof MultiblockBakedModel) {
                Stack<MultiblockBakedModel.TextureMap> mapStack = new Stack<>();
                mapStack.push(((MultiblockBakedModel) model).map);
                while (!mapStack.empty()) {
                    MultiblockBakedModel.TextureMap map = mapStack.pop();
                    sprites.add(map.spriteLocation);
                    for (MultiblockBakedModel.TextureMap value : map.map.values()) {
                        mapStack.push(value);
                    }
                }
            }
        }
        for (ResourceLocation sprite : sprites) {
            event.addSprite(sprite);
        }
    }

    public static void registerConfig() {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        Reflections ref = new Reflections(callerPackage);
        Set<Class<?>> configs = ref.getTypesAnnotatedWith(RegisterConfig.class);
        for (Class<?> config : configs) {
//            ConfigLoader.registerConfig(config);
        }
    }

    public static synchronized void registerWorldGen() {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        Reflections ref = new Reflections(callerPackage);
        Set<Class<?>> ores = ref.getTypesAnnotatedWith(RegisterOre.class);
        HashSet<Block> blocksRegistered = Registry.blocksRegistered
                .computeIfAbsent(modNamespace, k -> new HashSet<>());

        for (Class<?> ore : ores) {
            try {
                RegisterOre oreAnnotation = ore.getAnnotation(RegisterOre.class);
                RegisterBlock blockAnnotation = ore.getAnnotation(RegisterBlock.class);

                Block oreInstance = null;
                for (Block block : blocksRegistered) {
                    if (Objects.requireNonNull(block.getRegistryName())
                            .toString().equals(modNamespace + ":" + blockAnnotation.name())) {
                        oreInstance = block;
                    }
                }

                for (Biome biome : ForgeRegistries.BIOMES) {
                    if (oreAnnotation.spawnBiomes().length > 0) {
                        if (!Arrays.asList(oreAnnotation.spawnBiomes()).contains(
                                Objects.requireNonNull(biome.getRegistryName()).toString())) {
                            continue;
                        }
                    }

                    assert oreInstance != null;
                    FillerBlockType fillerBlock = oreAnnotation.isNetherOre() ? FillerBlockType.NETHERRACK : FillerBlockType.NATURAL_STONE;
                    biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
                            new OreFeatureConfig(fillerBlock, oreInstance.getDefaultState(), oreAnnotation.size()))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(oreAnnotation.count(), oreAnnotation.minLevel(), oreAnnotation.offset(), oreAnnotation.maxLevel()))));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadConfig() {
        String callerClass = new Exception().getStackTrace()[1].getClassName();
        String callerPackage = callerClass.substring(0, callerClass.lastIndexOf("."));
        String modNamespace = callerPackage.substring(callerPackage.lastIndexOf(".") + 1);
        Reflections ref = new Reflections(callerPackage);
        Set<Class<?>> configs = ref.getTypesAnnotatedWith(RegisterConfig.class);
        for (Class<?> config : configs) {
            for (Method declaredMethod : config.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(PhosphophylliteConfig.OnLoad.class)) {
                    try {
                        declaredMethod.invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
