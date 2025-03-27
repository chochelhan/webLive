package com.riverflow.livegoapp.Repository.specification;

import com.riverflow.livegoapp.Entity.Member;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class MemberSpecification {

    public static Specification<Member> likeName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),"%"+name+"%");
    }
    public static Specification<Member> likeUid(String uid) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("uid"),"%"+uid+"%");
    }
    public static Specification<Member> likeEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"),"%"+email+"%");
    }

    public static Specification<Member> equalEmailSend(String emailSend) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("emailSend"),emailSend);
    }

    public static Specification<Member> equalRole(String role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"),role);
    }

    public static Specification<Member> equalAuth(String auth) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("auth"),auth);
    }

    public static Specification<Member> equalNotRole(String role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("role"),role);
    }
    public static Specification<Member> equalNotUout(String uout) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("uout"),uout);
    }

    public static Specification<Member> startCreateAt(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createAt"),date);
    }
    public static Specification<Member> endCreateAt(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createAt"),date);
    }

}
