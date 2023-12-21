package gt.com.tigo.integradorhome.util;

import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class SpecFactory {

    private static final Boolean DISTINCT = false;
    public static final String YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private SpecFactory() {
        // do nothing
    }

    /**
     * Builds a DISTINCT predicate. ALPHA
     * @return A DISTINCT predicate.
     */
    public static Specification<Object> getDistinctSpecification() {
        return (root, query, cb) -> {
            query.distinct(true);
            return null;
        };
    }

    /**
     * Builds a default predicate equivalent to 1 == 1.
     * @return A default predicate.
     */
    public static Specification<Object> getDefaultSpecification() {
        return (root, query, builder) -> builder.conjunction();
    }

    /**
     * Builds a predicate using the LIKE operator.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param operator The operator to use. Can use LIKE, STARTSWITH, ENDSWITH
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the LIKE operator.
     */
    public static <T> Specification<T> getLikeSpecification(final String fieldName, final String filterValue, final String operator) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate = filterValue == null ?
                        builder.conjunction() :
                        builder.like(builder.upper(root.<String>get(fieldName)), String.format(getOperator(operator), filterValue.toUpperCase()));

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    public static <T> Specification<T> getLikeSpecificationN(final String fieldName, final String filterValue) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate = filterValue == null ?
                        builder.conjunction() :
                        (filterValue.equals("NULL") ?
                                builder.isNull(builder.upper(root.<String>get(fieldName))):
                                builder.like(builder.upper(root.<String>get(fieldName)), String.format(getOperator("LIKE"), filterValue.toUpperCase())));

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate using the LIKE operator.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the LIKE operator.
     */
    public static <T> Specification<T> getLikeSpecification(final String fieldName, final String filterValue) {
        return getLikeSpecification(fieldName, filterValue, "LIKE");
    }

    /**
     * Builds a predicate using the LIKE operator.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param operator The operator to use. Can use LIKE, STARTSWITH, ENDSWITH
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the LIKE operator.
     */
    public static <T> Specification<T> getLikeSpecification(final String fieldName, final Double filterValue, final String operator) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate = filterValue == null ?
                        builder.conjunction() :
                        builder.like(builder.upper(root.<String>get(fieldName)), String.format(getOperator(operator), filterValue));

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate using the LIKE operator.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the LIKE operator.
     */
    public static <T> Specification<T> getLikeSpecification(final String fieldName, final Double filterValue) {
        return getLikeSpecification(fieldName, filterValue, "LIKE");
    }

    /**
     * Builds a predicate using the LIKE operator.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param operator The operator to use. Can use LIKE, STARTSWITH, ENDSWITH
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the LIKE operator.
     */
    public static <T> Specification<T> getLikeSpecification(final String fieldName, final Long filterValue, final String operator) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate = filterValue == null ?
                        builder.conjunction() :
                        builder.like(builder.upper(root.<String>get(fieldName)), String.format(getOperator(operator), filterValue));

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate using the LIKE operator.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the LIKE operator.
     */
    public static <T> Specification<T> getLikeSpecification(final String fieldName, final Long filterValue) {
        return getLikeSpecification(fieldName, filterValue, "LIKE");
    }

    /**
     * Returns comparison operator from name.
     * @param operator The name of the operator.
     * @return The value of the operator.
     */
    private static String getOperator(String operator) {
        if (operator != null && operator.equalsIgnoreCase("STARTSWITH")) {
            return "%%%s";
        } else if (operator != null && operator.equalsIgnoreCase("ENDSWITH")) {
            return "%s%%";
        }

        return "%%%s%%";
    }

    /**
     * Builds a predicate using the BETWEEN operator. It works for dates.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the BETWEEN operator.
     */
    public static <T> Specification<T> getBetweenSpecification(final String fieldName, final Date startDate, final Date endDate) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate =  startDate == null || endDate == null ?
                        builder.conjunction() :
                        builder.between(root.<Date>get(fieldName), startDate, endDate);

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate using the EQUAL operator. It works for strings.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the EQUAL operator.
     */
    public static <T> Specification<T> getEqualSpecification(final String fieldName, final String filterValue) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate =  filterValue == null ?
                        builder.conjunction() :
                        builder.equal(builder.upper(root.<String>get(fieldName)), filterValue.toUpperCase());

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }


    /**
     * Builds a predicate using the GREATER THAN OR EQUAL TO (>=) operator. It works for dates.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the GREATER THAN OR EQUAL TO (>=) operator.
     */
    public static <T> Specification<T> getGreaterThanOrEqualToSpecification(final String fieldName, final Date filterValue) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate =  filterValue == null ?
                        builder.conjunction() :
                        builder.greaterThanOrEqualTo(root.<Date>get(fieldName), filterValue);

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate using the EQUAL operator. It works with long values.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the EQUAL operator.
     */
    public static <T> Specification<T> getEqualSpecification(final String fieldName, final Long filterValue) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate =  filterValue == null ?
                        builder.conjunction() :
                        builder.equal(root.<Long>get(fieldName), filterValue);

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate using the EQUAL operator. It works with double values.
     * @param fieldName The field name to use in filter.
     * @param filterValue The value to filter.
     * @param <T> The type of the built specification.
     * @return A predicate comparing two values using the EQUAL operator.
     */
    public static <T> Specification<T> getEqualSpecification(final String fieldName, final Double filterValue) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Predicate predicate =  filterValue == null ?
                        builder.conjunction() :
                        builder.equal(root.<Double>get(fieldName), filterValue);

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate by joining T and U tables. It uses the LIKE operator.
     * @param parentFieldName
     * @param childFieldName
     * @param filterValue
     * @param operator
     * @param <T> The type of the parent table.
     * @param <U> The type of the child table.
     * @return A predicate comparing two values using the LIKE operator.
     */
    public static <T, U> Specification<T> getLikeChildSpecification(final String parentFieldName, final String childFieldName, final String filterValue, final String operator) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Join<T, U> join = getJoin(root, parentFieldName);

                final Predicate predicate =  filterValue == null ?
                        builder.conjunction() :
                        builder.like(builder.upper(join.<String>get(childFieldName)), String.format(getOperator(operator), filterValue.toUpperCase()));

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate by joining T and U tables. It uses the LIKE operator.
     * @param parentFieldName
     * @param childFieldName
     * @param filterValue
     * @param <T> The type of the parent table.
     * @param <U> The type of the child table.
     * @return A predicate comparing two values using the LIKE operator.
     */
    public static <T, U> Specification<T> getLikeChildSpecification(final String parentFieldName, final String childFieldName, final String filterValue) {
        return SpecFactory.<T, U>getLikeChildSpecification(parentFieldName, childFieldName, filterValue, "LIKE");
    }

    public static <T, U> Join<T, U> getJoin(Root<T> root, String joinFieldName, JoinType joinType) {
        Iterator<Join<T, ?>> joins = root.getJoins().iterator();

        while (joins.hasNext()) {
            Join<T, U> tempJoin = (Join<T, U>) joins.next();

            if (tempJoin.getAttribute().getName().equals(joinFieldName)) {
                return tempJoin;
            }
        }

        return root.join(joinFieldName, joinType);
    }

    private static <T, U> Join<T, U> getJoin(Root<T> root, String joinFieldName) {
        return getJoin(root, joinFieldName, JoinType.INNER);
    }

    /**
     * Builds a predicate by joining T and U tables. It uses the EQUAL operator and works for strings.
     * @param parentFieldName
     * @param childFieldName
     * @param filterValue
     * @param <T> The type of the parent table.
     * @param <U> The type of the child table.
     * @return A predicate comparing two values using the EQUAL operator.
     */
    public static <T, U> Specification<T> getEqualChildSpecification(final String parentFieldName, final String childFieldName, final String filterValue, final JoinType joinType) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Join<T, U> join = getJoin(root, parentFieldName, joinType);

                final Predicate predicate =  filterValue == null ?
                        builder.conjunction() :
                        builder.equal(builder.upper(join.<String>get(childFieldName)), filterValue.toUpperCase());

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate by joining T and U tables. It uses the EQUAL operator and works for strings.
     * @param parentFieldName
     * @param childFieldName
     * @param filterValue
     * @param <T> The type of the parent table.
     * @param <U> The type of the child table.
     * @return A predicate comparing two values using the EQUAL operator.
     */
    public static <T, U> Specification<T> getEqualChildSpecification(final String parentFieldName, final String childFieldName, final String filterValue) {
        return SpecFactory.<T,U>getEqualChildSpecification(parentFieldName, childFieldName, filterValue, JoinType.INNER);
    }

    /**
     * Builds a predicate by joining T and U tables. It uses the EQUAL operator and works for long values.
     * @param parentFieldName
     * @param childFieldName
     * @param filterValue
     * @param <T> The type of the parent table.
     * @param <U> The type of the child table.
     * @return A predicate comparing two values using the EQUAL operator.
     */
    public static <T, U> Specification<T> getEqualChildSpecification(final String parentFieldName, final String childFieldName, final Long filterValue, final JoinType joinType) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Join<T, U> join = getJoin(root, parentFieldName, joinType);

                final Predicate predicate =  filterValue == null ?
                        builder.conjunction() :
                        builder.equal(join.get(childFieldName), filterValue);

                query.distinct(DISTINCT);

                return predicate;
            }
        };
    }

    /**
     * Builds a predicate by joining T and U tables. It uses the EQUAL operator and works for long values.
     * @param parentFieldName
     * @param childFieldName
     * @param filterValue
     * @param <T> The type of the parent table.
     * @param <U> The type of the child table.
     * @return A predicate comparing two values using the EQUAL operator.
     */
    public static <T, U> Specification<T> getEqualChildSpecification(final String parentFieldName, final String childFieldName, final Long filterValue) {
        return SpecFactory.<T, U>getEqualChildSpecification(parentFieldName, childFieldName, filterValue, JoinType.INNER);
    }

    /**
     * Gets a string value from a map.
     * @param map The map.
     * @param key The key to search for.
     * @return The string value.
     */
    public static String getValueAsString(Map<String, FilterInfoDto> map, String key) {
        return (map.containsKey(key) && map.get(key) != null) ? map.get(key).getValue() : null;
    }

    /**
     * Gets a long value from a map.
     * @param map The map.
     * @param key The key to search for.
     * @return The long value.
     */
    public static Long getValueAsLong(Map<String, FilterInfoDto> map, String key) {
        return (map.containsKey(key) && map.get(key) != null) ? Long.parseLong(map.get(key).getValue()) : null;
    }

    /**
     * Gets a double value from a map.
     * @param map The map.
     * @param key The key to search for.
     * @return The double value.
     */
    public static Double getValueAsDouble(Map<String, FilterInfoDto> map, String key) {
        return (map.containsKey(key) && map.get(key) != null) ? Double.parseDouble(map.get(key).getValue()) : null;
    }

    /**
     * Gets a value from a map.
     * @param map The map.
     * @param key The key to search for.
     * @return The date value.
     */
    public static Date getValueAsDate(Map<String, FilterInfoDto> map, String key) {
        DateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_SSS);

        try {
            return (map.containsKey(key) && map.get(key) != null) ? sdf.parse(map.get(key).getValue()) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Gets a value from a map.
     * @param map The map.
     * @param key The key to search for.
     * @return The zoned date value.
     */
    public static Date getValueAsZonedDate(Map<String, FilterInfoDto> map, String key) {
        DateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_SSS);

        try {
            Date date = (map.containsKey(key) && map.get(key) != null) ? sdf.parse(map.get(key).getValue()) : null;

            if (date != null) {
                return removeOffset(date);
            }

            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Gets a zoned date from a timezone agnostic date.
     * @param timezoneAgnosticDate The timezone agnostic date.
     * @return The zoned date value.
     */
    public static Date getValueAsZonedDate(String timezoneAgnosticDate) {
        DateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_SSS);

        try {
            Date date = sdf.parse(timezoneAgnosticDate);

            if (date != null) {
                return removeOffset(date);
            }

            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Removes an offset from a zoned date.
     * @param date The date to remove offset.
     * @return The date without the offset.
     */
    public static Date removeOffset(Date date) {
        // create timestamp
        Timestamp ts = new Timestamp(date.getTime());

        // create instant
        ZonedDateTime instant = ts.toInstant().atZone(ZoneId.of("America/Guatemala"));

        // get offset millis
        int offsetMillis = instant.getOffset().getTotalSeconds() * 1000;

        // substract offset from date
        ts.setTime(date.getTime() + offsetMillis);

        // update date
        date.setTime(ts.getTime());

        return date;
    }

    /**
     * Gets a operator from a map.
     * @param map The map.
     * @param key The key to search for.
     * @return The operator name.
     */
    public static String getOperator(Map<String, FilterInfoDto> map, String key) {
        return (map.containsKey(key) && map.get(key) != null) ? map.get(key).getOperator() : null;
    }

    /**
     * Converts a list of filters to a map.
     * @param filters The filter.
     * @return a Map with the name of the filter column as key.
     */
    public static Map<String, FilterInfoDto> getFiltersAsMap(List<FilterInfoDto> filters) {
        Map<String, FilterInfoDto> filtersInfo = new HashMap<>();

        // get column names to filter
        if (filters != null) {
            for (FilterInfoDto filterInfo : filters) {
                filtersInfo.put(filterInfo.getId(), filterInfo);
            }
        }

        return filtersInfo;
    }

    public static List<FilterInfoDto> getMapAsFilters(Map<String, FilterInfoDto> filters) {
        List<FilterInfoDto> filtersInfo = new ArrayList<>();

        for (FilterInfoDto filterInfo : filters.values()) {
            filtersInfo.add(filterInfo);
        }

        return filtersInfo;
    }

    /**
     * Removes a filter entry from a list of filters.
     * @param key The key to remove.
     * @return an updated list without the removed filter.
     */
    public static boolean removeFilter(List<FilterInfoDto> filters, String filterIdToRemove) {
        FilterInfoDto filterToRemove = null;

        for (FilterInfoDto filter : filters) {
            if (filter.getId().equals(filterIdToRemove)) {
                filterToRemove = filter;
                break;
            }
        }

        if (filterToRemove != null) {
            filters.remove(filterToRemove);

            return true;
        }

        return false;
    }

    /**
     * Returns a conjunction predicate. That is, 1 = 1.
     * @param <T>
     * @return The conjunction predicate.
     */
    public static <T> Specification<T> getConjunction() {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.conjunction();
            }
        };
    }

}

