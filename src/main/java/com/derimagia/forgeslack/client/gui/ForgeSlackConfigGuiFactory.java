package com.derimagia.forgeslack.client.gui;

import com.derimagia.forgeslack.ForgeSlack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.gui.ForgeGuiFactory;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.FMLConfigGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.*;
import net.minecraftforge.fml.common.ModContainer;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

public class ForgeSlackConfigGuiFactory implements IModGuiFactory {
    public static class ForgeSlackConfigGuiScreen extends GuiConfig {
        public ForgeConfigGui(GuiScreen parentScreen)
        {
            super(parentScreen, getConfigElements(), ForgeVersion.MOD_ID, false, false, I18n.format("forge.configgui.forgeConfigTitle"));
        }

        private static List<IConfigElement> getConfigElements()
        {
            List<IConfigElement> list = new ArrayList<IConfigElement>();
            list.add(new DummyConfigElement.DummyCategoryElement("forgeCfg", "forge.configgui.ctgy.forgeGeneralConfig", ForgeGuiFactory.ForgeConfigGui.GeneralEntry.class));
            list.add(new DummyConfigElement.DummyCategoryElement("forgeClientCfg", "forge.configgui.ctgy.forgeClientConfig", ForgeGuiFactory.ForgeConfigGui.ClientEntry.class));
            list.add(new DummyConfigElement.DummyCategoryElement("forgeChunkLoadingCfg", "forge.configgui.ctgy.forgeChunkLoadingConfig", ForgeGuiFactory.ForgeConfigGui.ChunkLoaderEntry.class));
            list.add(new DummyConfigElement.DummyCategoryElement("forgeVersionCheckCfg", "forge.configgui.ctgy.VersionCheckConfig", ForgeGuiFactory.ForgeConfigGui.VersionCheckEntry.class));
            return list;
        }

