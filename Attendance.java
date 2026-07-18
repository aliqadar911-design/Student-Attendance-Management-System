package com.company;
import java.time.LocalDate;
public class Attendance {
    private int studentId;
    private LocalDate date;
    private String status;

    public Attendance(int studentId, LocalDate date, String status) {

        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    public int getStudentId() {
        return studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
