/*
 This file is part of BORG.

 BORG is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 BORG is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with BORG; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Copyright 2003 by Mike Berger
 */

package net.sf.borg.ui.task;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sf.borg.common.Errmsg;
import net.sf.borg.common.Resource;
import net.sf.borg.model.TaskModel;
import net.sf.borg.model.TaskTypes;
import net.sf.borg.model.beans.Project;
import net.sf.borg.ui.ResourceHelper;

/**
 * 
 * @author MBERGER
 * @version
 */

// task tracker main window
// this view shows a list of tasks in a table format with all kinds
// of sorting/filtering options. It is really like the "main" window
// for a whole task traking application separate from the calendar
// application. In prior non-java versions of BORG, the task tracker
// and calendar apps were completely separate apps.
public class TaskFilterPanel extends JPanel  {

	
	private JCheckBox caseBox = null;

	private JPanel jPanel2 = null;

	private javax.swing.JTextField jTextField3;

	JComboBox projectBox = null;

	private JComboBox statusBox = null;

	private JLabel statusLabel = null;

	private TaskListPanel taskList = null;
	
	public TaskFilterPanel() {
		super();
		
		try {
			initComponents();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Errmsg.errmsg(e);
			return;
		}

	}

	// get the filter string typed by the user
	private String filter() {
		return (jTextField3.getText());
	}

	
	/**
	 * This method initializes caseBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCaseBox() {
		if (caseBox == null) {
			caseBox = new JCheckBox();
			caseBox.setText(Resource.getResourceString("case_sensitive"));
		}
		return caseBox;
	}



	private JPanel getJPanel2() throws Exception {
		if (jPanel2 == null) {

			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			statusLabel = new JLabel();
			statusLabel
					.setText(Resource.getPlainResourceString("Status") + ":");
			jPanel2 = new JPanel();
			jPanel2.setLayout(flowLayout);
			jPanel2.add(statusLabel, null);
			jPanel2.add(getStatusBox(), null);
			JLabel plabel = new JLabel(Resource
					.getPlainResourceString("project")
					+ ":");
			JLabel spacer = new JLabel("           ");
			jPanel2.add(spacer, null);
			jPanel2.add(plabel, null);
			jPanel2.add(getProjectBox());
		}
		return jPanel2;
	}



	private JComboBox getProjectBox() throws Exception {
		if (projectBox == null) {
			projectBox = new JComboBox();
			loadProjectBox();
			projectBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					refresh();
				}
			});
		}
		return projectBox;
	}

	
	/**
	 * This method initializes statusBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getStatusBox() {
		if (statusBox == null) {
			statusBox = new JComboBox();
			setStatuses(statusBox);
			statusBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					refresh();
				}
			});
		}
		return statusBox;
	}

	
	

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the FormEditor.
	 */

	private void initComponents() throws Exception {

		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.gridy = 4;
		
		JScrollPane jScrollPane1 = new JScrollPane();
		
		JButton jButton21 = new JButton();
		jTextField3 = new javax.swing.JTextField();
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 4;
		gridBagConstraints2.gridy = 1;

		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();

		this.setLayout(new GridBagLayout());
		gridBagConstraints8.gridx = 2;
		gridBagConstraints8.gridy = 1;
		gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints8.insets = new java.awt.Insets(4, 4, 4, 4);
		gridBagConstraints9.gridx = 3;
		gridBagConstraints9.gridy = 1;
		gridBagConstraints9.weightx = 1.0;
		gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints9.gridwidth = 1;
		gridBagConstraints9.insets = new java.awt.Insets(4, 4, 4, 4);
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 3;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints11.gridwidth = 5;
		gridBagConstraints11.insets = new java.awt.Insets(4, 4, 4, 4);
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.gridy = 0;
		gridBagConstraints15.gridwidth = 5;
		gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;

		jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/resource/Find16.gif")));
		ResourceHelper.setText(jButton21, "Filter:");
		jButton21.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton21ActionPerformed(evt);
			}
		});

		this.add(jButton21, gridBagConstraints8);
		this.add(jTextField3, gridBagConstraints9);
		this.add(jScrollPane1, gridBagConstraints11);
		this.add(getJPanel2(), gridBagConstraints15);

		this.add(getCaseBox(), gridBagConstraints2);
		
		taskList = new TaskListPanel();
		
		jScrollPane1.setViewportView(taskList);

		refresh();

	}

	private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {
		// just call refresh when filter button pressed
		refresh();
	}

	private void loadProjectBox() throws Exception {
		projectBox.removeAllItems();
		projectBox.addItem(Resource.getPlainResourceString("All"));
		try {
			Collection<Project> projects = TaskModel.getReference().getProjects();
			Iterator<Project> pi = projects.iterator();
			while (pi.hasNext()) {
				Project p = pi.next();
				projectBox.addItem(TaskView.getProjectString(p));
			}
		}
		// ignore exception if projects not supported
		catch (Exception e) {
		}
	}

	public void print() {

		// print the current table of tasks
		try {
			taskList.print();
		} catch (Exception e) {
			Errmsg.errmsg(e);
		}
	}

	
	// refresh is called to update the table of shown tasks due to model
	// changes
	// or if the user
	// changes the filtering criteria
	public void refresh() {

		
		// reload project filter
		Object o = projectBox.getSelectedItem();
		try {
			loadProjectBox();
		} catch (Exception e1) {
			Errmsg.errmsg(e1);
			return;
		}
		if (o != null)
			projectBox.setSelectedItem(o);

		
		// get any filter string the user has typed
		String filt = filter();

		String statfilt = (String) statusBox.getSelectedItem();

		String projfilt = (String) projectBox.getSelectedItem();
		
		
		
		taskList.setFilterCriteria(projfilt, statfilt, filt, caseBox.isSelected());
		taskList.refresh();
	
	}

	private void setStatuses(JComboBox s) {

		s.addItem(Resource.getPlainResourceString("All_Open"));
		s.addItem(Resource.getPlainResourceString("All"));
		TaskTypes t = TaskModel.getReference().getTaskTypes();
		TreeSet<String> ts = new TreeSet<String>();
		Vector<String> types = t.getTaskTypes();
		Iterator<String> it = types.iterator();
		while (it.hasNext()) {
			String type = it.next();
			Collection<String> states = t.getStates(type);
			Iterator<String> it2 = states.iterator();
			while (it2.hasNext()) {
				ts.add(it2.next());
			}
		}
		it = ts.iterator();
		while (it.hasNext()) {
			s.addItem(it.next());
		}
	}



	public void showTasksForProject(Project p) {

		statusBox.setSelectedIndex(0);
		String ps = TaskView.getProjectString(p);
		projectBox.setSelectedItem(ps);
		refresh();

	}
}
