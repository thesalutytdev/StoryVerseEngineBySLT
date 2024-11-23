package org.thesalutyt.storyverse.api.addon;

public @interface Addon {
    String id();
    String name() default "Addon";
    String description() default "No description";
    String authors() default "None";
    String version() default "0.0.0";
    String addonVersion() default "0.0.0";
    String credits() default "None";

    class AddonInfo {
        public String id;
        public String name;
        public String description;
        public String authors;
        public String version;
        public String addonVersion;
        public String credits;

        public AddonInfo(String id, String name, String description, String authors, String version, String addonVersion, String credits) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.authors = authors;
            this.version = version;
            this.addonVersion = addonVersion;
            this.credits = credits;
        }

        // @Override
        // public String toString() {
        //     // return ImAddon.addonInfoPrinter();
        // }
    }
}
