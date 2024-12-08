package be.ehb.tristan.javaadvanced.internquest.services;

import be.ehb.tristan.javaadvanced.internquest.enums.Industry;
import be.ehb.tristan.javaadvanced.internquest.models.Company;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.ActivityRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.CompanyRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;

    public Company getCompanyById(Long id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found by id " + id));
    }

    public Company saveCompany(Company company){
        return companyRepository.save(company);
    }

    public void deleteCompanyById(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found by id " + companyId));

        company.getUsers().forEach(user -> {
            user.getCompanies().remove(company);
            userRepository.save(user);
        });

        company.getActivities().forEach(activity -> {
            activity.getCompanies().remove(company);
            activityRepository.save(activity);
        });

        companyRepository.flush();
        userRepository.flush();
        activityRepository.flush();

        companyRepository.delete(company);
    }

    public void validateCompanyInformation(Company company, Industry industry, User user){
        if(company.getNameOfCompany() == "" || company.getNameOfCompany() == null){
            throw new RuntimeException("Company name is empty");
        }
        if(company.getAddress().getStreetName() == "" || company.getAddress().getStreetName() == null){
            throw new RuntimeException("Street name is empty");
        }
        if(company.getAddress().getHouseNumber() <= 0){
            throw new RuntimeException("House number cannot be negative or zero");
        }
        if(company.getAddress().getPostalCode() <= 0){
            throw new RuntimeException("Postal code cannot be negative or zero");
        }
        if(company.getAddress().getPostalCode() > 9999)
            throw new RuntimeException("Postal code cannot exceed 9999");
        if(company.getAddress().getCity() == "" || company.getAddress().getCity() == null){
            throw new RuntimeException("City name is empty");
        }
        if(company.getAddress().getCountry() == "" || company.getAddress().getCountry() == null){
            throw new RuntimeException("Country name is empty");
        }
        if(company.getIndustry() == null){
            throw new RuntimeException("Industry is empty");
        }
        company.setIndustry(industry);

        if (user.getCompanies() == null) {
            user.setCompanies(new HashSet<>());
        }
        user.getCompanies().add(company);

        if(company.getUsers() == null) {
            company.setUsers(new HashSet<>());
        }
        company.getUsers().add(user);

        saveCompany(company);
    }

    public void validateCompanyInformation(Company changedCompany, Company company, Industry industry){
        if (changedCompany.getNameOfCompany() == "" || changedCompany.getNameOfCompany() == null){
            throw new RuntimeException("Company name is empty");
        }
        if(changedCompany.getAddress().getStreetName() == "" || changedCompany.getAddress().getStreetName() == null){
            throw new RuntimeException("Street name is empty");
        }
        if(changedCompany.getAddress().getHouseNumber() <= 0){
            throw new RuntimeException("House number cannot be negative or zero");
        }
        if(changedCompany.getAddress().getPostalCode() <= 0){
            throw new RuntimeException("Postal code cannot be equal to zero or under zero");
        }
        if (changedCompany.getAddress().getPostalCode() > 9999){
            throw new RuntimeException("Postal code cannot be greater than 9999");
        }
        if(changedCompany.getAddress().getCity() == "" || changedCompany.getAddress().getCity() == null){
            throw new RuntimeException("City is empty");
        }
        if(changedCompany.getAddress().getCountry() == "" || changedCompany.getAddress().getCountry() == null){
            throw new RuntimeException("Country is empty");
        }
        if(changedCompany.getIndustry() == null){
            throw new RuntimeException("Industry is empty");
        }

        company.setNameOfCompany(changedCompany.getNameOfCompany());
        company.setIndustry(industry);
        company.setDescription(changedCompany.getDescription());
        company.getAddress().setStreetName(changedCompany.getAddress().getStreetName());
        company.getAddress().setHouseNumber(changedCompany.getAddress().getHouseNumber());
        company.getAddress().setBusNumber(changedCompany.getAddress().getBusNumber());
        company.getAddress().setCity(changedCompany.getAddress().getCity());
        company.getAddress().setPostalCode(changedCompany.getAddress().getPostalCode());
        company.getAddress().setCountry(changedCompany.getAddress().getCountry());
        saveCompany(company);
    }
}