        /**
         * This custom list entry provides the General Settings entry on the Minecraft Forge Configuration screen.
         * It extends the base Category entry class and defines the IConfigElement objects that will be used to build the child screen.
         */
        public static class GeneralEntry extends GuiConfigEntries.CategoryEntry
        {
            public GeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
            {
                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen()
            {
                // This GuiConfig object specifies the configID of the object and as such will force-save when it is closed. The parent
                // GuiConfig object's entryList will also be refreshed to reflect the changes.
                return new GuiConfig(this.owningScreen,
                        (new ConfigElement(ForgeModContainer.getConfig().getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
                        this.owningScreen.modID, Configuration.CATEGORY_GENERAL, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                        GuiConfig.getAbridgedConfigPath(ForgeModContainer.getConfig().toString()));
            }
        }

        /**
         * This custom list entry provides the Client only Settings entry on the Minecraft Forge Configuration screen.
         * It extends the base Category entry class and defines the IConfigElement objects that will be used to build the child screen.
         */
        public static class ClientEntry extends GuiConfigEntries.CategoryEntry
        {
            public ClientEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
            {
                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen()
            {
                // This GuiConfig object specifies the configID of the object and as such will force-save when it is closed. The parent
                // GuiConfig object's entryList will also be refreshed to reflect the changes.
                return new GuiConfig(this.owningScreen,
                        (new ConfigElement(ForgeModContainer.getConfig().getCategory(Configuration.CATEGORY_CLIENT))).getChildElements(),
                        this.owningScreen.modID, Configuration.CATEGORY_CLIENT, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                        GuiConfig.getAbridgedConfigPath(ForgeModContainer.getConfig().toString()));
            }
        }

        /**
         * This custom list entry provides the Forge Chunk Manager Config entry on the Minecraft Forge Configuration screen.
         * It extends the base Category entry class and defines the IConfigElement objects that will be used to build the child screen.
         */
        public static class ChunkLoaderEntry extends GuiConfigEntries.CategoryEntry
        {
            public ChunkLoaderEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
            {
                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen()
            {
                List<IConfigElement> list = new ArrayList<IConfigElement>();

                list.add(new DummyConfigElement.DummyCategoryElement("forgeChunkLoadingModCfg", "forge.configgui.ctgy.forgeChunkLoadingModConfig",
                        ForgeGuiFactory.ForgeConfigGui.ModOverridesEntry.class));
                list.addAll((new ConfigElement(ForgeChunkManager.getDefaultsCategory())).getChildElements());

                // This GuiConfig object specifies the configID of the object and as such will force-save when it is closed. The parent
                // GuiConfig object's propertyList will also be refreshed to reflect the changes.
                return new GuiConfig(this.owningScreen, list, this.owningScreen.modID, "chunkLoader",
                        this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                        GuiConfig.getAbridgedConfigPath(ForgeChunkManager.getConfig().toString()),
                        I18n.format("forge.configgui.ctgy.forgeChunkLoadingConfig"));
            }
        }

        public ForgeSlackConfigGuiScreen(GuiScreen parent)
        {
            super(parent, getConfigElements(), "Forgeslack", false, false, I18n.format("fml.config.sample.title"));
        }

        private static List<IConfigElement> getConfigElements()
        {
            List<IConfigElement> list = new ArrayList<IConfigElement>();
            List<IConfigElement> listsList = new ArrayList<IConfigElement>();
            List<IConfigElement> stringsList = new ArrayList<IConfigElement>();
            List<IConfigElement> numbersList = new ArrayList<IConfigElement>();
            Pattern commaDelimitedPattern = Pattern.compile("([A-Za-z]+((,){1}( )*|$))+?");

            // Top Level Settings
            list.add(new DummyConfigElement("imABoolean", true, ConfigGuiType.BOOLEAN, "fml.config.sample.imABoolean").setRequiresMcRestart(true));
            list.add(new DummyConfigElement("imAnInteger", 42, ConfigGuiType.INTEGER, "fml.config.sample.imAnInteger", -1, 256).setRequiresMcRestart(true));
            list.add(new DummyConfigElement("imADouble", 42.4242D, ConfigGuiType.DOUBLE, "fml.config.sample.imADouble", -1.0D, 256.256D).setRequiresMcRestart(true));
            list.add(new DummyConfigElement("imAString", "http://www.montypython.net/scripts/string.php", ConfigGuiType.STRING, "fml.config.sample.imAString").setRequiresMcRestart(true));

            // Lists category
            listsList.add(new DummyConfigElement.DummyListElement("booleanList", new Boolean[] {true, false, true, false, true, false, true, false}, ConfigGuiType.BOOLEAN, "fml.config.sample.booleanList"));
            listsList.add(new DummyConfigElement.DummyListElement("booleanListFixed", new Boolean[] {true, false, true, false, true, false, true, false}, ConfigGuiType.BOOLEAN, "fml.config.sample.booleanListFixed", true));
            listsList.add(new DummyConfigElement.DummyListElement("booleanListMax", new Boolean[] {true, false, true, false, true, false, true, false}, ConfigGuiType.BOOLEAN, "fml.config.sample.booleanListMax", 10));
            listsList.add(new DummyConfigElement.DummyListElement("doubleList", new Double[] {0.0D, 1.1D, 2.2D, 3.3D, 4.4D, 5.5D, 6.6D, 7.7D, 8.8D, 9.9D}, ConfigGuiType.DOUBLE, "fml.config.sample.doubleList"));
            listsList.add(new DummyConfigElement.DummyListElement("doubleListFixed", new Double[] {0.0D, 1.1D, 2.2D, 3.3D, 4.4D, 5.5D, 6.6D, 7.7D, 8.8D, 9.9D}, ConfigGuiType.DOUBLE, "fml.config.sample.doubleListFixed", true));
            listsList.add(new DummyConfigElement.DummyListElement("doubleListMax", new Double[] {0.0D, 1.1D, 2.2D, 3.3D, 4.4D, 5.5D, 6.6D, 7.7D, 8.8D, 9.9D}, ConfigGuiType.DOUBLE, "fml.config.sample.doubleListMax", 15));
            listsList.add(new DummyConfigElement.DummyListElement("doubleListBounded", new Double[] {0.0D, 1.1D, 2.2D, 3.3D, 4.4D, 5.5D, 6.6D, 7.7D, 8.8D, 9.9D}, ConfigGuiType.DOUBLE, "fml.config.sample.doubleListBounded", -1.0D, 10.0D));
            listsList.add(new DummyConfigElement.DummyListElement("integerList", new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, ConfigGuiType.INTEGER, "fml.config.sample.integerList"));
            listsList.add(new DummyConfigElement.DummyListElement("integerListFixed", new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, ConfigGuiType.INTEGER, "fml.config.sample.integerListFixed", true));
            listsList.add(new DummyConfigElement.DummyListElement("integerListMax", new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, ConfigGuiType.INTEGER, "fml.config.sample.integerListMax", 15));
            listsList.add(new DummyConfigElement.DummyListElement("integerListBounded", new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, ConfigGuiType.INTEGER, "fml.config.sample.integerListBounded", -1, 10));
            listsList.add(new DummyConfigElement.DummyListElement("stringList", new String[] {"An", "array", "of", "string", "values"}, ConfigGuiType.STRING, "fml.config.sample.stringList"));
            listsList.add(new DummyConfigElement.DummyListElement("stringListFixed", new String[] {"A", "fixed", "length", "array", "of", "string", "values"}, ConfigGuiType.STRING, "fml.config.sample.stringListFixed", true));
            listsList.add(new DummyConfigElement.DummyListElement("stringListMax", new String[] {"An", "array", "of", "string", "values", "with", "a", "max", "length", "of", "15"}, ConfigGuiType.STRING, "fml.config.sample.stringListMax", 15));
            listsList.add(new DummyConfigElement.DummyListElement("stringListPattern", new String[] {"Valid", "Not Valid", "Is, Valid", "Comma, Separated, Value"}, ConfigGuiType.STRING, "fml.config.sample.stringListPattern", commaDelimitedPattern));
            listsList.add(new DummyConfigElement.DummyListElement("stringListCustom", new Object[0], ConfigGuiType.STRING, "fml.config.sample.stringListCustom").setArrayEntryClass(FMLConfigGuiFactory.CustomArrayEntry.class));

            list.add(new DummyConfigElement.DummyCategoryElement("lists", "fml.config.sample.ctgy.lists", listsList));

            // Strings category
            stringsList.add(new DummyConfigElement("basicString", "Just a regular String value, anything goes.", ConfigGuiType.STRING, "fml.config.sample.basicString"));
            stringsList.add(new DummyConfigElement("cycleString", "this", ConfigGuiType.STRING, "fml.config.sample.cycleString", new String[] {"this", "property", "cycles", "through", "a", "list", "of", "valid", "choices"}));
            stringsList.add(new DummyConfigElement("patternString", "only, comma, separated, words, can, be, entered, in, this, box", ConfigGuiType.STRING, "fml.config.sample.patternString", commaDelimitedPattern));
            stringsList.add(new DummyConfigElement("chatColorPicker", "c", ConfigGuiType.COLOR, "fml.config.sample.chatColorPicker", new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"}));
            stringsList.add(new DummyConfigElement("modIDSelector", "FML", ConfigGuiType.MOD_ID, "fml.config.sample.modIDSelector"));

            list.add(new DummyConfigElement.DummyCategoryElement("strings", "fml.config.sample.ctgy.strings", stringsList));

            // Numbers category
            numbersList.add((new DummyConfigElement("basicInteger", 42, ConfigGuiType.INTEGER, "fml.config.sample.basicInteger")));
            numbersList.add((new DummyConfigElement("boundedInteger", 42, ConfigGuiType.INTEGER, "fml.config.sample.boundedInteger", -1, 256)));
            numbersList.add((new DummyConfigElement("sliderInteger", 2000, ConfigGuiType.INTEGER, "fml.config.sample.sliderInteger", 100, 10000)).setCustomListEntryClass(GuiConfigEntries.NumberSliderEntry.class));
            numbersList.add(new DummyConfigElement("basicDouble", 42.4242D, ConfigGuiType.DOUBLE, "fml.config.sample.basicDouble"));
            numbersList.add(new DummyConfigElement("boundedDouble", 42.4242D, ConfigGuiType.DOUBLE, "fml.config.sample.boundedDouble", -1.0D, 256.256D));
            numbersList.add(new DummyConfigElement("sliderDouble", 42.4242D, ConfigGuiType.DOUBLE, "fml.config.sample.sliderDouble", -1.0D, 256.256D).setCustomListEntryClass(GuiConfigEntries.NumberSliderEntry.class));

            list.add(new DummyConfigElement.DummyCategoryElement("numbers", "fml.config.sample.ctgy.numbers", numbersList));

            return list;
        }
    }

    @Override
    public void initialize(Minecraft minecraftInstance) {
        ForgeSlack.logger.info("TESTING!!");
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return new ForgeSlackConfigGuiScreen(parent);
    }

    private static final Set<RuntimeOptionCategoryElement> fmlCategories = ImmutableSet.of(new RuntimeOptionCategoryElement("HELP", "FORGESLACK"));

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return fmlCategories;
    }
}
