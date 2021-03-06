/*
 * Copyright 2004 - 2009 Christian Sprajc. All rights reserved.
 *
 * This file is part of PowerFolder.
 *
 * PowerFolder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * PowerFolder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PowerFolder. If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id: BlackMoonSkin.java 8103 2009-05-27 23:56:11Z tot $
 */
package de.dal33t.powerfolder.skin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JComponent;

import de.dal33t.powerfolder.util.os.OSUtil;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.IVersion;

/**
 * Base class for own skin with synthetica LAF
 *
 * @author sprajc
 */
public abstract class AbstractSyntheticaSkin implements Skin {
    
    private static final Logger log = Logger.getLogger(LookAndFeel.class.getName());

    public abstract Properties getIconsProperties();

    public abstract String getName();

    public abstract String getID();

    public abstract Path getDefaultSynthXMLPath();

    public abstract Path getSynthXMLPath();

    public final LookAndFeel getLookAndFeel() throws ParseException {
        return new LookAndFeel();
    }

    public class LookAndFeel extends SyntheticaLookAndFeel {
        private static final long serialVersionUID = 1L;
        
        public LookAndFeel() throws ParseException {
            super();
            // Always load the default XML file from the jar
            super.loadXMLConfig(AbstractSyntheticaSkin.this.getDefaultSynthXMLPath().toString().replace("\\", "/"));
            // If XML file exists in skin folder, load it
            if (AbstractSyntheticaSkin.this.getSynthXMLPath() !=null && Files.exists(AbstractSyntheticaSkin.this.getSynthXMLPath())) {
                try {
                    URL xmlURL;
                    if (OSUtil.isWindowsSystem()) {
                        xmlURL = new URL("file:///" + AbstractSyntheticaSkin.this.getSynthXMLPath());
                    } else {
                        xmlURL = new URL("file://" + AbstractSyntheticaSkin.this.getSynthXMLPath());
                    }
                    super.load(xmlURL);
                } catch (MalformedURLException e) {
                    log.severe("Cannot load XML file");
                }
                catch (IOException e) {
                    log.severe("Cannot load XML file");
                }
            }
        }

        @Override
        public String getID() {
            return AbstractSyntheticaSkin.this.getID();
        }

        @Override
        public String getName() {
            return AbstractSyntheticaSkin.this.getName();
        }

        public Icon getDisabledIcon(JComponent component, Icon icon) {
            // We don't do it.
            return null;

            // if (icon instanceof ImageIcon) {
            // ImageIcon i = (ImageIcon) icon;
            // GrayFilter filter = new GrayFilter(true, 100);
            // ImageProducer prod = new FilteredImageSource(i.getImage()
            // .getSource(), filter);
            // Image grayImage = Toolkit.getDefaultToolkit().createImage(prod);
            // return new ImageIconUIResource(grayImage);
            // }
            // return null;
        }

        @Override
        public IVersion getVersion() {
            final int major = 1;
            final int minor = 0;
            final int revision = 0;
            final int build = 1;

            return new IVersion() {
                public int getMajor() {
                    return major;
                }

                public int getMinor() {
                    return minor;
                }

                public int getRevision() {
                    return revision;
                }

                public int getBuild() {
                    return build;
                }

                public String toString() {
                    return major + "." + minor + "." + revision + " Build "
                        + build;
                }
            };
        }
    }
}
