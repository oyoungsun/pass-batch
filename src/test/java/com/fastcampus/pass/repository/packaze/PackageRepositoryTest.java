package com.fastcampus.pass.repository.packaze;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @Test
    public void test_save(){

        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        //when
        packageRepository.save(packageEntity);

        //then
        assertNotNull(packageEntity.getPackageSeq()); //자동으로 ai 될 것이다.
        System.out.println("번호 출력"+packageEntity.getPackageSeq());

    }

    @Test
    public void test_findByCreatedAtAfter(){ //특정 생성시점 이후의 값 가져오기
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1); //지금 시점으로부터 1분 전

        PackageEntity packageEntity0 = new PackageEntity();
        packageEntity0.setPackageName("학생 전용 3개월");
        packageEntity0.setPeriod(90);
        packageRepository.save(packageEntity0);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 6개월");
        packageEntity1.setPeriod(180);
        packageRepository.save(packageEntity1); //2개 생성
        PackageEntity packageEntity2 = new PackageEntity();
        packageEntity2.setPackageName("학생 전용 62개월");
        packageEntity2.setPeriod(180);
        packageRepository.save(packageEntity2); //2개 생성
        //when - select time -> order by
        final List<PackageEntity> packageEntities = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0,1, Sort.by("packageSeq").descending()));
        //then
        assertEquals(1, packageEntities.size()); //현재 이후에 생성된 애들이 들어간다.
        //assertEquals(packageEntity1.getPackageSeq(), packageEntities.get(0).getPackageSeq());
        System.out.println(packageEntities.get(0).getPackageName()); //2번쨰 녀석만 선택된다.

        //3개를 넣어줘도 한개만 가져옴
    }

    @Test
    public void test_updateCountAndPeriod(){
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);


        //when
        int updateCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final PackageEntity updatedPackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //then
        assertEquals(1, updatedPackageEntity.getPackageSeq());
        assertEquals(30, updatedPackageEntity.getCount());
        assertEquals(120, updatedPackageEntity.getPeriod());
    }

    @Test
    public void test_delete(){
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("제거할 이용권");
        packageEntity.setCount(1);
        PackageEntity newPackageEntity = packageRepository.save(packageEntity);

        //when
        packageRepository.deleteById(newPackageEntity.getPackageSeq());

        //then
        assertThat(packageRepository.findById(newPackageEntity.getPackageSeq()).isEmpty());
    }
}