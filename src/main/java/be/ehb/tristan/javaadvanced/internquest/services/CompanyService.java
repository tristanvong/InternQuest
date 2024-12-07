package be.ehb.tristan.javaadvanced.internquest.services;

import be.ehb.tristan.javaadvanced.internquest.models.Company;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company getCompanyById(Long id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found by id " + id));
    }

    public Company saveCompany(Company company){
        return companyRepository.save(company);
    }
}