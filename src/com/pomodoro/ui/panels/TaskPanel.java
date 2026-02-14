package com.pomodoro.ui.panels;

import com.pomodoro.logic.TaskManager;
import com.pomodoro.model.Category;
import com.pomodoro.model.Priority;
import com.pomodoro.model.SingleTask;
import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;
import com.pomodoro.ui.Theme;
import com.pomodoro.ui.components.TableRenderers;
import com.pomodoro.ui.components.TailwindButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TaskPanel extends JPanel {
    private TaskManager taskManager;
    private JTable table;
    private DefaultTableModel model;
    private String currentFilter = "ALL";

    private Consumer<Task> onTaskSelected;

    public TaskPanel(TaskManager tm, Consumer<Task> onTaskSelected) {
        this.taskManager = tm;
        this.onTaskSelected = onTaskSelected;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_APP);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Theme.BG_APP);
        toolbar.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel title = new JLabel("My Tasks");
        title.setFont(Theme.FONT_Header);
        toolbar.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(Theme.BG_APP);

        TailwindButton sortBtn = new TailwindButton("⇅ Sort", Theme.BG_WHITE, Theme.TEXT_PRIMARY, true);
        sortBtn.addActionListener(e -> showSortMenu(sortBtn));

        TailwindButton addBtn = new TailwindButton("+ Add Task", Theme.BLUE_600, Color.WHITE, false);
        addBtn.addActionListener(e -> openTaskDialog(null));

        actions.add(sortBtn);
        actions.add(addBtn);
        toolbar.add(actions, BorderLayout.EAST);
        add(toolbar, BorderLayout.NORTH);

        // Table
        setupTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(Theme.BORDER_COLOR));
        scroll.getViewport().setBackground(Theme.BG_WHITE);
        add(scroll, BorderLayout.CENTER);
    }

    private void setupTable() {
        String[] cols = { "#", "Status", "Task Title", "Priority", "Due Date", "Time" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(56);
        table.setFont(Theme.FONT_Body);
        table.setShowVerticalLines(false);
        table.setGridColor(Theme.BG_APP);
        table.setSelectionBackground(Theme.BLUE_50);
        table.setSelectionForeground(Theme.BLUE_600);

        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setMaxWidth(40);
        cm.getColumn(1).setMinWidth(110);
        cm.getColumn(1).setMaxWidth(110);
        cm.getColumn(3).setMaxWidth(100);
        cm.getColumn(4).setMinWidth(140);
        cm.getColumn(4).setMaxWidth(180);
        cm.getColumn(5).setMaxWidth(80);

        cm.getColumn(0).setCellRenderer(new TableRenderers.IndexRenderer());
        cm.getColumn(1).setCellRenderer(new TableRenderers.StatusRenderer());
        cm.getColumn(3).setCellRenderer(new TableRenderers.PriorityRenderer());
        cm.getColumn(4).setCellRenderer(new TableRenderers.DateRenderer());
        cm.getColumn(5).setCellRenderer(new TableRenderers.TimeRenderer());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                Task t = getTaskAtRow(table.getSelectedRow());
                if (onTaskSelected != null)
                    onTaskSelected.accept(t);
            }
        });

        // Popup Menu
        JPopupMenu popup = new JPopupMenu();
        JMenuItem doneItem = new JMenuItem("✅ Mark as Done");
        JMenuItem editItem = new JMenuItem("✏️ Edit");
        JMenuItem delItem = new JMenuItem("🗑️ Delete");

        doneItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Task t = getTaskAtRow(row);
                if (t != null) {
                    t.setStatus(TaskStatus.DONE);
                    refreshTable();
                }
            }
        });
        editItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1)
                openTaskDialog(getTaskAtRow(row));
        });
        delItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Task t = getTaskAtRow(row);
                if (t != null) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Delete task '" + t.getTitle() + "'?", "Confirm",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        taskManager.removeTask(t);
                        refreshTable();
                        if (onTaskSelected != null)
                            onTaskSelected.accept(null); // Clear selection
                    }
                }
            }
        });

        popup.add(doneItem);
        popup.add(editItem);
        popup.addSeparator();
        popup.add(delItem);
        table.setComponentPopupMenu(popup);

        refreshTable();
    }

    public void setFilter(String filter) {
        this.currentFilter = filter;
        refreshTable();
    }

    public void refreshTable() {

        model.setRowCount(0);
        List<Task> list = getFilteredTasks();
        for (int i = 0; i < list.size(); i++) {
            Task t = list.get(i);
            String timeStr = t.getTotalFocusMinutes() + " min";
            model.addRow(new Object[] { (i + 1), t.getStatus(), t.getTitle(), t.getPriority(), t, timeStr });
        }
    }

    private List<Task> getFilteredTasks() {
        return taskManager.getAllTasks().stream().filter(t -> {
            if (currentFilter.equals("ALL"))
                return t.getStatus() != TaskStatus.DONE;
            if (currentFilter.equals("TODAY")) {
                if (t.getDueDate() == null)
                    return false;
                java.util.Calendar now = java.util.Calendar.getInstance();
                java.util.Calendar due = java.util.Calendar.getInstance();
                due.setTime(t.getDueDate());
                return now.get(java.util.Calendar.YEAR) == due.get(java.util.Calendar.YEAR) &&
                        now.get(java.util.Calendar.DAY_OF_YEAR) == due.get(java.util.Calendar.DAY_OF_YEAR) &&
                        t.getStatus() != TaskStatus.DONE;
            }
            if (currentFilter.equals("SCHEDULED"))
                return t.getDueDate() != null && t.getStatus() != TaskStatus.DONE;
            if (currentFilter.equals("COMPLETED"))
                return t.getStatus() == TaskStatus.DONE;
            return true;
        }).collect(Collectors.toList());
    }

    private Task getTaskAtRow(int row) {
        List<Task> list = getFilteredTasks();
        if (row >= 0 && row < list.size()) {
            return list.get(row);
        }
        return null;
    }

    private void showSortMenu(Component invoker) {
        JPopupMenu m = new JPopupMenu();
        m.add(new JMenuItem("Name (A-Z)")).addActionListener(e -> {
            taskManager.sortByName();
            refreshTable();
        });
        m.add(new JMenuItem("Priority (High)")).addActionListener(e -> {
            taskManager.sortByPriority();
            refreshTable();
        });
        m.add(new JMenuItem("Due Date (Near First)")).addActionListener(e -> {
            taskManager.sortByDueDate();
            refreshTable();
        });
        m.show(invoker, 0, invoker.getHeight());
    }

    private void openTaskDialog(Task existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                existing == null ? "New Task" : "Edit Task", true);
        dlg.setSize(500, 550);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BG_WHITE);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.gridx = 0;
        gbc.weightx = 1;

        // Title
        p.add(new JLabel("Task Title"), gbc);
        JTextField titleF = new JTextField(existing != null ? existing.getTitle() : "");
        titleF.setBorder(Theme.INPUT_BORDER);
        gbc.gridy = 1;
        p.add(titleF, gbc);

        // Priority & Category
        JPanel row2 = new JPanel(new GridLayout(1, 2, 10, 0));
        row2.setBackground(Theme.BG_WHITE);

        JComboBox<Priority> priBox = new JComboBox<>(Priority.values());
        if (existing != null)
            priBox.setSelectedItem(existing.getPriority());

        List<Category> cats = taskManager.getCategories();
        JComboBox<Category> catBox = new JComboBox<>(cats.toArray(new Category[0]));
        if (existing != null)
            catBox.setSelectedItem(existing.getCategory());

        row2.add(wrapField("Priority", priBox));
        row2.add(wrapField("Category", catBox));
        gbc.gridy = 2;
        p.add(row2, gbc);

        // Due Date
        gbc.gridy = 3;
        p.add(new JLabel("Due Date"), gbc);

        JSpinner dateSpin = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de = new JSpinner.DateEditor(dateSpin, "yyyy-MM-dd HH:mm");
        dateSpin.setEditor(de);
        if (existing != null && existing.getDueDate() != null)
            dateSpin.setValue(existing.getDueDate());
        else if (existing == null)
            dateSpin.setValue(new java.util.Date());

        gbc.gridy = 4;
        p.add(dateSpin, gbc);

        // Description
        gbc.gridy = 5;
        p.add(new JLabel("Description"), gbc);
        JTextArea descIn = new JTextArea(4, 20);
        descIn.setLineWrap(true);
        if (existing != null)
            descIn.setText(existing.getDescription());
        JScrollPane descScroll = new JScrollPane(descIn);
        descScroll.setBorder(Theme.INPUT_BORDER);
        gbc.gridy = 6;
        p.add(descScroll, gbc);

        // Save Button
        TailwindButton save = new TailwindButton("Save Task", Theme.BLUE_600, Color.WHITE, false);
        save.addActionListener(e -> {
            if (titleF.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Title is required!");
                return;
            }

            if (existing == null) {
                Task t = new SingleTask(titleF.getText(), (Priority) priBox.getSelectedItem(),
                        (Category) catBox.getSelectedItem());
                t.setDueDate((java.util.Date) dateSpin.getValue());
                t.setDescription(descIn.getText());
                taskManager.addTask(t);
            } else {
                existing.setTitle(titleF.getText());
                existing.setPriority((Priority) priBox.getSelectedItem());
                existing.setCategory((Category) catBox.getSelectedItem());
                existing.setDueDate((java.util.Date) dateSpin.getValue());
                existing.setDescription(descIn.getText());
            }
            refreshTable();
            dlg.dispose();
        });

        gbc.gridy = 7;
        gbc.insets = new Insets(20, 0, 0, 0);
        p.add(save, gbc);

        dlg.add(p);
        dlg.setVisible(true);
    }

    private JPanel wrapField(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_WHITE);
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }
}
