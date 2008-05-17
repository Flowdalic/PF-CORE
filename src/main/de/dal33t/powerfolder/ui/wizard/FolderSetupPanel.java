/* $Id: LoadInvitationPanel.java,v 1.11 2006/03/04 11:16:39 bytekeeper Exp $
 */
package de.dal33t.powerfolder.ui.wizard;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.dal33t.powerfolder.Controller;
import de.dal33t.powerfolder.disk.SyncProfile;
import de.dal33t.powerfolder.light.FolderInfo;
import de.dal33t.powerfolder.ui.Icons;
import static de.dal33t.powerfolder.ui.wizard.WizardContextAttributes.*;
import de.dal33t.powerfolder.util.IdGenerator;
import de.dal33t.powerfolder.util.Translation;
import de.dal33t.powerfolder.util.ui.SimpleComponentFactory;
import de.dal33t.powerfolder.util.ui.SyncProfileSelectorPanel;
import jwf.WizardPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class to do folder creation for an optional specified folderInfo.
 * 
 * @author <a href="mailto:harry@powerfolder.com">Harry Glasgow</a>
 * @version $Revision: 1.11 $
 */
public class FolderSetupPanel extends PFWizardPanel {

    private final String initialFolderName;

    private JTextField folderNameTextField;
    private SyncProfileSelectorPanel syncProfileSelectorPanel;
    private JCheckBox sendInviteAfterCB;

    /**
     * Constuctor
     * 
     * @param controller
     * @param folderName
     *            the recommended folder name.
     */
    public FolderSetupPanel(Controller controller, String folderName) {
        super(controller);
        initialFolderName = folderName;
    }

    /**
     * Can procede if an invitation exists.
     */
    public boolean hasNext() {
        return folderNameTextField.getText().trim().length() > 0;
    }

    public WizardPanel next() {

        // Set FolderInfo
        FolderInfo folderInfo = new FolderInfo(folderNameTextField.getText()
            .trim(), '[' + IdGenerator.makeId() + ']');
        getWizardContext().setAttribute(FOLDERINFO_ATTRIBUTE, folderInfo);

        // Set sync profile
        getWizardContext().setAttribute(SYNC_PROFILE_ATTRIBUTE,
            syncProfileSelectorPanel.getSyncProfile());

        // Do not prompt for send invitation afterwards
        getWizardContext().setAttribute(SEND_INVIATION_AFTER_ATTRIBUTE,
            sendInviteAfterCB.isSelected());

        // Setup choose disk location panel
        getWizardContext().setAttribute(PROMPT_TEXT_ATTRIBUTE,
            Translation.getTranslation("wizard.invite.selectlocaldirectory"));

        // Setup sucess panel of this wizard path
        TextPanelPanel successPanel = new TextPanelPanel(getController(),
            Translation.getTranslation("wizard.setupsuccess"), Translation
                .getTranslation("wizard.successjoin"));
        getWizardContext().setAttribute(PFWizard.SUCCESS_PANEL, successPanel);

        return new FolderCreatePanel(getController());
    }

    protected JPanel buildContent() {
        FormLayout layout = new FormLayout("right:pref, 5dlu, pref",
            "pref, 5dlu, pref, 5dlu, pref, 5dlu, pref");

        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        // Folder Name
        builder.add(new JLabel(Translation.getTranslation("fileinfo.name")), cc
            .xy(1, 1));
        builder.add(folderNameTextField, cc.xy(3, 1));

        // Sync
        builder.add(new JLabel(Translation
            .getTranslation("general.synchonisation")), cc.xy(1, 3));
        JPanel p = (JPanel) syncProfileSelectorPanel.getUIComponent();
        p.setOpaque(false);
        builder.add(p, cc.xy(3, 3));

        // Send Invite
        builder.add(sendInviteAfterCB, cc.xy(3, 5));
        return builder.getPanel();
    }

    /**
     * Initializes all necessary components
     */
    protected void initComponents() {

        getWizardContext().setAttribute(PFWizard.PICTO_ICON,
            Icons.FILESHARING_PICTO);

        // Folder name label
        folderNameTextField = SimpleComponentFactory.createTextField(true);
        folderNameTextField.setText(initialFolderName);
        folderNameTextField.addKeyListener(new MyKeyListener());

        // Sync profile
        syncProfileSelectorPanel = new SyncProfileSelectorPanel(
            getController(), SyncProfile.SYNCHRONIZE_PCS);

        // Send Invite
        sendInviteAfterCB = SimpleComponentFactory.createCheckBox(Translation
            .getTranslation("wizard.setup_folder.sendinvitation"));
        sendInviteAfterCB.setOpaque(false);
        sendInviteAfterCB.setSelected(true);
    }

    protected Icon getPicto() {
        return getContextPicto();
    }

    protected String getTitle() {
        return Translation.getTranslation("wizard.setup_folder.title");
    }

    private class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            // Not implemented
        }

        public void keyReleased(KeyEvent e) {
            // Not implemented
        }

        public void keyTyped(KeyEvent e) {
            updateButtons();
        }
    }
}