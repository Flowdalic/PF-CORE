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
package de.dal33t.powerfolder.ui.navigation;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.dal33t.powerfolder.event.NavigationEvent;
import de.dal33t.powerfolder.event.NavigationListener;

/**
 * The Navigation Model holds the stacks that make the forward/back and up
 * buttons work.<BR>
 * FIXME: seperate UI update events and real change of the selected item events.<BR>
 * 
 * @author <A HREF="mailto:schaatser@powerfolder.com">Jan van Oosterom</A>
 * @version $Revision: 1.1 $
 */
public class NavigationModel {
    /** stack of tree paths */
    private Stack<TreePath> forwardStack = new Stack<TreePath>();
    /** stack of tree paths */
    private Stack<TreePath> backStack = new Stack<TreePath>();
    private List<NavigationListener> listeners = new LinkedList<NavigationListener>();
    private TreeSelectionModel treeSelectionModel;

    /** used to prevent loops* */
    private TreePath last;
    /** true if a navigation button is clicked false it item in tree is clicked */
    private boolean thisAsSource = false;

    public NavigationModel(TreeSelectionModel treeSelectionModel) {
        this.treeSelectionModel = treeSelectionModel;
        treeSelectionModel
            .addTreeSelectionListener(new NavTreeSelectionListener());
    }

    public boolean hasParent() {
        TreePath currentPath = treeSelectionModel.getSelectionPath();
        if (currentPath != null) {
            TreePath parentPath = currentPath.getParentPath();
            if (parentPath == null) {
                return false;
            }
            return (parentPath.getPathCount() != 0);
        }
        return false;
    }

    public boolean hasBack() {
        return !backStack.isEmpty();
    }

    public boolean hasForward() {
        return !forwardStack.isEmpty();
    }

    public void up() {
        thisAsSource = true;
        TreePath currentPath = treeSelectionModel.getSelectionPath();
        if (currentPath != null) {
            TreePath parentPath = currentPath.getParentPath();
            if (parentPath.getPathCount() != 0) {
                forwardStack.clear();
                backStack.push(currentPath);
                last = parentPath;
                fireNavigationChanged(parentPath);
            }
        }
    }

    public Object peekUp() {
        TreePath currentPath = treeSelectionModel.getSelectionPath();
        if (currentPath != null) {
            TreePath parentPath = currentPath.getParentPath();
            if (parentPath == null || parentPath.getPathCount() == 0) {
                return null;
            }
            return parentPath.getLastPathComponent();
        }
        return null;
    }

    public void forward() {
        thisAsSource = true;
        if (!forwardStack.empty()) {
            TreePath currentPath = treeSelectionModel.getSelectionPath();
            if (currentPath != null) {
                backStack.push(currentPath);
            }
            TreePath newTreePath = forwardStack.pop();
            last = newTreePath;
            fireNavigationChanged(newTreePath);
        }
    }

    public Object peekForward() {
        if (!forwardStack.empty()) {
            return forwardStack.peek().getLastPathComponent();
        }
        return null;
    }

    public void back() {
        thisAsSource = true;
        if (!backStack.empty()) {
            TreePath currentPath = treeSelectionModel.getSelectionPath();
            if (currentPath != null) {
                forwardStack.push(currentPath);
            }
            TreePath newTreePath = backStack.pop();
            last = newTreePath;
            fireNavigationChanged(newTreePath);
        }
    }

    public Object peekBack() {
        if (!backStack.empty()) {
            return backStack.peek().getLastPathComponent();
        }
        return null;
    }

    /**
     * Directly set the navigation path.
     * 
     * @param newTreePath
     */
    public void setPath(TreePath newTreePath) {
        last = newTreePath;
        fireNavigationChanged(newTreePath);
    }

    public void addNavigationListener(NavigationListener listener) {
        listeners.add(listener);
    }

    public void removeNavigationListener(NavigationListener listener) {
        listeners.add(listener);
    }

    private void fireNavigationChanged(TreePath treePath) {
        NavigationEvent event = new NavigationEvent(this, treePath);
        for (int i = 0; i < listeners.size(); i++) {
            NavigationListener listener = listeners.get(i);
            listener.navigationChanged(event);
        }
    }

    private final class NavTreeSelectionListener implements
        TreeSelectionListener
    {
        public void valueChanged(TreeSelectionEvent e) {
            TreePath oldPath = e.getOldLeadSelectionPath();
            TreePath newPath = e.getNewLeadSelectionPath();
            if (thisAsSource) { // if a nav button is clicked
                if (newPath != null && last != null) {
                    if (!last.equals(newPath)) {// make sure we dont create
                        // a loop
                        if (oldPath != null) {
                            backStack.push(oldPath);
                            last = null;
                            thisAsSource = false;
                            // updates the ui buttons
                            fireNavigationChanged(newPath);
                        }
                    }
                }
            } else {// if a tree item is clicked
                forwardStack.clear();
                if (newPath != null && oldPath != null) {
                    backStack.push(oldPath);
                    last = null;
                    thisAsSource = false;
                    // updates the ui buttons
                    fireNavigationChanged(newPath);
                }
            }
            last = null;
            thisAsSource = false;
        }
    }
}
