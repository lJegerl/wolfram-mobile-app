package ru.ctraltdefeat.DemoWolframSingle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ctraltdefeat.DemoWolframSingle.domain.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<RoleType, Integer> {

}
