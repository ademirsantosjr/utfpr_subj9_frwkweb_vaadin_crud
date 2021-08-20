package org.vaadin.example.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {

    public List<Person> findByCpf(String cpf);

    public List<Person> findByEmail(String email);

    @Query(value = "from Person p where lower(p.fullName) like '%'||lower(?1)||'%'")
    public List<Person> findByFullName(String fullName);
}
