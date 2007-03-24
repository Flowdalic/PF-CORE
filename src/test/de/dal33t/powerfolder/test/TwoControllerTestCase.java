/* $Id: TwoControllerTestCase.java,v 1.2 2006/04/21 22:58:42 totmacherr Exp $
 */
package de.dal33t.powerfolder.test;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import de.dal33t.powerfolder.Controller;
import de.dal33t.powerfolder.Member;
import de.dal33t.powerfolder.disk.Folder;
import de.dal33t.powerfolder.disk.FolderException;
import de.dal33t.powerfolder.disk.SyncProfile;
import de.dal33t.powerfolder.light.FileInfo;
import de.dal33t.powerfolder.light.FolderInfo;
import de.dal33t.powerfolder.net.ConnectionException;
import de.dal33t.powerfolder.test.TestHelper.Condition;
import de.dal33t.powerfolder.util.Format;
import de.dal33t.powerfolder.util.Logger;
import de.dal33t.powerfolder.util.Reject;

/**
 * Provides basic testcase-setup with two controllers. Bart and Lisa
 * <p>
 * After <code>#setUp()</code> is invoked it is ensured, that both controllers
 * are running. There are several utility methods to bring the test into a usual
 * state. To connect both controllers just call
 * <code>{@link #connectBartAndLisa()}</code> in <code>{@link #setUp()}</code>.
 * After that both controllers are connected, Lisa runs in normal node, Bart as
 * supernode.
 * <p>
 * You can access both controllers and do manupulating/testing stuff on them
 * 
 * @author <a href="mailto:totmacher@powerfolder.com">Christian Sprajc</a>
 * @version $Revision: 1.2 $
 */
public class TwoControllerTestCase extends TestCase {
    // For the optional test folder.
    protected static final File TESTFOLDER_BASEDIR_BART = new File(TestHelper
        .getTestDir(), "ControllerBart/testFolder");
    protected static final File TESTFOLDER_BASEDIR_LISA = new File(TestHelper
        .getTestDir(), "ControllerLisa/testFolder");

    private Controller controllerBart;
    private Controller controllerLisa;

    // The optional test folders
    private Folder folderBart;
    private Folder folderLisa;

    protected void setUp() throws Exception {
        super.setUp();

        Logger.setPrefixEnabled(true);
       // Logger.removeExcludeConsoleLogLevel(Logger.VERBOSE);
        Logger.addExcludeConsoleLogLevel(Logger.DEBUG);
        System.setProperty("powerfolder.test", "true");

        // Cleanup
        TestHelper.cleanTestDir();
        FileUtils.deleteDirectory(new File(Controller.getMiscFilesLocation(),
            "build"));

        // Copy fresh configs
        FileUtils.copyFile(
            new File("src/test-resources/ControllerBart.config"), new File(
                "build/test/ControllerBart/PowerFolder.config"));
        FileUtils.copyFile(
            new File("src/test-resources/ControllerLisa.config"), new File(
                "build/test/ControllerLisa/PowerFolder.config"));

        // Start controllers
        System.out.println("Starting controllers...");
        controllerBart = Controller.createController();
        controllerBart.startConfig("build/test/ControllerBart/PowerFolder");
        waitForStart(controllerBart);
        controllerBart.getPreferences().putBoolean("createdesktopshortcuts",
            false);
        controllerLisa = Controller.createController();
        controllerLisa.startConfig("build/test/ControllerLisa/PowerFolder");
        waitForStart(controllerLisa);
        controllerLisa.getPreferences().putBoolean("createdesktopshortcuts",
            false);
        System.out.println("Controllers started");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        controllerBart.shutdown();
        controllerLisa.shutdown();

        // Give them time to shut down
        Thread.sleep(1000);
        int i = 0;
        while (controllerBart.isShuttingDown()) {
            i++;
            if (i > 100) {
                System.out.println("Shutdown of Bart failed");
                break;
            }
            Thread.sleep(1000);
        }
        i = 0;
        while (controllerLisa.isShuttingDown()) {
            i++;
            if (i > 100) {
                System.out.println("Shutdown of Lisa failed");
                break;
            }
            Thread.sleep(1000);
        }
        assertFalse(controllerBart.isStarted());
        assertFalse(controllerLisa.isStarted());

        // add a pause to make sure files can be cleaned before next test.
        TestHelper.waitMilliSeconds(500);
    }

    // For subtest ************************************************************

    protected Controller getContollerBart() {
        return controllerBart;
    }

    protected Controller getContollerLisa() {
        return controllerLisa;
    }

    // Helpers ****************************************************************

