package de.dal33t.powerfolder.ui.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.dal33t.powerfolder.ConfigurationEntry;
import de.dal33t.powerfolder.Controller;
import de.dal33t.powerfolder.PFComponent;
import de.dal33t.powerfolder.util.Translation;
import de.dal33t.powerfolder.util.ui.SimpleComponentFactory;

public class DynDnsSettingsTab extends PFComponent implements PreferenceTab {
    // disposition constants for status messages
    final static int DISP_INFO = 0; // just informing message
    final static int DISP_WARNING = 1; // warning
    final static int DISP_ERROR = 2; // error

    public static String password;
    public static String username;
    public static String newDyndns;
    public static String dyndnsSystem;

    private JPanel panel;
    private JTextField dyndnsUserField;
    private JPasswordField dyndnsPasswordField;
    private JLabel currentIPField;
    private JLabel updatedIPField;
    private JCheckBox cbAutoUpdate;
    private ValueModel mydnsndsModel;
    private JLabel dyndnsHost;
    private JButton updateButton;

    public DynDnsSettingsTab(Controller controller, ValueModel mydnsndsModel) {
        super(controller);
        this.mydnsndsModel = mydnsndsModel;
        initComponents();
    }

    public String getTabName() {
        return Translation.getTranslation("preferences.dialog.dyndns.title");
    }

    public boolean needsRestart() {
        return false;
    }

    public void undoChanges() {

    }

    public boolean validate() {
        if (mydnsndsModel.getValue() == null
            || ((String) mydnsndsModel.getValue()).trim().length() == 0)
        {
            return true;
        }
        if (!getController().getDynDnsManager().validateDynDns(
            mydnsndsModel.getValue().toString()))
        {
            // myDnsField.grabFocus();
            // myDnsField.selectAll();
            return false;
        }

        // all validations have passed
        return true;
    }

    /**
     * Saves the dyndns settings
     */
    public void save() {
        String theDyndnsHost = (String) mydnsndsModel.getValue();
        if (!StringUtils.isBlank(theDyndnsHost)) {
            ConfigurationEntry.DYNDNS_HOSTNAME.setValue(getController(),
                theDyndnsHost);
        } else {
            ConfigurationEntry.DYNDNS_HOSTNAME.removeValue(getController());
        }

        if (!StringUtils.isBlank(theDyndnsHost)) {
            if (!StringUtils.isBlank(dyndnsUserField.getText())) {
                ConfigurationEntry.DYNDNS_USERNAME.setValue(getController(),
                    dyndnsUserField.getText());
            } else {
                ConfigurationEntry.DYNDNS_USERNAME.removeValue(getController());
            }

            String thePassword = new String(dyndnsPasswordField.getPassword());
            if (!StringUtils.isBlank(thePassword)) {
                ConfigurationEntry.DYNDNS_PASSWORD.setValue(getController(),
                    thePassword);
            } else {
                ConfigurationEntry.DYNDNS_PASSWORD.removeValue(getController());
            }
        }

        boolean b = cbAutoUpdate.isSelected();
        ConfigurationEntry.DYNDNS_AUTO_UPDATE.setValue(getController(),
            Boolean.valueOf(b).toString());
        
        // Let the DynDns manager check if he needs to do something.
        getController().getDynDnsManager().update();
    }

