package social.repository;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.jooq.impl.DSL.val;

@Repository
public class TestRepository {


    private final DSLContext dslContext;

    @Autowired
    public TestRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }


    public Long dummy() {
        Long cnt =
        dslContext.select(val("1234", Long.class))
            .fetchOneInto(Long.class);

        return cnt == null ? 0 : cnt;
    }
}
