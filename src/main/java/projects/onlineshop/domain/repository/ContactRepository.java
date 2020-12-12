package projects.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.onlineshop.domain.model.Contact;

public interface ContactRepository extends JpaRepository<Contact,Long> {


}
