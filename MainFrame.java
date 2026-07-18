package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    private JTextField nameField;
    private JTextField courseField;
    private JTextField studentIdField;

    private JTable studentTable;
    private JTable attendanceTable;

    private DefaultTableModel studentModel;
    private DefaultTableModel attendanceModel;

    public MainFrame() {

        setTitle(
                "Student Attendance Management System"
        );

        setSize(900, 600);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {

        JTabbedPane tabs =
                new JTabbedPane();

        tabs.addTab(
                "Student Management",
                createStudentPanel()
        );

        tabs.addTab(
                "Attendance",
                createAttendancePanel()
        );

        tabs.addTab(
                "Reports",
                createReportPanel()
        );

        add(tabs);
    }

    // ==============================
    // STUDENT PANEL
    // ==============================

    private JPanel createStudentPanel() {

        JPanel panel =
                new JPanel(new BorderLayout());

        JPanel formPanel =
                new JPanel(new GridLayout(3, 2, 10, 10));

        nameField =
                new JTextField();

        courseField =
                new JTextField();

        formPanel.add(
                new JLabel("Student Name:")
        );

        formPanel.add(nameField);

        formPanel.add(
                new JLabel("Course:")
        );

        formPanel.add(courseField);

        JButton addButton =
                new JButton("Add Student");

        JButton clearButton =
                new JButton("Clear");

        formPanel.add(addButton);

        formPanel.add(clearButton);

        panel.add(
                formPanel,
                BorderLayout.NORTH
        );

        studentModel =
                new DefaultTableModel();

        studentModel.addColumn("ID");
        studentModel.addColumn("Name");
        studentModel.addColumn("Course");

        studentTable =
                new JTable(studentModel);

        panel.add(
                new JScrollPane(studentTable),
                BorderLayout.CENTER
        );

        addButton.addActionListener(e -> {

            String name =
                    nameField.getText();

            String course =
                    courseField.getText();

            if (name.isEmpty()
                    || course.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields!"
                );

                return;
            }

            DataStore.addStudent(
                    name,
                    course
            );

            refreshStudentTable();

            nameField.setText("");

            courseField.setText("");

            JOptionPane.showMessageDialog(
                    this,
                    "Student added successfully!"
            );
        });

        clearButton.addActionListener(e -> {

            nameField.setText("");

            courseField.setText("");
        });

        return panel;
    }

    private void refreshStudentTable() {

        studentModel.setRowCount(0);

        for (Student student :
                DataStore.students) {

            studentModel.addRow(
                    new Object[]{
                            student.getId(),
                            student.getName(),
                            student.getCourse()
                    }
            );
        }
    }

    // ==============================
    // ATTENDANCE PANEL
    // ==============================

    private JPanel createAttendancePanel() {

        JPanel panel =
                new JPanel(new BorderLayout());

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                10,
                                10
                        )
                );

        studentIdField =
                new JTextField();

        JComboBox<String> statusBox =
                new JComboBox<>(
                        new String[]{
                                "Present",
                                "Absent"
                        }
                );

        formPanel.add(
                new JLabel("Student ID:")
        );

        formPanel.add(studentIdField);

        formPanel.add(
                new JLabel("Status:")
        );

        formPanel.add(statusBox);

        JButton markButton =
                new JButton(
                        "Mark Attendance"
                );

        JButton clearButton =
                new JButton("Clear");

        formPanel.add(markButton);

        formPanel.add(clearButton);

        panel.add(
                formPanel,
                BorderLayout.NORTH
        );

        attendanceModel =
                new DefaultTableModel();

        attendanceModel.addColumn(
                "Student ID"
        );

        attendanceModel.addColumn(
                "Student Name"
        );

        attendanceModel.addColumn(
                "Date"
        );

        attendanceModel.addColumn(
                "Status"
        );

        attendanceTable =
                new JTable(attendanceModel);

        panel.add(
                new JScrollPane(
                        attendanceTable
                ),
                BorderLayout.CENTER
        );

        markButton.addActionListener(e -> {

            try {

                int id =
                        Integer.parseInt(
                                studentIdField.getText()
                        );

                Student student =
                        DataStore.findStudent(id);

                if (student == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Student not found!"
                    );

                    return;
                }

                String status =
                        statusBox
                                .getSelectedItem()
                                .toString();

                Attendance attendance =
                        new Attendance(
                                id,
                                LocalDate.now(),
                                status
                        );

                DataStore.attendance.add(
                        attendance
                );

                refreshAttendanceTable();

                JOptionPane.showMessageDialog(
                        this,
                        "Attendance marked successfully!"
                );

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid Student ID!"
                );
            }
        });

        clearButton.addActionListener(e -> {

            studentIdField.setText("");
        });

        return panel;
    }

    private void refreshAttendanceTable() {

        attendanceModel.setRowCount(0);

        for (Attendance attendance :
                DataStore.attendance) {

            Student student =
                    DataStore.findStudent(
                            attendance.getStudentId()
                    );

            attendanceModel.addRow(
                    new Object[]{
                            attendance.getStudentId(),
                            student.getName(),
                            attendance.getDate(),
                            attendance.getStatus()
                    }
            );
        }
    }

    // ==============================
    // REPORT PANEL
    // ==============================

    private JPanel createReportPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        JButton reportButton =
                new JButton(
                        "Generate Attendance Report"
                );

        JTextArea reportArea =
                new JTextArea();

        reportArea.setEditable(false);

        panel.add(
                reportButton,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(
                        reportArea
                ),
                BorderLayout.CENTER
        );

        reportButton.addActionListener(e -> {

            StringBuilder report =
                    new StringBuilder();

            for (Student student :
                    DataStore.students) {

                int total = 0;

                int present = 0;

                for (Attendance attendance :
                        DataStore.attendance) {

                    if (
                            attendance.getStudentId()
                                    == student.getId()
                    ) {

                        total++;

                        if (
                                attendance
                                        .getStatus()
                                        .equals("Present")
                        ) {

                            present++;
                        }
                    }
                }

                double percentage = 0;

                if (total > 0) {

                    percentage =
                            (present * 100.0)
                                    / total;
                }

                report.append(
                        "Student ID: "
                ).append(
                        student.getId()
                ).append(
                        "\nName: "
                ).append(
                        student.getName()
                ).append(
                        "\nCourse: "
                ).append(
                        student.getCourse()
                ).append(
                        "\nAttendance: "
                ).append(
                        String.format(
                                "%.2f",
                                percentage
                        )
                ).append(
                        "%\n"
                ).append(
                        "--------------------------\n"
                );
            }

            reportArea.setText(
                    report.toString()
            );
        });

        return panel;
    }
}
