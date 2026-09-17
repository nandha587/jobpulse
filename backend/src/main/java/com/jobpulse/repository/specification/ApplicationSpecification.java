package com.jobpulse.repository.specification;

import com.jobpulse.entity.Application;
import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.Priority;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ApplicationSpecification {

    public static Specification<Application> filter(
            Long userId,
            String search,
            ApplicationStatus status,
            Priority priority,
            String location) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                Predicate companyMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("companyName")), pattern);
                Predicate titleMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("jobTitle")), pattern);
                predicates.add(criteriaBuilder.or(companyMatch, titleMatch));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            if (StringUtils.hasText(location)) {
                String locPattern = "%" + location.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")), locPattern));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
