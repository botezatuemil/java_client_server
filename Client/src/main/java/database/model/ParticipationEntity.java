package database.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "participation", schema = "public", catalog = "competition")
public class ParticipationEntity implements Serializable {
    private long id;
    private Integer points;
    private long idPerson;
    private Boolean participating;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "points")
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Id
    @Column(name = "id_person")
    public long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(long idPerson) {
        this.idPerson = idPerson;
    }

    @Basic
    @Column(name = "participating")
    public Boolean getParticipating() {
        return participating;
    }

    public void setParticipating(Boolean participating) {
        this.participating = participating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationEntity that = (ParticipationEntity) o;
        return id == that.id && idPerson == that.idPerson && Objects.equals(points, that.points) && Objects.equals(participating, that.participating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, points, idPerson, participating);
    }
}