    /**
     * Makes lisa and bart friends. Sweet! ;)
     */
    protected void makeFriends() {
        Member lisaAtBart = controllerBart.getNodeManager().getNode(
            controllerLisa.getMySelf().getId());
        if (lisaAtBart == null) {
            lisaAtBart = controllerBart.getNodeManager().addNode(
                controllerLisa.getMySelf().getInfo());
        }
        lisaAtBart.setFriend(true);

        Member bartAtLisa = controllerLisa.getNodeManager().getNode(
            controllerBart.getMySelf().getId());
        if (bartAtLisa == null) {
            bartAtLisa = controllerBart.getNodeManager().addNode(
                controllerBart.getMySelf().getInfo());
        }
        bartAtLisa.setFriend(true);
    }

    /**
     * @see #joinTestFolder(SyncProfile)
     * @return the test folder @ bart. or null if not setup.
     */
    protected Folder getFolderAtBart() {
        return folderBart;
    }

    /**
     * @see #joinTestFolder(SyncProfile)
     * @return the test folder @ lisa. or null if not setup.
     */
    protected Folder getFolderAtLisa() {
        return folderLisa;
    }

    /**
     * Joins both controllers into a testfolder. get these testfolders with
     * <code>getTestFolderBart()</code> and <code>getTestFolderLisa()</code>
     * 
     * @see #getFolderAtBart()
     * @see #getFolderAtLisa()
     */
    protected void joinTestFolder(SyncProfile syncprofile) {
        FolderInfo testFolder = new FolderInfo("testFolder", UUID.randomUUID()
            .toString(), true);
        joinFolder(testFolder, TESTFOLDER_BASEDIR_BART,
            TESTFOLDER_BASEDIR_LISA, syncprofile);
        folderBart = getContollerBart().getFolderRepository().getFolder(
            testFolder);
        folderLisa = getContollerLisa().getFolderRepository().getFolder(
            testFolder);
    }

    /**
     * Waits for the controller to startup
     * 
     * @param controller
     */
    protected void waitForStart(final Controller controller) {
        TestHelper.waitForCondition(30, new Condition() {
            public boolean reached() {
                return controller.isStarted();
            }
        });
    }

    /**
     * Try to connect controllers.
     * 
     * @return true if the lisa and bart are connected.
     */
    protected boolean tryToConnectBartAndLisa() {
        // Wait for connection between both controllers
        try {
            return connect(controllerLisa, controllerBart);
        } catch (InterruptedException e) {
            return false;
        } catch (ConnectionException e) {
            return false;
        }
    }

    /**
     * Connects both controllers.
     */
    protected void connectBartAndLisa() {
        // Wait for connection between both controllers
        try {
            if (!connect(controllerLisa, controllerBart)) {
                fail("Unable to connect Bart and Lisa");
            }
        } catch (InterruptedException e) {
            fail(e.toString());
        } catch (ConnectionException e) {
            e.printStackTrace();
            fail(e.toString());
        }

        // Bart should be supernode
        assertTrue(controllerBart.getMySelf().isSupernode());
    }

    /**
     * Disconnectes Lisa and Bart.
     */
    protected void disconnectBartAndLisa() {
        Member lisaAtBart = getContollerBart().getNodeManager().getNode(
            getContollerLisa().getMySelf().getId());
        lisaAtBart.shutdown();
        Member bartAtLisa = getContollerLisa().getNodeManager().getNode(
            getContollerBart().getMySelf().getId());
        bartAtLisa.shutdown();
    }

    /**
     * Connects and waits for connection of both controllers
     * 
     * @param cont1
     * @param cont2
     * @throws InterruptedException
     * @throws ConnectionException
     */
    private boolean connect(Controller cont1, Controller cont2)
        throws InterruptedException, ConnectionException
    {
        Reject.ifTrue(!cont1.isStarted(), "Controller1 not started yet");
        Reject.ifTrue(!cont2.isStarted(), "Controller2 not started yet");

        // Connect
        System.out.println("Connecting controllers...");
        System.out.println("Con to: "
            + cont2.getConnectionListener().getAddress());

        Member member2atCon1 = cont1.getNodeManager().getNode(
            cont2.getMySelf().getId());
        Member member1atCon2 = cont2.getNodeManager().getNode(
            cont1.getMySelf().getId());
        boolean connected = member2atCon1 != null && member1atCon2 != null
            && member2atCon1.isCompleteyConnected()
            && member1atCon2.isCompleteyConnected();
        if (connected) {
            // Already connected
            return true;
        }
        int i = 0;
        do {
            if (i % 10 == 0) {
                cont1.connect(cont2.getConnectionListener().getAddress());
            }
            member2atCon1 = cont1.getNodeManager().getNode(
                cont2.getMySelf().getId());
            member1atCon2 = cont2.getNodeManager().getNode(
                cont1.getMySelf().getId());
            connected = member2atCon1 != null && member1atCon2 != null
                && member2atCon1.isCompleteyConnected()
                && member1atCon2.isCompleteyConnected();

            i++;
            Thread.sleep(100);
            if (i > 50) {
                return false;
            }
        } while (!connected);
        System.out.println("Both Controller connected");
        return true;
    }

