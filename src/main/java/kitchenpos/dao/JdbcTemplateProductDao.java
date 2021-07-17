package kitchenpos.dao;

import kitchenpos.domain.ProductRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateProductDao implements ProductDao {
    private static final String TABLE_NAME = "product";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public ProductRequest save(final ProductRequest entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    @Override
    public Optional<ProductRequest> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProductRequest> findAll() {
        final String sql = "SELECT id, name, price FROM product";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private ProductRequest select(final Long id) {
        final String sql = "SELECT id, name, price FROM product WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private ProductRequest toEntity(final ResultSet resultSet) throws SQLException {
        final ProductRequest entity = new ProductRequest();
        entity.setId(resultSet.getLong(KEY_COLUMN_NAME));
        entity.setName(resultSet.getString("name"));
        entity.setPrice(resultSet.getBigDecimal("price"));
        return entity;
    }
}
