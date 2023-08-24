package cc.cybereflex.infrastructure.type_handler;

import cc.cybereflex.common.component.Json;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class JsonTypeHandler extends BaseTypeHandler<Map<String, Object>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) {

    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getString(columnName));
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getString(columnIndex));
    }

    private Map<String, Object> convert(String value) throws SQLException {
        try {
            if (StringUtils.isBlank(value)) return Collections.emptyMap();

            Optional<Map<String, Object>> data = Json.parse(value, new TypeReference<>() {});
            return data.orElse(Collections.emptyMap());
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}
