package org.springframework.grpc.sample.infrastructure.out.persistence.adapter;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.grpc.sample.application.port.out.JDBCRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JDBCRepositoryImpl implements JDBCRepository {

    private static final Map<Class<?>, TableMeta> TABLE_MAP = new ConcurrentHashMap<>();

    protected final NamedParameterJdbcTemplate jdbcTemplate;

    public JDBCRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Transactional
    @Override
    public <ID> int updateOne(Class<?> entityClass, ID id, Map<String, Object> updates) {
        TableMeta tableMeta = resolveTableMeta(entityClass);
        validateUpdatesMap(tableMeta, updates);

        String tableName = tableMeta.tableName;
        String primaryKeyColumn = tableMeta.primaryKeyColumn;
        String sql = "UPDATE " + tableName + " SET " +
            updates.keySet().stream().map(key -> key + " = :" + key).collect(Collectors.joining(", ")) +
            " WHERE " + primaryKeyColumn + "= :ID";
        Map<String, Object> params = new HashMap<>(updates);
        params.put("ID", id);

        //noinspection SqlSourceToSinkFlow
        return jdbcTemplate.update(sql, params);
    }

    @Transactional
    @Override
    public <ID> int updateMany(Class<?> entityClass, Collection<ID> ids, Map<String, Object> updates) {
        TableMeta tableMeta = resolveTableMeta(entityClass);
        validateUpdatesMap(tableMeta, updates);

        String tableName = tableMeta.tableName;
        String primaryKeyColumn = tableMeta.primaryKeyColumn;
        String sql = "UPDATE " + tableName + " SET " +
            updates.keySet().stream().map(key -> key + " = :" + key).collect(Collectors.joining(", ")) +
            " WHERE " + primaryKeyColumn + " IN (:IDS)";
        Map<String, Object> params = new HashMap<>(updates);
        params.put("IDS", ids);

        //noinspection SqlSourceToSinkFlow
        return jdbcTemplate.update(sql, params);
    }

    private void validateUpdatesMap(TableMeta tableMeta, Map<String, Object> updates) {
        String primaryKeyColumn = tableMeta.primaryKeyColumn;
        if (updates.keySet().stream().anyMatch(k -> k.equalsIgnoreCase(primaryKeyColumn))) {
            throw new IllegalArgumentException("Updates must not contain primary key field: " + primaryKeyColumn);
        }
    }

    private static TableMeta resolveTableMeta(Class<?> entityClass) {
        return TABLE_MAP.computeIfAbsent(entityClass, TableMeta::new);
    }

    @Getter
    private static class TableMeta {
        private final String tableName;
        private final String primaryKeyColumn;
        private final Class<?> entityClass;

        public TableMeta(Class<?> entityClass) {
            this.entityClass = entityClass;
            this.tableName = findTableName(entityClass);
            this.primaryKeyColumn = findPrimaryKeyColumn(entityClass);
        }

        private static String findTableName(Class<?> entityClass) {
            Table tableAnnotation = entityClass.getAnnotation(Table.class);
            if (tableAnnotation == null) {
                throw new IllegalArgumentException(entityClass.getName() + " is not annotated with @" + Table.class.getSimpleName());
            }
            return tableAnnotation.name();
        }

        private static String findPrimaryKeyColumn(Class<?> entityClass) {
            Field primaryField = ReflectionUtils.findField(entityClass, new ReflectionUtils.AnnotationFieldFilter(Id.class));
            if (primaryField == null) {
                throw new IllegalStateException(entityClass.getName() + " does not have a primary key annotated with @" + Id.class.getSimpleName());
            }
            if (!BeanUtils.isSimpleProperty(primaryField.getType())) {
                throw new IllegalStateException(entityClass.getName() + " primary key field " + primaryField.getName() + " is not a simple property");
            }
            Column colAnnotation = primaryField.getAnnotation(Column.class);
            if (colAnnotation != null) {
                return colAnnotation.name();
            }
            return primaryField.getName();
        }
    }
}
