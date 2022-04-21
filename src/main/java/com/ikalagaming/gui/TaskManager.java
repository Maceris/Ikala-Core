package com.ikalagaming.gui;

import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.plugins.PluginState;

import lombok.Getter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Displays various information about the program.
 *
 * @author Ches Burks
 *
 */
public class TaskManager extends JFrame {

	private static class Updater extends Thread {
		TaskManager owner;

		public Updater(TaskManager manager) {
			this.setName("TaskMgrUpdateThread");
			this.owner = manager;
		}

		@Override
		public void run() {
			while (true) {
				this.owner.tick();
				try {
					Thread.sleep(this.owner.getDelay());
				}
				catch (InterruptedException e) {
					e.printStackTrace();
					// Re-interrupt as per SonarLint java:S2142
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private static final String DISABLE = "Disable";
	private static final String ENABLE = "Enable";
	private static final String RELOAD = "Reload";

	private static final long serialVersionUID = -4427516209866980363L;

	private static final String UNLOAD = "Unload";

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					this.showMenu(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					this.showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());

				if (e.getSource() instanceof JTable) {
					JTable source = (JTable) e.getSource();
					int row = source.rowAtPoint(e.getPoint());
					int column = source.columnAtPoint(e.getPoint());

					if (!source.isRowSelected(row)) {
						source.changeSelection(row, column, false, false);
					}
				}
			}
		});
	}

	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel contentPane;
	/**
	 * The time to wait between plugin status updates in milliseconds
	 *
	 * @return The delay in ms between updates
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private long delay = 1000;
	private JLabel memUsage;
	private DefaultTableModel model;
	private transient PluginManager pluginManager;
	private Map<String, PluginState> plugins = new HashMap<>();
	private JTable table;
	private JLabel threads;

	private int tickCount = 0;// counts down to refreshing plugin list

	/**
	 * Create the frame.
	 *
	 * @param pluginMgr the plugin manager to list plugins for
	 *
	 */
	public TaskManager(PluginManager pluginMgr) {
		this.pluginManager = pluginMgr;
		this.setTitle("Task Manager");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 465);

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewPlugin = new JMenuItem("New Plugin");
		mnFile.add(mntmNewPlugin);

