package com.company;



import java.util.ArrayList;
import java.util.List;

public class DataStore {

    public static List<Student> students =
            new ArrayList<>();

    public static List<Attendance> attendance =
            new ArrayList<>();

    private static int studentId = 1;

    public static void addStudent(
            String name,
            String course) {

        Student student =
                new Student(
                        studentId++,
                        name,
                        course
                );

        students.add(student);
    }

    // FIXED: return type is Student, not void
    public static Student findStudent(int id) {

        for (Student student : students) {

            if (student.getId() == id) {

                return student;
            }
        }

        return null;
    }
}

