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
package de.dal33t.powerfolder.event;

import java.awt.EventQueue;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingUtilities;

import de.dal33t.powerfolder.util.Logger;
import de.dal33t.powerfolder.util.ui.UIUtil;
import de.dal33t.powerfolder.Profiling;
import de.dal33t.powerfolder.ProfilingEntry;

/**
 * Factory used to created event/listener support upon eventlistner interfaces.
 * <P>
 * Created Listenersupport implementation maintains a listener list and handles
 * swing thread wrapping issues.
 * <p>
 * Listenersupport implementaion can be created upon an event listener
 * interface. E.g. for <code>TransferManagerListener</code>. This
 * Listenersupport implementaion will fire events to all registered listeners.
 * Just call the event method for the eventlistner interface on the
 * implementation returned by <code>createListenerSupport</code>
 *
 * @author <a href="mailto:totmacher@powerfolder.com">Christian Sprajc </a>
 * @version $Revision: 1.8 $
 */
public class ListenerSupportFactory {

    private static final Logger LOG = Logger
        .getLogger(ListenerSupportFactory.class);

    // AWT system check
    private static final boolean awtAvailable = UIUtil.isAWTAvailable();

    /**
     * Creates a listener support for the listener event interface. Returned
     * object can directly be casted into the listener event interface.
     * <p>
     * All calls to methods on that object will fire that event to its
     * registered listeners.
     * <p>
     *
     * @param listenerInterface
     * @return
     */
    public static Object createListenerSupport(Class listenerInterface) {
        if (listenerInterface == null) {
            throw new NullPointerException("Listener interface is empty");
        }
        if (!listenerInterface.isInterface()) {
            throw new IllegalArgumentException(
                "Listener interface class is not an java Interface!");
        }
        ClassLoader cl = listenerInterface.getClassLoader();
        InvocationHandler handler = new ListenerSupportInvocationHandler(
            listenerInterface);
        Object listenerSupportImpl = Proxy.newProxyInstance(cl,
            new Class[]{listenerInterface}, handler);
        LOG.verbose("Created event listener support for interface '"
            + listenerInterface.getName() + '\'');
        return listenerSupportImpl;
    }

    /**
     * Suspends (or resumes) a listener support, it set to true this listener
     * support will not file events until set to false. The listener support has
     * to be created via <code>createListenerSupport</code> before. Also the
     * listener needs to implement the listener event interface. Otherwise an
     * exception is thrown
     *
     * @param listenerSupport
     * @param suspended
     */
    public static void setSuspended(Object listenerSupport, boolean suspended) {
        if (listenerSupport == null) {
            throw new NullPointerException("Listener support is null");
        }
        if (!Proxy.isProxyClass(listenerSupport.getClass())) {
            throw new IllegalArgumentException(
                "Listener support is not valid. Seems not to be created with createListenerSupport.");
        }
        InvocationHandler invHandler = Proxy
            .getInvocationHandler(listenerSupport);
        if (invHandler instanceof ListenerSupportInvocationHandler) {
            ListenerSupportInvocationHandler lsInvHandler = (ListenerSupportInvocationHandler) invHandler;
            // Now suspend listener
            lsInvHandler.setSuspended(suspended);
        } else {
            throw new IllegalArgumentException(
                "Listener support is not valid. Seems not to be created with createListenerSupport.");
        }
    }

    /**
     * Adds a listener to a listener support. The listener support has to be
     * created via <code>createListenerSupport</code> factory method. Also the
     * listener needs to implement the listener event interface otherwise an
     * exception is thrown (see ListenerSupportInvocationHandler.checkListener).
     *
     * @param listenerSupport
     *      The listenerSupport where the listener should be added to.
     * @param listener
     *      The event listener to add.
     */
    public static void addListener(CoreListener listenerSupport,
        CoreListener listener)
    {
        if (listenerSupport == null) {
            throw new NullPointerException("Listener support is null");
        }
        if (!Proxy.isProxyClass(listenerSupport.getClass())) {
            throw new IllegalArgumentException(
                "Listener support is not valid. Seems not to be created with createListenerSupport.");
        }
        InvocationHandler invHandler = Proxy
            .getInvocationHandler(listenerSupport);
        if (invHandler instanceof ListenerSupportInvocationHandler) {
            ListenerSupportInvocationHandler lsInvHandler = (ListenerSupportInvocationHandler) invHandler;
            // Now add listener
            lsInvHandler.addListener(listener);
        } else {
            throw new IllegalArgumentException(
                "Listener support is not valid. Seems not to be created with createListenerSupport.");
        }
    }

