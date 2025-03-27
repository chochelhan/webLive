package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastBoardArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BroadcastBoardArticleRepository extends JpaRepository<BroadcastBoardArticle, Long> {

    BroadcastBoardArticle getById(Long id);

    public List<BroadcastBoardArticle> getByParentId(Long parentId);

    @Query(value = "SELECT * FROM broadcast_board_article WHERE parent_id=:parentId ORDER BY notice DESC, id DESC", nativeQuery = true)
    public List<BroadcastBoardArticle> getArticleListByParentId(@Param(value = "parentId") Long parentId);


}
