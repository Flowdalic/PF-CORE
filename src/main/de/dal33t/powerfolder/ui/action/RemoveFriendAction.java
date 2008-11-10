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
* $Id: NewFolderAction.java 5419 2008-09-29 12:18:20Z harry $
*/
package de.dal33t.powerfolder.ui.action;

import de.dal33t.powerfolder.Controller;
import de.dal33t.powerfolder.Member;
import de.dal33t.powerfolder.light.MemberInfo;

import java.awt.event.ActionEvent;

/**
 * Action which removes a member from friends.
 *
 * @author <a href="mailto:hglasgow@powerfolder.com">Harry Glasgow</a>
 * @version $Revision: 4.0 $
 */
public class RemoveFriendAction extends BaseAction {

    public RemoveFriendAction(Controller controller) {
        super("action_remove_friend", controller);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source != null && source instanceof MemberInfo) {
            MemberInfo memberInfo = (MemberInfo) source;
            Member member = getController().getNodeManager().getNode(memberInfo);
            member.setFriend(false, null);
        }
    }
}