    /*
     * Builds DynDns UI panel
     */
    public JPanel getUIPanel() {
        if (panel == null) {
            FormLayout layout = new FormLayout(
                "right:pref, 7dlu, 80dlu, 3dlu, left:40dlu",
                "pref, 3dlu, pref, 7dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, "
                    + "3dlu, pref, 7dlu, pref, 7dlu");

            PanelBuilder builder = new PanelBuilder(layout);
            builder.setBorder(Borders.createEmptyBorder("3dlu, 3dlu, 0, 3dlu"));
            CellConstraints cc = new CellConstraints();
            int row = 1;

            builder.addLabel(Translation
                .getTranslation("preferences.dialog.dyndnsLoginPanel"), cc.xy(
                1, row));
            row += 2;

            builder.addLabel(Translation
                .getTranslation("preferences.dialog.dyndnsHostname"), cc.xy(1,
                row));
            builder.add(dyndnsHost, cc.xywh(3, row, 3, 1));

            row += 2;
            builder.addLabel(Translation
                .getTranslation("preferences.dialog.dyndnsUserName"), cc.xy(1,
                row));
            builder.add(dyndnsUserField, cc.xywh(3, row, 3, 1));

            row += 2;
            dyndnsPasswordField.setEchoChar('*');
            builder.addLabel(Translation
                .getTranslation("preferences.dialog.dyndnsPassword"), cc.xy(1,
                row));
            builder.add(dyndnsPasswordField, cc.xywh(3, row, 3, 1));

            row += 4;
            builder.addLabel(Translation
                .getTranslation("preferences.dialog.dyndnsCurrentIP"), cc.xy(1,
                row));
            builder.add(currentIPField, cc.xywh(3, row, 3, 1));

            row += 2;
            builder.addLabel(Translation
                .getTranslation("preferences.dialog.dyndnsUpdatedIP"), cc.xy(1,
                row));
            builder.add(updatedIPField, cc.xywh(3, row, 3, 1));

            row += 2;
            builder.addLabel(Translation
                .getTranslation("preferences.dialog.dyndnsAutoUpdate"), cc
                .xy(1, row));
            builder.add(cbAutoUpdate, cc.xywh(3, row, 3, 1));

            row += 2;
            builder.add(updateButton, cc.xy(3, row));

            panel = builder.getPanel();
        }
        return panel;
    }

    private void initComponents() {
        dyndnsHost = BasicComponentFactory.createLabel(mydnsndsModel);

        if (ConfigurationEntry.DYNDNS_USERNAME.getValue(getController()) == null)
        {
            dyndnsUserField = new JTextField("");
        } else {
            dyndnsUserField = new JTextField(ConfigurationEntry.DYNDNS_USERNAME
                .getValue(getController()));
        }

        if (ConfigurationEntry.DYNDNS_PASSWORD.getValue(getController()) == null)
        {
            dyndnsPasswordField = new JPasswordField("");
        } else {
            dyndnsPasswordField = new JPasswordField(
                ConfigurationEntry.DYNDNS_PASSWORD.getValue(getController()));
        }

        currentIPField = new JLabel(getController().getDynDnsManager()
            .getDyndnsViaHTTP());

        if (!isUpdateSelected()) {
            updatedIPField = new JLabel(getController().getDynDnsManager()
                .getHostIP(
                    ConfigurationEntry.DYNDNS_HOSTNAME
                        .getValue(getController())));
        } else {
            updatedIPField = new JLabel(
                ConfigurationEntry.DYNDNS_LAST_UPDATED_UP
                    .getValue(getController()));
        }

        cbAutoUpdate = SimpleComponentFactory.createCheckBox();
        cbAutoUpdate.setSelected(isUpdateSelected());

        updateButton = createUpdateButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        updateButton.setEnabled(false);

                        username = dyndnsUserField.getText();
                        password = new String(dyndnsPasswordField.getPassword());
                        newDyndns = (String) mydnsndsModel.getValue();
                        if (dyndnsUserField.getText().equals("")) {
                            dyndnsUserField.grabFocus();
                        } else if (new String(dyndnsPasswordField.getPassword())
                            .equals(""))
                        {
                            dyndnsPasswordField.grabFocus();
                        }

                        if (!newDyndns.equals("")
                            && !dyndnsUserField.getText().equals("")
                            && !new String(dyndnsPasswordField.getPassword())
                                .equals(""))
                        {
                            // update
                            getController().getDynDnsManager().forceUpdate();
                            updatedIPField
                                .setText(ConfigurationEntry.DYNDNS_LAST_UPDATED_UP
                                    .getValue(getController()));
                        } else {
                            updateButton.setEnabled(false);
                            getController().getDynDnsManager()
                                .showPanelErrorMessage();
                        }
                        updateButton.setEnabled(true);
                    }
                }.start();
            }
        });
    }

    private boolean isUpdateSelected() {
        return ConfigurationEntry.DYNDNS_AUTO_UPDATE
            .getValueBoolean(getController()).booleanValue();
    }

    private JButton createUpdateButton(ActionListener listener) {
        updateButton = new JButton(Translation
            .getTranslation("preferences.dialog.dyndnsUpdateButton"));
        updateButton.addActionListener(listener);
        return updateButton;
    }
}
