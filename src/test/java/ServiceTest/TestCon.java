package ServiceTest;

import Utils.DruidPoor;
import generator.domain.Emp;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/6/19
 * 说明:
 */
public class TestCon {
    @Test
    void testCon() throws SQLException {
        Connection con = DruidPoor.con();
        QueryRunner queryRunner = new QueryRunner();
        String sql="select * from emp";
        List<Emp> query = queryRunner.query(con, sql, new BeanListHandler<>(Emp.class));
        query.forEach(e->{
            System.out.println(e.toString());
        });
    }

}
