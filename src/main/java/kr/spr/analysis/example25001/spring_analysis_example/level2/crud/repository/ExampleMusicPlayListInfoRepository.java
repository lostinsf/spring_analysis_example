package kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository;

import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.entity.ExampleMusicPlayListInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleMusicPlayListInfoRepository extends
    JpaRepository<ExampleMusicPlayListInfoEntity, Long> {

    @NonNull
    public Page<ExampleMusicPlayListInfoEntity> findAll(@Nullable Pageable pageable);

    @NonNull
    public Page<ExampleMusicPlayListInfoEntity> findByDeletedDateTimeIsNull(@Nullable Pageable pageable);

    @NonNull
    public Page<ExampleMusicPlayListInfoEntity> findByChannelIdCodeAndDeletedDateTimeIsNullOrderById(
        @Nullable String channelIdCode,
        Pageable pageable);

    @NonNull
    public Page<ExampleMusicPlayListInfoEntity> findByPlaylistIdCodeAndDeletedDateTimeIsNullOrderByIdDesc(
        @Nullable String playlistIdCode,
        Pageable pageable);

}
