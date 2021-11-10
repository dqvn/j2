package com.test.service;

import com.test.domain.*; // for static metamodels
import com.test.domain.Blog;
import com.test.repository.BlogRepository;
import com.test.service.criteria.BlogCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Blog} entities in the database.
 * The main input is a {@link BlogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Blog} or a {@link Page} of {@link Blog} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BlogQueryService extends QueryService<Blog> {

    private final Logger log = LoggerFactory.getLogger(BlogQueryService.class);

    private final BlogRepository blogRepository;

    public BlogQueryService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    /**
     * Return a {@link List} of {@link Blog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Blog> findByCriteria(BlogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Blog> specification = createSpecification(criteria);
        return blogRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Blog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Blog> findByCriteria(BlogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Blog> specification = createSpecification(criteria);
        return blogRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BlogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Blog> specification = createSpecification(criteria);
        return blogRepository.count(specification);
    }

    /**
     * Function to convert {@link BlogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Blog> createSpecification(BlogCriteria criteria) {
        Specification<Blog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Blog_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Blog_.name));
            }
            if (criteria.getHandle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHandle(), Blog_.handle));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Blog_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
