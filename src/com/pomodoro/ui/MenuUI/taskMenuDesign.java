package com.pomodoro.ui.MenuUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import java.awt.FlowLayout;

import com.pomodoro.logic.TaskManager;
import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;
import com.pomodoro.ui.TaskTableModel;
import com.pomodoro.ui.formUI.addFormUI;

public class taskMenuDesign extends JPanel {

    private TaskManager taskManager;
    private TaskTableModel tableModel;

    public taskMenuDesign(TaskManager taskManager) {
        this.taskManager = taskManager;

        setBounds(113, 5, 780, 551);
        setLayout(null);

        // แสดงข้อมูลในรูปแบบตาราง
        tableModel = new TaskTableModel(taskManager);
        tableModel.setBounds(10, 11, 725, 315);
        add(tableModel);

        // โหลดข้อมูลจากฐานข้อมูลขึ้นมาแสดงผลตอนเปิดโปรแกรม
        tableModel.reloadTable();

        // หน้าต่างแสดงรายละเอียดงานแบบเจาะจง
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBounds(10, 337, 724, 94);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));
        add(detailsPanel);
        detailsPanel.setLayout(null);

        // หัวข้อ
        JLabel nameHeader = new JLabel("ชื่องาน : ");
        nameHeader.setBounds(10, 17, 54, 24);
        detailsPanel.add(nameHeader);

        JLabel desHeader = new JLabel("คำอธิบาย :");
        desHeader.setBounds(10, 38, 69, 24);
        detailsPanel.add(desHeader);

        JLabel priHeader = new JLabel("ความสำคัญ : ");
        priHeader.setBounds(10, 61, 80, 24);
        detailsPanel.add(priHeader);

        JLabel cateHeader = new JLabel("หมวดหมู่ : ");
        cateHeader.setBounds(157, 61, 65, 24);
        detailsPanel.add(cateHeader);

        JLabel dueDateHeader = new JLabel("วันครบกำหนด : ");
        dueDateHeader.setBounds(341, 17, 97, 23);
        detailsPanel.add(dueDateHeader);

        JLabel notiHeader = new JLabel("แจ้งเตือน : ");
        notiHeader.setBounds(341, 38, 63, 24);
        detailsPanel.add(notiHeader);

        JLabel statusHeader = new JLabel("สถานะ : ");
        statusHeader.setBounds(341, 61, 53, 23);
        detailsPanel.add(statusHeader);

        // ส่วนแสดงข้อมูล
        JLabel nameTitle = new JLabel("");
        nameTitle.setBounds(68, 17, 250, 24);
        detailsPanel.add(nameTitle);

        JLabel desTitle = new JLabel("");
        desTitle.setBounds(83, 38, 235, 24);
        detailsPanel.add(desTitle);

        JLabel priTitle = new JLabel("");
        priTitle.setBounds(96, 61, 48, 24);
        detailsPanel.add(priTitle);

        JLabel cateTitle = new JLabel("");
        cateTitle.setBounds(226, 61, 92, 24);
        detailsPanel.add(cateTitle);

        JLabel dueDateTitle = new JLabel("");
        dueDateTitle.setBounds(443, 17, 259, 23);
        detailsPanel.add(dueDateTitle);

        JLabel notiTitle = new JLabel("");
        notiTitle.setBounds(413, 38, 286, 24);
        detailsPanel.add(notiTitle);

        JLabel statusTitle = new JLabel("");
        statusTitle.setBounds(398, 61, 301, 23);
        detailsPanel.add(statusTitle);

        // เมื่อกดเลือกที่งานระบบจะนำข้อมูลมาแสดงในหน้าต่างรายละเอียดด้านล่าง
        JTable rawTable = tableModel.getTable();
        rawTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = rawTable.getSelectedRow();
                    Task selected = tableModel.getTaskAt(selectedRow);
                    if (selected != null) {
                        nameTitle.setText(selected.getTitle());
                        if (selected.getDescription() != null) {
                            desTitle.setText(selected.getDescription());
                        } else {
                            desTitle.setText("-");
                        }

                        priTitle.setText(selected.getPriority());

                        if (selected.getCategory() != null) {
                            cateTitle.setText(selected.getCategory().getName());
                        } else {
                            cateTitle.setText("-");
                        }

                        if (selected.getDueDate() != null) {
                            dueDateTitle.setText(
                                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(selected.getDueDate()));
                        } else {
                            dueDateTitle.setText("-");
                        }

                        if (selected.getReminderTime() != null) {
                            notiTitle.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                                    .format(selected.getReminderTime()));
                        } else {
                            notiTitle.setText("-");
                        }
                        statusTitle.setText(TaskStatus.getDisplayLabel(selected.getStatus()));
                    }
                }
            }
        });

        // เมนูคลิกขวา
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuAddTask = new JMenuItem("➕ เพิ่มงานใหม่");
        JMenuItem menuEdit = new JMenuItem("✏️ แก้ไขงาน");
        JMenuItem menuTrash = new JMenuItem("🗑️ ย้ายไป Trash");
        JSeparator separator = new JSeparator();
        JMenuItem menuTodo = new JMenuItem("📌 To Do");
        JMenuItem menuInProg = new JMenuItem("🔄 In Progress");
        JMenuItem menuDone = new JMenuItem("✅ Done");

        popupMenu.add(menuAddTask);
        popupMenu.add(menuEdit);
        popupMenu.add(menuTrash);
        popupMenu.add(separator);
        popupMenu.add(menuTodo);
        popupMenu.add(menuInProg);
        popupMenu.add(menuDone);
        
        JSeparator separator2 = new JSeparator();
        popupMenu.add(separator2);
        
        javax.swing.JMenu menuSort = new javax.swing.JMenu("เรียงลำดับ...");
        JMenuItem sortNameItem = new JMenuItem("เรียงตามชื่องาน (A-Z)");
        JMenuItem sortPriItem = new JMenuItem("เรียงตามความสำคัญ (High-Low)");
        JMenuItem sortDateItem = new JMenuItem("เรียงตามวันที่ครบกำหนด");
        JMenuItem sortCateItem = new JMenuItem("เรียงตามหมวดหมู่");
        menuSort.add(sortNameItem);
        menuSort.add(sortPriItem);
        menuSort.add(sortDateItem);
        menuSort.add(sortCateItem);
        popupMenu.add(menuSort);

        // ดักจับการคลิกขวาบนบรรทัดในตารางของหน้างาน
        rawTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger())
                    showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                int row = rawTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    rawTable.setRowSelectionInterval(row, row);
                } else {
                    rawTable.clearSelection();
                }
                popupMenu.show(rawTable, e.getX(), e.getY());
            }
        });

        // ดักจับการคลิกขวาบนพื้นที่ว่างในตารางเพื่อให้แสดงเมนูขึ้นมาได้
        tableModel.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger())
                    handleViewportPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    handleViewportPopup(e);
            }

            private void handleViewportPopup(MouseEvent e) {
                // แปลงตำแหน่งคลิกให้ตรงกับตารางเพื่อหาว่ากำลังคลิกตรงรายการไหน
                java.awt.Point pt = javax.swing.SwingUtilities.convertPoint(
                        tableModel.getViewport(), e.getPoint(), rawTable);
                int row = rawTable.rowAtPoint(pt);
                if (row >= 0)
                    rawTable.setRowSelectionInterval(row, row);
                popupMenu.show(rawTable, pt.x, pt.y);
            }
        });

        // ผูกปุ่มเข้ากับคำสั่ง
        menuAddTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                java.awt.Frame pf = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(taskMenuDesign.this);
                new addFormUI(pf, taskManager, new Runnable() {
                    public void run() {
                        tableModel.reloadTable();
                    }
                }).setVisible(true);
            }
        });

        menuEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task t = getSelectedTask(rawTable);
                if (t == null) {
                    javax.swing.JOptionPane.showMessageDialog(null, "กรุณาเลือกงานที่ต้องการแก้ไข");
                    return;
                }
                java.awt.Frame pf = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(taskMenuDesign.this);
                new addFormUI(pf, taskManager, t, new Runnable() {
                    public void run() {
                        tableModel.reloadTable();
                        clearDetails(nameTitle, desTitle, priTitle, cateTitle, dueDateTitle, notiTitle, statusTitle);
                    }
                }).setVisible(true);
            }
        });

        menuTrash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task t = getSelectedTask(rawTable);
                if (t == null) {
                    javax.swing.JOptionPane.showMessageDialog(null, "กรุณาเลือกงานที่ต้องการย้ายลงถังขยะ");
                    return;
                }
                taskManager.moveToTrash(t);
                tableModel.reloadTable();
                clearDetails(nameTitle, desTitle, priTitle, cateTitle, dueDateTitle, notiTitle, statusTitle);
            }
        });

        menuTodo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task t = getSelectedTask(rawTable);
                if (t != null) {
                    t.setStatus(TaskStatus.TODO);
                    taskManager.updateTask(t);
                    tableModel.reloadTable();
                }
            }
        });

        menuInProg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task t = getSelectedTask(rawTable);
                if (t != null) {
                    t.setStatus(TaskStatus.IN_PROGRESS);
                    taskManager.updateTask(t);
                    tableModel.reloadTable();
                }
            }
        });

        menuDone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task t = getSelectedTask(rawTable);
                if (t != null) {
                    t.setStatus(TaskStatus.DONE);
                    taskManager.updateTask(t);
                    tableModel.reloadTable();
                }
            }
        });

        sortNameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskManager.sortByName();
                tableModel.reloadTable();
            }
        });

        sortPriItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskManager.sortByPriority();
                tableModel.reloadTable();
            }
        });

        sortDateItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskManager.sortByDueDate();
                tableModel.reloadTable();
            }
        });

        sortCateItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskManager.sortByCategory();
                tableModel.reloadTable();
            }
        });


        // ส่วนของฟอร์มกรอกรายละเอียดเวลาเพิ่มงาน
        final java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities
                .getWindowAncestor(taskMenuDesign.this);
        addFormUI addForm = new addFormUI(parentFrame, taskManager, new Runnable() {
            public void run() {
                tableModel.reloadTable();
            }
        });

        // พื้นที่วางปุ่มกดลงในหน้าต่าง
        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(10, 438, 725, 79);
        add(btnPanel);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));

        // ปุ่มสั่งการทั่วไป
        JPanel actionBtnPanel = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) actionBtnPanel.getLayout();
        flowLayout_1.setAlignment(FlowLayout.LEFT);
        btnPanel.add(actionBtnPanel);

        JButton addBtn = new JButton("เพิ่มงาน");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addForm.setVisible(true);
            }
        });
        actionBtnPanel.add(addBtn);

        JButton deleteBtn = new JButton("ลบงาน");
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task t = getSelectedTask(rawTable);
                if (t != null) {
                    taskManager.moveToTrash(t);
                    taskManager.updateTask(t);
                    tableModel.reloadTable();
                    clearDetails(nameTitle, desTitle, priTitle, cateTitle, dueDateTitle, notiTitle, statusTitle);
                }
            }
        });
        actionBtnPanel.add(deleteBtn);

        JButton exportBtn = new JButton("ส่งออก");
        actionBtnPanel.add(exportBtn);

        // ปุ่มจัดเรียงข้อมูลตามประเภท
        JPanel sortBtnPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) sortBtnPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        btnPanel.add(sortBtnPanel);

        JLabel sortLabel = new JLabel("Sort : ");
        sortBtnPanel.add(sortLabel);

        JButton sortNameBtn = new JButton("ชื่องาน");
        sortNameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskManager.sortByName();
                tableModel.reloadTable();
            }
        });
        sortBtnPanel.add(sortNameBtn);

        JButton sortPriBtn = new JButton("ความสำคัญ");
        sortPriBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskManager.sortByPriority();
                tableModel.reloadTable();
            }
        });
        sortBtnPanel.add(sortPriBtn);

        JButton sortDateBtn = new JButton("วันที่ครบกำหนด");
        sortDateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskManager.sortByDueDate();
                tableModel.reloadTable();
            }
        });
        sortBtnPanel.add(sortDateBtn);

        JButton sortCateBtn = new JButton("หมวดหมู่");
        sortCateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskManager.sortByCategory();
                tableModel.reloadTable();
            }
        });
        sortBtnPanel.add(sortCateBtn);

        // ย้ายคำสั่งตรวจสอบแจ้งเตือนงานไปยัง MainWindowDesign.java
        // แทนเพื่อตั้งรันเป็นพื้นหลัง
    }

    // ตัวช่วยอื่นๆ

    private Task getSelectedTask(JTable table) {
        int row = table.getSelectedRow();
        return tableModel.getTaskAt(row);
    }

    private void clearDetails(JLabel name, JLabel des, JLabel pri, JLabel cate,
            JLabel due, JLabel noti, JLabel status) {
        name.setText("");
        des.setText("");
        pri.setText("");
        cate.setText("");
        due.setText("");
        noti.setText("");
        status.setText("");
    }

    public void reloadTask() {
        tableModel.reloadTable();
    }
}
