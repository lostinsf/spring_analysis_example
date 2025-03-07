package kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository;

import kr.spr.analysis.example25001.spring_analysis_example.level2.crud.entity.ExampleMusicInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleMusicInfoRepository extends JpaRepository<ExampleMusicInfoEntity, Long> {

    @NonNull
    public Page<ExampleMusicInfoEntity> findAll(@Nullable Pageable pageable);

    @NonNull
    public Page<ExampleMusicInfoEntity> findByDeletedDateTimeIsNull(@Nullable Pageable pageable);

    @NonNull
    public Page<ExampleMusicInfoEntity> findByTitleAndArtistNameAndDeletedDateTimeIsNull(
            @Nullable String title, @Nullable String artistName, Pageable pageable);

}
