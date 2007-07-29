package de.dal33t.powerfolder.test;

import java.io.File;

import de.dal33t.powerfolder.disk.Folder;
import de.dal33t.powerfolder.disk.SyncProfile;
import de.dal33t.powerfolder.disk.FolderSettings;
import de.dal33t.powerfolder.light.FolderInfo;
import de.dal33t.powerfolder.util.IdGenerator;
import de.dal33t.powerfolder.util.test.ControllerTestCase;

/**
 * Scan took: 49301 Scan took: 21651 Scan took: 21892 Scan took: 21651 Scan
 * took: 21421
 */
public class TestScanFolder extends ControllerTestCase {

    private TestScanFolder() {
        try {
            setUp();
            doTest();
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.exit(0);
    }
    private String location = "f:/test";
    private Folder folder;

    private void doTest() throws Exception {
        FolderInfo testFolder = new FolderInfo("testFolder", IdGenerator
            .makeId(), true);
        FolderSettings folderSettings = new FolderSettings(new File(location),
            SyncProfile.MANUAL_DOWNLOAD, false, true);
        folder = getController().getFolderRepository().createFolder(testFolder,
            folderSettings);

        // long started =System.currentTimeMillis();
        scanFolder(folder);
        // FolderScanner scanner = new FolderScanner(getController());
        // new Thread(scanner).start();
        // scanner.scan(folder);

        // while(scanner.isScanning()) {
        // Thread.sleep(1000);
        // }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new TestScanFolder();

    }

}
