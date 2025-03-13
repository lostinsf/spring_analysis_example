package kr.spr.analysis.example25001.spring_analysis_example.level2.readonly.repository;

import kr.spr.analysis.example25001.spring_analysis_example.level2.readonly.entity.MySiteUserMusicInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface MySiteUserMusicInfoRepository extends
    JpaRepository<MySiteUserMusicInfoEntity, Long> {

    @NonNull
    public Page<MySiteUserMusicInfoEntity> findAll(@Nullable Pageable pageable);

    @NonNull
    public Page<MySiteUserMusicInfoEntity> findByDeletedDateTimeIsNull(@Nullable Pageable pageable);

    @NonNull
    public Page<MySiteUserMusicInfoEntity> findByChannelIdAndDeletedDateTimeIsNullOrderById(
        @Nullable String channelId,
        Pageable pageable);

}
