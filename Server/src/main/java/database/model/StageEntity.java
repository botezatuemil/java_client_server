package database.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "stage", schema = "public", catalog = "competition")
public class StageEntity implements Serializable {
    private long id;
    private String name;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageEntity that = (StageEntity) o;
        return id == that.id && name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