    /**
     * Let both controller join the specified folder.
     * <p>
     * After the method is invoked, it is ensured that folders on both
     * controllers have two members. Otherwise the test will fail.
     * <p>
     * Sets the syncprofile to <code>MANUAL_DOWNLOAD</code>
     * 
     * @param foInfo
     *            the folder to join
     * @param bartFolderDir
     *            the local base dir for folder at bart
     * @param lisaFolderDir
     *            the local base dir for folder at lisa
     */
    protected void joinFolder(FolderInfo foInfo, File bartFolderDir,
        File lisaFolderDir)
    {
        final Folder folder1;
        final Folder folder2;
        try {
            folder1 = getContollerBart().getFolderRepository().createFolder(
                foInfo, bartFolderDir, SyncProfile.MANUAL_DOWNLOAD, false);

            folder2 = getContollerLisa().getFolderRepository().createFolder(
                foInfo, lisaFolderDir, SyncProfile.MANUAL_DOWNLOAD, false);
        } catch (FolderException e) {
            e.printStackTrace();
            fail("Unable to join both controller to " + foInfo + ". "
                + e.toString());
            return;
        }

        // Give them time to join
        TestHelper.waitForCondition(300, new Condition() {
            public boolean reached() {
                return folder1.getMembersCount() >= 2
                    && folder2.getMembersCount() >= 2;
            }
        });
    }

    /**
     * Let both controller join the specified folder.
     * <p>
     * After the method is invoked, it is ensured that folders on both
     * controllers have two members. Otherwise the test will fail.
     * 
     * @param foInfo
     *            the folder to join
     * @param baseDir1
     *            the local base dir for the first controller
     * @param baseDir2
     *            the local base dir for the second controller
     * @param profile
     *            the profile to use
     */
    protected void joinFolder(FolderInfo foInfo, File baseDir1, File baseDir2,
        SyncProfile profile)
    {
        final Folder folder1;
        final Folder folder2;
        try {
            folder1 = getContollerBart().getFolderRepository().createFolder(
                foInfo, baseDir1, profile, false);

            folder2 = getContollerLisa().getFolderRepository().createFolder(
                foInfo, baseDir2, profile, false);
        } catch (FolderException e) {
            e.printStackTrace();
            fail("Unable to join both controller to " + foInfo + ". "
                + e.toString());
            return;
        }

        // Give them time to join
        TestHelper.waitForCondition(30, new Condition() {
            public boolean reached() {
                return folder1.getMembersCount() >= 2
                    && folder2.getMembersCount() >= 2;
            }
        });
    }

    /**
     * Tests if the diskfile matches the fileinfo. Checks name, lenght/size,
     * modification date and the deletion status.
     * 
     * @param diskFile
     *            the diskfile to compare
     * @param fInfo
     *            the fileinfo
     * @param controller
     *            the controller to use.
     */
    protected void assertFileMatch(File diskFile, FileInfo fInfo,
        Controller controller)
    {
        boolean nameMatch = diskFile.getName().equals(fInfo.getFilenameOnly());
        boolean sizeMatch = diskFile.length() == fInfo.getSize();
        boolean fileObjectEquals = diskFile.equals(fInfo.getDiskFile(controller
            .getFolderRepository()));
        boolean deleteStatusMatch = diskFile.exists() == !fInfo.isDeleted();
        boolean lastModifiedMatch = diskFile.lastModified() == fInfo
            .getModifiedDate().getTime();

        // Skip last modification test when diskfile is deleted.
        boolean matches = !diskFile.isDirectory() && nameMatch && sizeMatch
            && (!diskFile.exists() || lastModifiedMatch) && deleteStatusMatch
            && fileObjectEquals;

        assertTrue("FileInfo does not match physical file. \nFileInfo:\n "
            + fInfo.toDetailString() + "\nFile:\n " + diskFile.getName()
            + ", size: " + Format.formatBytes(diskFile.length())
            + ", lastModified: " + new Date(diskFile.lastModified())
            + "\n\nWhat matches?:\nName: " + nameMatch + "\nSize: " + sizeMatch
            + "\nlastModifiedMatch: " + lastModifiedMatch + "\ndeleteStatus: "
            + deleteStatusMatch + "\nFileObjectEquals: " + fileObjectEquals,
            matches);
    }
}
