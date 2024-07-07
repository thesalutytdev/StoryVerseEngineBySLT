package org.thesalutyt.storyverse.api.addon;

import org.thesalutyt.storyverse.SVEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class ImAddon {
    public String name;
    public String description;
    public String modId;
    public String authors;
    public String version;
    public String addonVersion;
    public String credits;
    public static HashMap<String, ImAddon> addons = new HashMap<>();
    public static ArrayList<ImAddon> addonsList = new ArrayList<>();

    public ImAddon(String name, String description, String modId) {
        this.name = name;
        this.description = description;
        this.modId = modId;
        addons.put(modId, this);
        addonsList.add(this);
    }

    public ImAddon(String name, String description, String modId,
                   String authors, String version,
                   String addonVersion, String credits) {
        this.name = name;
        this.description = description;
        this.modId = modId;
        this.authors = authors;
        this.version = version;
        this.addonVersion = addonVersion;
        this.credits = credits;
        addons.put(modId, this);
        addonsList.add(this);
    }

    public static String addonInfoPrinter(ImAddon addon) {
        return String.format(SVEngine.BORDER + "\n%s (%S):" +
                        "\n | Description: %s" +
                        "\n | Authors: %s" +
                        "\n | MC Version: %s" +
                        "\n | Addon Version: %s" +
                        "\n | Credits: %s\n" + SVEngine.BORDER,
                addon.name, addon.modId, addon.description, addon.authors, addon.version, addon.addonVersion, addon.credits);
    }
}