    /**
     * Removes a listener from a listener support. The listener support has to
     * be created via <code>createListenerSupport</code> factory method. Also the
     * listener needs to implement the listener event interface otherwise an
     * exception is thrown (see ListenerSupportInvocationHandler.checkListener).
     *
     * @param listenerSupport
     * @param listener
     */
    public static void removeListener(Object listenerSupport,
        CoreListener listener)
    {
        if (listenerSupport == null) {
            throw new NullPointerException("Listener support is null");
        }
        if (!Proxy.isProxyClass(listenerSupport.getClass())) {
            throw new IllegalArgumentException(
                "Listener support is not valid. Seems not to be created with createListenerSupport.");
        }
        InvocationHandler invHandler = Proxy
            .getInvocationHandler(listenerSupport);
        if (invHandler instanceof ListenerSupportInvocationHandler) {
            ListenerSupportInvocationHandler lsInvHandler = (ListenerSupportInvocationHandler) invHandler;
            // Now remove the listener
            lsInvHandler.removeListener(listener);
        } else {
            throw new IllegalArgumentException(
                "Listener support is not valid. Seems not to be created with createListenerSupport.");
        }
    }

    /**
     * Removes all listeners from a listener support. The listener support has
     * to be created via <code>createListenerSupport</code> before. Otherwise
     * an exception is thrown
     *
     * @param listenerSupport
     * @param listener
     */
    public static void removeAllListeners(Object listenerSupport) {
        if (listenerSupport == null) {
            throw new NullPointerException("Listener support is null");
        }
        if (!Proxy.isProxyClass(listenerSupport.getClass())) {
            throw new IllegalArgumentException(
                "Listener support is not valid. Seems not to be created with createListenerSupport.");
        }
        InvocationHandler invHandler = Proxy
            .getInvocationHandler(listenerSupport);
        if (invHandler instanceof ListenerSupportInvocationHandler) {
            ListenerSupportInvocationHandler lsInvHandler = (ListenerSupportInvocationHandler) invHandler;
            // Now remove all listeners
            lsInvHandler.removeAllListeners();
        } else {
            throw new IllegalArgumentException(
                "Listener support is not valid. Seems not to be created with createListenerSupport.");
        }
    }

    // Inner classes **********************************************************

