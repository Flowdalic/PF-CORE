/*
 * Copyright 2004 - 2008 Christian Sprajc. All rights reserved.
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
 * $Id$
 */
package de.dal33t.powerfolder.ui.notification;

import de.dal33t.powerfolder.Controller;

/**
 * Really simple message with no buttons, for previewing in the DialogSettingsTab.
 */
public class PreviewNotificationHandler extends NotificationHandlerBase {

    /**
     * Constructor.
     *
     * @param controller
     * @param title
     * @param messageText
     */
    public PreviewNotificationHandler(Controller controller,
                                   String title, String messageText) {
        super(controller, false);
        setTitle(title);
        setMessageText(messageText);
    }
}