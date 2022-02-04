package database.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "person", schema = "public", catalog = "competition")
public class PersonEntity implements Serializable {
    private long id;
    private String username;
    private boolean type;
    private int idTeam;

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
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "type")
    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Basic
    @Column(name = "id_team")
    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return id == that.id && type == that.type && idTeam == that.idTeam && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, type, idTeam);
    }
}
