package org.frc3620.timeclock.app;

import java.awt.Image;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wegscd
 */
public class AppSettingsAndResources {

    static Logger logger = LoggerFactory.getLogger(AppSettingsAndResources.class);
    Map<Integer, Image> icons = new TreeMap<>();

    static AppSettingsAndResources instance = null;

    public static AppSettingsAndResources getInstance() {
        if (instance == null) {
            synchronized (AppSettingsAndResources.class) {
                if (instance == null) {
                    instance = new AppSettingsAndResources();
                }
            }
        }
        return instance;
    }

    public AppSettingsAndResources() {
        icons.put(16, getIcon(16));
        icons.put(24, getIcon(24));
        icons.put(32, getIcon(32));
        icons.put(64, getIcon(64));
        icons.put(128, getIcon(128));
    }

    public List<Image> getIcons() {
        return new ArrayList<Image>(icons.values());
    }

    final Image getIcon(int size) {
        String name = String.format("icons/FIRSTicon_%d.png", size);
        try {
            return ImageIO.read(ClassLoader.getSystemResource(name));
        } catch (IOException ex) {
            logger.error("unable to load icon {}: {}", name, ex);
        }
        return null;
    }

}
