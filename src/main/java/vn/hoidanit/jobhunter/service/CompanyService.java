package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO.Meta;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleSaveUser(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO getAllCompanies(Specification spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());
        ResultPaginationDTO res = new ResultPaginationDTO();
        res.setMeta(meta);
        res.setResult(pageCompany.getContent());
        return res;
    }

    public Company getCompanyById(long id) {
        Optional<Company> optionalCompany = this.companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        return null;
    }

    public Company handleUpdateCompany(Company updatedCompany) {
        Company company = this.getCompanyById(updatedCompany.getId());
        if (company != null) {
            company.setAddress(updatedCompany.getAddress());
            company.setDescription(updatedCompany.getDescription());
            company.setLogo(updatedCompany.getLogo());
            company.setName(updatedCompany.getName());
            company = this.companyRepository.save(company);
        }
        return company;
    }

    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