    /**
     * The invocation handler, which delegates fire event method calls to the
     * listener. Maybe suspended, in this state it will not fire events.
     *
     * @author <a href="mailto:totmacher@powerfolder.com">Christian Sprajc </a>
     */
    private static class ListenerSupportInvocationHandler implements
        InvocationHandler
    {
        private Class listenerInterface;
        private List<CoreListener> listenersNotInDispatchThread;
        private List<CoreListener> listenersInDispatchThread;
        private boolean suspended;

        /**
         * Creates an invocation handler which basically handles the event
         * support.
         *
         * @param listenerInterface
         *            the listener event interface
         */
        private ListenerSupportInvocationHandler(Class listenerInterface) {
            this.listenerInterface = listenerInterface;
            this.listenersInDispatchThread = new CopyOnWriteArrayList<CoreListener>();
            this.listenersNotInDispatchThread = new CopyOnWriteArrayList<CoreListener>();
        }

        /**
         * Adds a listener to this support impl
         *
         * @param listener
         */
        public void addListener(CoreListener listener) {
            if (checkListener(listener)) {
                // Okay, add listener
                if (listener.fireInEventDispathThread()) {
                    listenersInDispatchThread.add(listener);
                } else {
                    listenersNotInDispatchThread.add(listener);
                }
            }
        }

        /**
         * Removes a listener from this support impl
         *
         * @param listener
         */
        public void removeListener(CoreListener listener) {
            if (checkListener(listener)) {
                // Okay, remove listener
                if (listener.fireInEventDispathThread()) {
                    listenersInDispatchThread.remove(listener);
                } else {
                    listenersNotInDispatchThread.remove(listener);
                }
            }
        }

        /**
         * Removes all listeners from this support impl
         */
        public void removeAllListeners() {
            listenersInDispatchThread.clear();
            listenersNotInDispatchThread.clear();
        }

        /**
         * Checks if the listener is an instance of our supported listener
         * interface.
         *
         * @param listener The listener to check
         * @return true if succeeded, otherwise exception is thrown
         * @throws IllegalArgumentException
         *             if both do not match
         */
        private boolean checkListener(CoreListener listener) {
            if (listener == null) {
                throw new NullPointerException("Listener is null");
            }
            if (!listenerInterface.isInstance(listener)) {
                throw new IllegalArgumentException("Listener '" + listener
                    + "' is not an instance of support listener interface '"
                    + listenerInterface.getName() + '\'');
            }
            return true;
        }

        /**
         * Delegates calls to registered listeners
         *
         * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
         *      java.lang.reflect.Method, java.lang.Object[])
         */
        public Object invoke(Object proxy, final Method method,
            final Object[] args) throws Throwable
        {
            if (listenersInDispatchThread.isEmpty()
                && listenersNotInDispatchThread.isEmpty())
            {
                // No listeners, skip
                return null;
            }

            // Create runner
            if (!suspended) {
                Runnable runner = new Runnable() {
                    public void run() {
                        for (CoreListener listener : listenersInDispatchThread)
                        {
                            ProfilingEntry profilingEntry = Profiling
                                    .start(listener.getClass()
                                            .toString() + ':' +
                                            method.getName());
                            try {
                                method.invoke(listener, args);
                            } catch (IllegalArgumentException e) {
                                LOG.error(
                                    "Received an exception from listener '"
                                        + listener + "', class '"
                                        + listener.getClass().getName() + '\'',
                                    e);
                            } catch (IllegalAccessException e) {
                                LOG.error(
                                    "Received an exception from listener '"
                                        + listener + "', class '"
                                        + listener.getClass().getName() + '\'',
                                    e);
                            } catch (InvocationTargetException e) {
                                LOG.error(
                                    "Received an exception from listener '"
                                        + listener + "', class '"
                                        + listener.getClass().getName() + '\'',
                                    e.getCause());
                                // Also log original exception
                                LOG.verbose(e);
                            } finally {
                                Profiling.end(profilingEntry, 50);
                            }
                        }
                    }
                };

                if (!awtAvailable || EventQueue.isDispatchThread()) {
                    // NO awt system ? do not put in swing thread
                    // Already in swing thread ? also don't wrap
                    runner.run();
                } else {
                    // Put runner in swingthread
                    SwingUtilities.invokeLater(runner);
                }

                for (CoreListener listener : listenersNotInDispatchThread) {
                    ProfilingEntry profilingEntry = Profiling.start(
                            listener.getClass().toString()
                                    + ':' + method.getName());
                    try {
                        method.invoke(listener, args);
                    } catch (IllegalArgumentException e) {
                        LOG.error("Received an exception from listener '"
                            + listener + "', class '"
                            + listener.getClass().getName() + '\'', e);
                    } catch (IllegalAccessException e) {
                        LOG.error("Received an exception from listener '"
                            + listener + "', class '"
                            + listener.getClass().getName() + '\'', e);
                    } catch (InvocationTargetException e) {
                        LOG
                            .error("Received an exception from listener '"
                                + listener + "', class '"
                                + listener.getClass().getName() + '\'', e
                                .getCause());
                        // Also log original exception
                        LOG.verbose(e);
                    } finally {
                        Profiling.end(profilingEntry, 50);
                    }
                }
            }
            return null;

        }

        public boolean isSuspended() {
            return suspended;
        }

        public void setSuspended(boolean suspended) {
            this.suspended = suspended;
        }
    }

}
