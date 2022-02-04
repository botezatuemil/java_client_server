package database.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ParticipationEntityPK implements Serializable {
    private long id;
    private long idPerson;

    @Column(name = "id")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "id_person")
    @Id
    public long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(long idPerson) {
        this.idPerson = idPerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationEntityPK that = (ParticipationEntityPK) o;
        return id == that.id && idPerson == that.idPerson;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idPerson);
    }
}
