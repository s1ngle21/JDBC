package jdbc.entity;

import java.util.Objects;

public class Lesson {

    private Long id;
    private String name;
    private Homework homework;

    public Lesson() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(name, lesson.name) && Objects.equals(homework, lesson.homework);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, homework);
    }

    @Override
    public String toString() {
        return "jdbc.exceptions.Lesson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", homework='" + homework + '\'' +
                '}';
    }
}
