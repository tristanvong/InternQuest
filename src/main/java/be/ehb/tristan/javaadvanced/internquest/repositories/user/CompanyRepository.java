package be.ehb.tristan.javaadvanced.internquest.repositories.user;

import be.ehb.tristan.javaadvanced.internquest.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findCompanyByNameOfCompany(String nameOfCompany);
}
