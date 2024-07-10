package org.thesalutyt.storyverse.effekseer;
import org.thesalutyt.storyverse.effekseer.natives_config.InitializationConfigs;

import java.io.*;

public class Library {
    static {
        try {
            File file = getDllFileEffekseer();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();

                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                InputStream stream = Library.class.getClassLoader().getResourceAsStream(
                        "EffekseerNativeForJava" + (InitializationConfigs.os.equals("Win") ? ".dll" : ".so")
                );
                int b;
                while ((b = stream.read()) != -1) stream2.write(b);
                stream.close();
                byte[] bytes = stream2.toByteArray();

                FileOutputStream stream1 = new FileOutputStream(file);
                stream1.write(bytes);
                stream1.close();
                stream1.flush();
            }
        } catch (Throwable err) {
            if (err instanceof RuntimeException) throw (RuntimeException) err;
            throw new RuntimeException(err);
        }
    }

    public static File getDllFileEffekseer() {
        return new File(InitializationConfigs.binPath + "/effekseer/" + "EffekseerNativeForJava-effekseer4j-1" + (InitializationConfigs.os.equals("Win") ? ".dll" : ".so"));
    }

    public static File getDllFile() {
        return new File(
                InitializationConfigs.binPath + "/" +
                        InitializationConfigs.versionFile() + "/"
        );
    }

    protected static void init() {
    }
}