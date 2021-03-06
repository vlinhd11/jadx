package jadx.gui.settings;

import jadx.gui.ui.MainWindow;
import jadx.gui.utils.NLS;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JadxSettingsWindow extends JDialog {
	private static final long serialVersionUID = -1804570470377354148L;

	private final MainWindow mainWindow;
	private final JadxSettings settings;
	private final String startSettings;

	private boolean needReload = false;

	public JadxSettingsWindow(MainWindow mainWindow, JadxSettings settings) {
		this.mainWindow = mainWindow;
		this.settings = settings;
		this.startSettings = JadxSettingsAdapter.makeString(settings);

		initUI();
	}

	private void initUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 10, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(makeDeobfuscationGroup());
		panel.add(makeOtherGroup());

		JButton saveBtn = new JButton(NLS.str("preferences.save"));
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				settings.sync();
				if (needReload) {
					mainWindow.reOpenFile();
				}
				dispose();
			}
		});
		JButton cancelButton = new JButton(NLS.str("preferences.cancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JadxSettingsAdapter.fill(settings, startSettings);
				dispose();
			}
		});

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(saveBtn);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);

		Container contentPane = getContentPane();
		contentPane.add(panel, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		getRootPane().setDefaultButton(saveBtn);

		setTitle(NLS.str("preferences.title"));
		setSize(400, 550);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
	}

	private SettingsGroup makeDeobfuscationGroup() {

		JCheckBox deobfOn = new JCheckBox();
		deobfOn.setSelected(settings.isDeobfuscationOn());
		deobfOn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				settings.setDeobfuscationOn(e.getStateChange() == ItemEvent.SELECTED);
				needReload();
			}
		});

		JCheckBox deobfForce = new JCheckBox();
		deobfForce.setSelected(settings.isDeobfuscationForceSave());
		deobfForce.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				settings.setDeobfuscationForceSave(e.getStateChange() == ItemEvent.SELECTED);
				needReload();
			}
		});

		final JSpinner minLen = new JSpinner();
		minLen.setValue(settings.getDeobfuscationMinLength());
		minLen.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				settings.setDeobfuscationMinLength((Integer) minLen.getValue());
				needReload();
			}
		});

		final JSpinner maxLen = new JSpinner();
		maxLen.setValue(settings.getDeobfuscationMaxLength());
		maxLen.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				settings.setDeobfuscationMaxLength((Integer) maxLen.getValue());
				needReload();
			}
		});

		SettingsGroup deobfGroup = new SettingsGroup(NLS.str("preferences.deobfuscation"));
		deobfGroup.addRow(NLS.str("preferences.deobfuscation_on"), deobfOn);
		deobfGroup.addRow(NLS.str("preferences.deobfuscation_force"), deobfForce);
		deobfGroup.addRow(NLS.str("preferences.deobfuscation_min_len"), minLen);
		deobfGroup.addRow(NLS.str("preferences.deobfuscation_max_len"), maxLen);
		deobfGroup.end();
		return deobfGroup;
	}

	private SettingsGroup makeOtherGroup() {
		JCheckBox update = new JCheckBox();
		update.setSelected(settings.isCheckForUpdates());
		update.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				settings.setCheckForUpdates(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		JCheckBox fallback = new JCheckBox();
		fallback.setSelected(settings.isFallbackMode());
		fallback.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				settings.setFallbackMode(e.getStateChange() == ItemEvent.SELECTED);
				needReload();
			}
		});

		JCheckBox showInconsistentCode = new JCheckBox();
		showInconsistentCode.setSelected(settings.isShowInconsistentCode());
		showInconsistentCode.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				settings.setShowInconsistentCode(e.getStateChange() == ItemEvent.SELECTED);
				needReload();
			}
		});

		JCheckBox resourceDecode = new JCheckBox();
		resourceDecode.setSelected(settings.isSkipResources());
		resourceDecode.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				settings.setSkipResources(e.getStateChange() == ItemEvent.SELECTED);
				needReload();
			}
		});

		final JSpinner threadsCount = new JSpinner();
		threadsCount.setValue(settings.getThreadsCount());
		threadsCount.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				settings.setThreadsCount((Integer) threadsCount.getValue());
			}
		});

		JCheckBox cfg = new JCheckBox();
		cfg.setSelected(settings.isCFGOutput());
		cfg.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				settings.setCfgOutput(e.getStateChange() == ItemEvent.SELECTED);
				needReload();
			}
		});

		JCheckBox rawCfg = new JCheckBox();
		rawCfg.setSelected(settings.isRawCFGOutput());
		rawCfg.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				settings.setRawCfgOutput(e.getStateChange() == ItemEvent.SELECTED);
				needReload();
			}
		});

		SettingsGroup other = new SettingsGroup(NLS.str("preferences.other"));
		other.addRow(NLS.str("preferences.check_for_updates"), update);
		other.addRow(NLS.str("preferences.threads"), threadsCount);
		other.addRow(NLS.str("preferences.fallback"), fallback);
		other.addRow(NLS.str("preferences.showInconsistentCode"), showInconsistentCode);
		other.addRow(NLS.str("preferences.skipResourcesDecode"), resourceDecode);
		other.addRow(NLS.str("preferences.cfg"), cfg);
		other.addRow(NLS.str("preferences.raw_cfg"), rawCfg);
		return other;
	}

	private void needReload() {
		needReload = true;
	}

	private static class SettingsGroup extends JPanel {
		private static final long serialVersionUID = -6487309975896192544L;

		private final GridBagConstraints c;
		private int row;

		public SettingsGroup(String title) {
			setBorder(BorderFactory.createTitledBorder(title));
			setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.insets = new Insets(5, 5, 5, 5);
			c.weighty = 1.0;
		}

		public void addRow(String label, JComponent comp) {
			c.gridy = row++;
			JLabel jLabel = new JLabel(label);
			jLabel.setLabelFor(comp);
			jLabel.setHorizontalAlignment(SwingConstants.LEFT);
			c.gridx = 0;
			c.gridwidth = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.weightx = 0.8;
			c.fill = GridBagConstraints.NONE;
			add(jLabel, c);
			c.gridx = 1;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.anchor = GridBagConstraints.CENTER;
			c.weightx = 0.2;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(comp, c);
		}

		public void end() {
			add(Box.createVerticalGlue());
		}
	}
}
