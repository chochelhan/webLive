package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    public Member getById(Long id);


    public Member findByZid(String zid);

    public Member findByUid(String uid);

    public Member findByUidAndPasswdAndRole(String uid, String passwd,String role);

    public Member findByEmail(String email);

    public Member findByEmailAndIdNot(String email,Long id);


    public List<Member> findByRole(String role);


    @Query(value = "UPDATE member SET access_token=:accessToken,refresh_token=:refreshToken,expires=:expires,update_at=:updateAt  WHERE id=:id", nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSQLMemberZoomToken(@Param(value = "accessToken") String accessToken, @Param(value = "refreshToken") String refreshToken, @Param(value = "expires") int expires, @Param(value = "updateAt") LocalDateTime updateAt, @Param(value = "id") Long id);

}