		JMenuItem mntmExitTaskManager = new JMenuItem("Exit Task Manager");
		mnFile.add(mntmExitTaskManager);

		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);

		JMenuItem mntmUpdateNow = new JMenuItem("Update now");
		mnView.add(mntmUpdateNow);

		JMenu mnUpdateSpeed = new JMenu("Update Speed");
		mnView.add(mnUpdateSpeed);

		JRadioButtonMenuItem rdbtnmntmHigh = new JRadioButtonMenuItem("High");
		rdbtnmntmHigh.addItemListener(arg0 -> {
			if (arg0.getStateChange() == ItemEvent.SELECTED) {
				TaskManager.this.delay = 500;
			}
		});
		this.buttonGroup.add(rdbtnmntmHigh);
		mnUpdateSpeed.add(rdbtnmntmHigh);

		JRadioButtonMenuItem rdbtnmntmNormal =
			new JRadioButtonMenuItem("Normal");
		rdbtnmntmNormal.setSelected(true);
		rdbtnmntmNormal.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				TaskManager.this.delay = 1000;
			}
		});
		this.buttonGroup.add(rdbtnmntmNormal);
		mnUpdateSpeed.add(rdbtnmntmNormal);

		JRadioButtonMenuItem rdbtnmntmLow = new JRadioButtonMenuItem("Low");
		rdbtnmntmLow.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				TaskManager.this.delay = 2000;
			}
		});
		this.buttonGroup.add(rdbtnmntmLow);
		mnUpdateSpeed.add(rdbtnmntmLow);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		this.contentPane.add(tabbedPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Plugins", null, scrollPane, null);

		this.table = new JTable();
		this.table.setModel(new DefaultTableModel(new Object[][] {},
			new String[] {"Plugin Name", "Status"}));
		this.table.getColumnModel().getColumn(0).setPreferredWidth(102);

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem mntmEnable = new JMenuItem(TaskManager.ENABLE);
		mntmEnable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				TaskManager.this.changeState(TaskManager.ENABLE);
			}
		});
		popupMenu.add(mntmEnable);

		JMenuItem mntmDisable = new JMenuItem(TaskManager.DISABLE);
		mntmDisable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				TaskManager.this.changeState(TaskManager.DISABLE);
			}
		});
		popupMenu.add(mntmDisable);

		JMenuItem mntmUnload = new JMenuItem(TaskManager.UNLOAD);
		mntmUnload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				TaskManager.this.changeState(TaskManager.UNLOAD);
			}
		});
		popupMenu.add(mntmUnload);

		JMenuItem mntmReload = new JMenuItem(TaskManager.RELOAD);
		mntmReload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				TaskManager.this.changeState(TaskManager.RELOAD);
			}
		});
		popupMenu.add(mntmReload);

		TaskManager.addPopup(this.table, popupMenu);

		scrollPane.setViewportView(this.table);

		JPanel statsPanel = new JPanel();
		this.contentPane.add(statsPanel, BorderLayout.SOUTH);
		statsPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel lblMemoryUsage = new JLabel("Memory Usage:");
		statsPanel.add(lblMemoryUsage);

		this.memUsage = new JLabel("0");
		statsPanel.add(this.memUsage);

		JLabel lblThreads = new JLabel("Threads:");
		statsPanel.add(lblThreads);

		this.threads = new JLabel("0");
		statsPanel.add(this.threads);
		this.model = (DefaultTableModel) this.table.getModel();

		Updater update = new Updater(this);
		update.start();
	}

	/**
	 * Attempts to change the status of the currently selected plugin. This may
	 * be any of the following:
	 * <ul>
	 * <li>Enable</li>
	 * <li>Disable</li>
	 * <li>Load</li>
	 * <li>Unload</li>
	 * </ul>
	 *
	 * @param change the change to make
	 */
	public void changeState(String change) {
		int row = this.table.getSelectedRow();
		int column = -1;
		for (int i = 0; i < this.table.getColumnCount(); ++i) {
			if (this.table.getColumnName(i).equals("Plugin Name")) {
				column = i;
			}
		}
		if (row == -1 || column == -1) {
			return;
		}
		String pack = this.table.getValueAt(row, column).toString();
		if (TaskManager.ENABLE.equals(change)) {
			if (!this.pluginManager.isEnabled(pack)) {
				this.pluginManager.enable(pack);
			}
		}
		else if (TaskManager.DISABLE.equals(change)) {
			if (this.pluginManager.isEnabled(pack)) {
				this.pluginManager.disable(pack);
			}
		}
		else if (TaskManager.UNLOAD.equals(change)) {
			this.pluginManager.unloadPlugin(pack);
		}
		else if (TaskManager.RELOAD.equals(change)) {
			this.pluginManager.reload(pack);
		}

	}

	/**
	 * Updates and displays information about the program
	 */
	public void tick() {
		if (this.tickCount == 0) {
			this.updatePluginNames();
			this.tickCount = 10;
		}
		--this.tickCount;

		PluginState currentState;
		String name = "";
		for (int i = 0; i < this.model.getRowCount(); ++i) {
			name = (String) this.model.getValueAt(i, 0);
			currentState = this.pluginManager.getPluginState(name);
			if (!this.model.getValueAt(i, 1).equals(currentState)) {
				this.model.setValueAt(currentState, i, 1);
			}
		}
		this.threads.setText(java.lang.Thread.activeCount() + "");

		long memUsed = (Runtime.getRuntime().totalMemory()
			- Runtime.getRuntime().freeMemory()) / 1024;
		long percentUsed = (Runtime.getRuntime().totalMemory()
			- Runtime.getRuntime().freeMemory()) * 100
			/ Runtime.getRuntime().totalMemory();

		this.memUsage.setText(memUsed + " kb (" + percentUsed + "%)");
	}

	private void updatePluginNames() {
		Set<String> pluginNames =
			this.pluginManager.getLoadedPlugins().keySet();
		for (String s : pluginNames) {

			this.plugins.computeIfAbsent(s, name -> {
				PluginState state = this.pluginManager.getPluginState(name);
				this.model.addRow(new Object[] {s, state});
				return state;
			});

		}
		for (String s : this.plugins.keySet()) {
			if (!pluginNames.contains(s)) {
				for (int i = 0; i < this.model.getRowCount(); ++i) {
					if (this.model.getValueAt(i, 0).equals(s)) {
						this.model.removeRow(i);
						break;
					}
				}
			}
		}
		this.plugins.entrySet()
			.removeIf(entry -> !pluginNames.contains(entry.getKey()));
	}
}
